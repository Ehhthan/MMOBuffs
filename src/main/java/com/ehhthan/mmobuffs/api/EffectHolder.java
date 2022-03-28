package com.ehhthan.mmobuffs.api;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.ehhthan.mmobuffs.api.tag.CustomTagTypes;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static com.ehhthan.mmobuffs.util.KeyUtil.key;

public class EffectHolder implements PersistentDataHolder {
    private static final NamespacedKey EFFECTS = key("effects");

    // This supplies a blank boss bar with the configured color and overlay.
    private static final Function<ConfigurationSection, BossBar> BAR_SUPPLIER = (section) -> {
        BossBar.Color color = BossBar.Color.NAMES.value(section.getString("color", "white").toLowerCase(Locale.ROOT));
        if (color == null)
            color = BossBar.Color.WHITE;

        BossBar.Overlay overlay = BossBar.Overlay.NAMES.value(section.getString("overlay", "progress").toLowerCase(Locale.ROOT));
        if (overlay == null)
            overlay = BossBar.Overlay.PROGRESS;

        return BossBar.bossBar(Component.empty(), section.getInt("value", 1), color, overlay);
    };

    private static final Map<Player, EffectHolder> DATA = new HashMap<>();

    private final Player player;

    private BossBar bossBar;
    private final Component separator;

    private final BukkitRunnable effectUpdater = new BukkitRunnable() {
        @Override
        public void run() {
            if (!player.isOnline())
                cancel();

            Iterator<ActiveStatusEffect> iterator = effects.values().iterator();
            while (iterator.hasNext()) {
                // Get the effect.
                ActiveStatusEffect effect = iterator.next();

                // Tick the effect and update if needed.
                if (effect.tick())
                    updateEffect(effect.getStatusEffect().getKey());

                // Remove if the effect is no longer active.
                if (!effect.isActive()) {
                    MMOBuffs.getInst().getStatManager().remove(EffectHolder.this, effect);
                    iterator.remove();
                }
            }
            // Save to the persistent data container.
            save();
        }
    };

    private final BukkitRunnable bossBarUpdater = new BukkitRunnable() {
        @Override
        public void run() {
            // Creates a list of the displayable active status effects in ascending order.
            List<ActiveStatusEffect> sortedEffects = new LinkedList<>(effects.values().stream().filter(e -> e.getStatusEffect().hasDisplay()).sorted().toList());
            if (sortedEffects.size() > 0) {
                TextComponent.Builder builder = Component.text();
                // Checks if the effects should be descending and reverses.
                if (!MMOBuffs.getInst().getConfig().getBoolean("sorting.duration-ascending", true))
                    Collections.reverse(sortedEffects);

                for (int i = 0; i < sortedEffects.size(); i++) {
                    // Joins with the separator if previous component exists.
                    if (i != 0) {
                        builder.append(separator);
                    }

                    ActiveStatusEffect effect = sortedEffects.get(i);
                    builder.append(effect.getStatusEffect().getDisplay().build(player, effect));
                }

                bossBar.name(builder.build());
                player.showBossBar(bossBar);
            } else {
                if (!MMOBuffs.getInst().getConfig().getBoolean("bossbar-display.display-when-empty", false))
                    player.hideBossBar(bossBar);
            }
        }
    };

    // The holder's effects.
    private final Map<NamespacedKey, ActiveStatusEffect> effects = new HashMap<>();

    public EffectHolder(@NotNull Player player) {
        this.player = player;
        FileConfiguration config = MMOBuffs.getInst().getConfig();

        if (config.isConfigurationSection("bossbar-display") && config.getBoolean("bossbar-display.enabled", true))
            this.bossBar = BAR_SUPPLIER.apply(config.getConfigurationSection("bossbar-display"));

        this.separator = Component.text(config.getString("bossbar-display.effect-separator", " "));

        // Load saved effects.
        if (getPersistentDataContainer().has(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS)) {
            ActiveStatusEffect[] savedEffects = getPersistentDataContainer().get(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS);
            if (savedEffects != null && savedEffects.length > 0)
                for (ActiveStatusEffect effect : savedEffects) {
                    if (effect != null)
                        addEffect(effect, Modifier.SET, Modifier.SET);
                }
        }

        effectUpdater.runTaskTimer(MMOBuffs.getInst(), 1, 20);
        bossBarUpdater.runTaskTimer(MMOBuffs.getInst(), 2, MMOBuffs.getInst().getConfig().getInt("bossbar-display.update-ticks", 20));

        if (MMOBuffs.getInst().getConfig().getBoolean("bossbar-display.display-when-empty", false))
            player.showBossBar(bossBar);
    }

    public Player getPlayer() {
        return player;
    }

    private void save() {
        ActiveStatusEffect[] activeStatusEffects = effects.values().toArray(new ActiveStatusEffect[0]);
        getPersistentDataContainer().set(EFFECTS, CustomTagTypes.ACTIVE_EFFECTS, activeStatusEffects);
    }

    public void updateEffect(NamespacedKey key) {
        Bukkit.getScheduler().runTask(MMOBuffs.getInst(), () -> MMOBuffs.getInst().getStatManager().add(this, effects.get(key)));
    }

    public void addEffect(ActiveStatusEffect effect, Modifier durationModifier, Modifier stackModifier) {
        NamespacedKey key = effect.getStatusEffect().getKey();

        Bukkit.getScheduler().runTask(MMOBuffs.getInst(), () -> {
            if (effects.containsKey(key))
                effects.put(key, effects.get(key).merge(effect, durationModifier, stackModifier));
            else
                effects.put(key, effect);

            MMOBuffs.getInst().getStatManager().add(this, effects.get(key));
        });
    }

    public void removeEffect(NamespacedKey key) {
        if (hasEffect(key))
            Bukkit.getScheduler().runTask(MMOBuffs.getInst(), () -> {
                MMOBuffs.getInst().getStatManager().remove(EffectHolder.this, effects.get(key));
                effects.remove(key);
        });
    }

    public void removeEffects(boolean includePermanent) {
        for (ActiveStatusEffect effect : getEffects(includePermanent)) {
            removeEffect(effect.getStatusEffect().getKey());
        }
    }

    public boolean hasEffect(NamespacedKey key) {
        return effects.containsKey(key);
    }

    public @Nullable ActiveStatusEffect getEffect(NamespacedKey key) {
        return effects.get(key);
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
