package com.ehhthan.mmobuffs.api;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.ehhthan.mmobuffs.api.tag.CustomTagTypes;
import com.ehhthan.mmobuffs.comp.stat.StatHandler;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static com.ehhthan.mmobuffs.util.KeyUtil.key;

public class EffectHolder implements PersistentDataHolder {
    private static final NamespacedKey EFFECTS = key("effects");

    private static final Function<ConfigurationSection, BossBar> barSupplier = (section) -> {
        BossBar.Color color = BossBar.Color.NAMES.value(section.getString("color", "white").toLowerCase(Locale.ROOT));
        if (color == null)
            color = BossBar.Color.WHITE;

        BossBar.Overlay overlay = BossBar.Overlay.NAMES.value(section.getString("overlay", "progress").toLowerCase(Locale.ROOT));
        if (overlay == null)
            overlay = BossBar.Overlay.PROGRESS;

        return BossBar.bossBar(Component.empty(), section.getInt("value", 1), color, overlay);
    };

    private static final Map<Player, EffectHolder> DATA = new HashMap<>();

    private final Map<NamespacedKey, ActiveStatusEffect> effects = new HashMap<>();


    private final Player player;
    private BossBar bossBar;

    @SuppressWarnings("ConstantConditions")
    public EffectHolder(@NotNull Player player) {
        this.player = player;
        FileConfiguration config = MMOBuffs.getInst().getConfig();

        if (config.isConfigurationSection("bossbar-display") && config.getBoolean("bossbar-display.enabled", true))
            this.bossBar = barSupplier.apply(config.getConfigurationSection("bossbar-display"));

        if (getPersistentDataContainer().has(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS)) {
            ActiveStatusEffect[] savedEffects = getPersistentDataContainer().get(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS);
            if (savedEffects != null && savedEffects.length > 0)
                for (ActiveStatusEffect effect : savedEffects) {
                    if (effect != null)
                        addEffect(Modifier.SET, effect);
                }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    cancel();

                boolean display = false;

                if (effects.size() > 0) {
                    Collection<ActiveStatusEffect> values = effects.values();
                    for (ActiveStatusEffect activeEffect : values) {
                        NamespacedKey key = activeEffect.getStatusEffect().getKey();
                        if (activeEffect.tick()) {
                            updateEffect(key);
                        }

                        if (!activeEffect.isActive())
                            removeEffect(key);
                        else if (activeEffect.getStatusEffect().hasDisplay())
                            display = true;
                    }
                    save();
                }

                if (bossBar != null) {
                    if (display) {
                        TextComponent.Builder builder = Component.text();
                        // Creates a list of the displayable active status effects in ascending order.
                        List<ActiveStatusEffect> sortedEffects = effects.values().stream().filter(e -> e.getStatusEffect().hasDisplay()).sorted().toList();

                        // TODO: 1/6/2022 Sorting option in config
                        // Checks if the effects should be descending and reverses if true.
                        if (!MMOBuffs.getInst().getConfig().getBoolean("sorting.duration-ascending", true))
                            Collections.reverse(sortedEffects);


                        for (int i = 0; i < sortedEffects.size(); i++) {
                            ActiveStatusEffect effect = sortedEffects.get(i);
                            builder.append(effect.getStatusEffect().getDisplay().build(player, effect));

                            // Joins components with the separator.
                            if (i != sortedEffects.size() - 1) {
                                builder.append(Component.text(config.getString("bossbar-display.effect-separator", " ")));
                            }
                        }

                        bossBar.name(builder.build());
                        player.showBossBar(bossBar);
                    } else {
                        player.hideBossBar(bossBar);
                    }

                }
            }
        }.runTaskTimer(MMOBuffs.getInst(), 1, 20);
    }

    public Player getPlayer() {
        return player;
    }

    private void save() {
        ActiveStatusEffect[] activeStatusEffects = effects.values().toArray(new ActiveStatusEffect[0]);
        getPersistentDataContainer().set(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS, activeStatusEffects);
    }

    public void updateEffect(NamespacedKey key) {
        ActiveStatusEffect activeEffect = getEffect(key);
        if (MMOBuffs.getInst().hasStatHandler() && activeEffect.getStatusEffect().hasStats()) {
            MMOBuffs.getInst().getStatHandler().edit(EffectHolder.this, StatHandler.EditType.ADD, activeEffect);
        }
    }

    public void addEffect(Modifier modifier, ActiveStatusEffect effect) {
        NamespacedKey key = effect.getStatusEffect().getKey();

        if (effects.containsKey(key)) {
            effects.put(key, effects.get(key).merge(modifier, effect));
        } else {
            effects.put(key, effect);
        }
        if (MMOBuffs.getInst().hasStatHandler() && effect.getStatusEffect().hasStats())
            MMOBuffs.getInst().getStatHandler().edit(this, StatHandler.EditType.ADD, effect);
    }

    public void removeEffect(NamespacedKey key) {
        ActiveStatusEffect activeEffect = getEffect(key);
        if (MMOBuffs.getInst().hasStatHandler() && activeEffect.getStatusEffect().hasStats())
            MMOBuffs.getInst().getStatHandler().edit(this, StatHandler.EditType.REMOVE, activeEffect);
        effects.remove(key);
    }

    public void removeAllEffects(boolean includePermanent) {
        for (NamespacedKey key : getEffectsKeys(includePermanent))
            removeEffect(key);
    }

    public boolean hasEffect(NamespacedKey key) {
        return effects.containsKey(key);
    }

    public ActiveStatusEffect getEffect(NamespacedKey key) {
        return effects.get(key);
    }

    public List<NamespacedKey> getEffectsKeys(boolean includePermanent) {
        return getEffects(includePermanent).stream().map(effect -> effect.getStatusEffect().getKey()).toList();
    }

    public Collection<ActiveStatusEffect> getEffects(boolean includePermanent) {
        Collection<ActiveStatusEffect> values = effects.values();

        if (!includePermanent)
            values.removeIf(ActiveStatusEffect::isPermanent);

        return values;
    }

    public static EffectHolder get(Player player) {
        return DATA.get(player);
    }

    public static boolean has(Player player) {
        if (player == null)
            return false;

        return DATA.containsKey(player);
    }

    public static Collection<EffectHolder> getHolders() {
        return DATA.values();
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return player.getPersistentDataContainer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EffectHolder holder = (EffectHolder) o;

        return player.equals(holder.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    public static class PlayerListener implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent e) {
            DATA.put(e.getPlayer(), new EffectHolder(e.getPlayer()));
        }

        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            DATA.remove(e.getPlayer());
        }
    }
}
