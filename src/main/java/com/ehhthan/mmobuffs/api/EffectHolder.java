package com.ehhthan.mmobuffs.api;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.ehhthan.mmobuffs.api.tag.CustomTagTypes;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ehhthan.mmobuffs.util.KeyUtil.key;

public class EffectHolder implements PersistentDataHolder {
    private static final NamespacedKey EFFECTS = key("effects");

    private static final Map<Player, EffectHolder> DATA = new HashMap<>();

    private final Map<NamespacedKey, ActiveStatusEffect> effects = new HashMap<>();

    private final Player player;
    private final BossBar bossBar;

    @SuppressWarnings("ConstantConditions")
    public EffectHolder(@NotNull Player player) {
        this.player = player;

        FileConfiguration config = MMOBuffs.getInst().getConfig();
        this.bossBar = BossBar.bossBar(Component.empty()
            , config.getInt("bossbar-display.value", 1)
            , BossBar.Color.NAMES.value(config.getString("bossbar-display.color", "white").toLowerCase(Locale.ROOT))
            , BossBar.Overlay.NAMES.value(config.getString("bossbar-display.overlay", "progress").toLowerCase(Locale.ROOT)));

        if (getPersistentDataContainer().has(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS)) {
            ActiveStatusEffect[] savedEffects = getPersistentDataContainer().get(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS);
            if (savedEffects != null && savedEffects.length > 0)
                for (ActiveStatusEffect effect : savedEffects) {
                    effects.put(effect.getEffect().getKey(), effect);
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
                        activeEffect.tick();

                        if (!activeEffect.isActive())
                            effects.remove(activeEffect.getEffect().getKey());
                        else if (activeEffect.isDisplayable())
                            display = true;
                    }
                    save();
                }

                if (config.getBoolean("bossbar-display.enabled", true) && display) {
                    TextComponent.Builder builder = Component.text();

                    List<ActiveStatusEffect> sortedEffects = EffectHolder.this.effects.values().stream().sorted(Comparator.comparingInt(ActiveStatusEffect::getDuration)).toList();
                    for (int i = 0; i < sortedEffects.size(); i++) {
                        ActiveStatusEffect effect = sortedEffects.get(i);
                        builder.append(effect.getEffect().getDisplay().build(effect));

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
        }.runTaskTimer(MMOBuffs.getInst(), 1, 20);
    }

    public Player getPlayer() {
        return player;
    }

    private void save() {
        ActiveStatusEffect[] activeStatusEffects = effects.values().toArray(new ActiveStatusEffect[0]);
        getPersistentDataContainer().set(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS, activeStatusEffects);
    }

    public void addEffect(Modifier modifier, ActiveStatusEffect effect) {
        NamespacedKey key = effect.getEffect().getKey();
        if (effects.containsKey(key)) {
            effects.put(key, effects.get(key).merge(modifier, effect));
        } else {
            effects.put(key, effect);
        }
    }

    public void removeEffect(ActiveStatusEffect effect) {
        removeEffect(effect.getEffect().getKey());
    }

    public void removeEffect(NamespacedKey key) {
        effects.remove(key);
    }

    public boolean hasEffect(NamespacedKey key) {
        return effects.containsKey(key);
    }

    public ActiveStatusEffect getEffect(NamespacedKey key) {
        return effects.get(key);
    }

    public List<NamespacedKey> getEffects(boolean includePermanent) {
        Collection<ActiveStatusEffect> values = effects.values();

        if (!includePermanent)
            values.removeIf(ActiveStatusEffect::isPermanent);

        return values.stream().map(effect -> effect.getEffect().getKey()).toList();
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
            DATA.get(e.getPlayer()).save();
            DATA.remove(e.getPlayer());
        }
    }
}
