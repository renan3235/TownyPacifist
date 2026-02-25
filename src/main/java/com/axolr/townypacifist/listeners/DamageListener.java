package com.axolr.townypacifist.listeners;

import com.axolr.townypacifist.TownyPacifist;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.PlayerChangePlotEvent;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DamageListener implements Listener {

    private final TownyPacifist plugin;

    /** Players who have already been notified about arena PvP this visit. */
    private final Set<UUID> arenaNotified = new HashSet<>();

    public DamageListener(TownyPacifist plugin) {
        this.plugin = plugin;
    }

    // -------------------------------------------------------------------------
    // Damage handling
    // -------------------------------------------------------------------------

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTownyPlayerDamagePlayer(TownyPlayerDamagePlayerEvent event) {
        if (!plugin.getConfig().getBoolean("enabled", true)) return;

        Town attackerTown = event.getAttackerTown();
        Town victimTown   = event.getVictimTown();

        boolean attackerPacifist = attackerTown != null && attackerTown.isNeutral();
        boolean victimPacifist   = victimTown   != null && victimTown.isNeutral();

        if (!attackerPacifist && !victimPacifist) return;

        // Arena bypass — message is sent on plot enter, not here
        if (plugin.getConfig().getBoolean("allow-pacifist-in-arenas", true)) {
            TownBlock block = event.getTownBlock();
            if (block != null && block.getType().equals(TownBlockType.ARENA)) {
                return; // allow combat, no message spam
            }
        }

        event.setCancelled(true);

        if (attackerPacifist && victimPacifist) {
            event.getAttackingPlayer().sendMessage(plugin.msg("both-pacifist"));
        } else if (victimPacifist) {
            event.getAttackingPlayer().sendMessage(
                plugin.msg("target-is-pacifist", "player", event.getVictimPlayer().getName())
            );
        } else {
            event.getAttackingPlayer().sendMessage(plugin.msg("attacker-is-pacifist"));
        }
    }

    // -------------------------------------------------------------------------
    // Arena enter/leave notification — fires once per visit, no spam
    // -------------------------------------------------------------------------

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangePlot(PlayerChangePlotEvent event) {
        if (!plugin.getConfig().getBoolean("enabled", true)) return;
        if (!plugin.getConfig().getBoolean("allow-pacifist-in-arenas", true)) return;

        // Only care about pacifist players
        Town town = TownyAPI.getInstance().getTown(event.getPlayer());
        if (town == null || !town.isNeutral()) return;

        UUID uuid = event.getPlayer().getUniqueId();
        boolean enteringArena = isArena(event.getTo());
        boolean leavingArena  = isArena(event.getFrom());

        if (enteringArena && !leavingArena) {
            // Just entered an arena — notify once
            if (arenaNotified.add(uuid)) {
                event.getPlayer().sendMessage(plugin.msg("arena-pvp-allowed"));
            }
        } else if (!enteringArena && leavingArena) {
            // Left the arena — reset so they get notified next time they enter
            arenaNotified.remove(uuid);
        }
    }

    private boolean isArena(WorldCoord coord) {
        if (coord == null) return false;
        TownBlock block = coord.getTownBlockOrNull();
        return block != null && block.getType().equals(TownBlockType.ARENA);
    }
}
