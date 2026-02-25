package com.axolr.townypacifist.listeners;

import com.axolr.townypacifist.TownyPacifistSettings;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.PlayerChangePlotEvent;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DamageListener implements Listener {

    /** Players who have already been notified about arena PvP this visit. */
    private final Set<UUID> arenaNotified = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTownyPlayerDamagePlayer(TownyPlayerDamagePlayerEvent event) {
        if (!TownyPacifistSettings.isEnabled()) return;

        Town attackerTown = event.getAttackerTown();
        Town victimTown   = event.getVictimTown();

        boolean attackerPacifist = attackerTown != null && attackerTown.isNeutral();
        boolean victimPacifist   = victimTown   != null && victimTown.isNeutral();

        if (!attackerPacifist && !victimPacifist) return;

        // Arena bypass — message is sent on plot enter, not here
        if (TownyPacifistSettings.allowPacifistInArenas()) {
            TownBlock block = event.getTownBlock();
            if (block != null && block.getType().equals(TownBlockType.ARENA)) return;
        }

        event.setCancelled(true);

        Player attacker = event.getAttackingPlayer();
        Resident attackerResident = TownyAPI.getInstance().getResident(attacker);

        if (attackerPacifist && victimPacifist) {
            attacker.sendMessage(msg("townypacifist.both-pacifist", attackerResident));
        } else if (victimPacifist) {
            attacker.sendMessage(msg("townypacifist.target-is-pacifist", attackerResident,
                event.getVictimPlayer().getName()));
        } else {
            attacker.sendMessage(msg("townypacifist.attacker-is-pacifist", attackerResident));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangePlot(PlayerChangePlotEvent event) {
        if (!TownyPacifistSettings.isEnabled()) return;
        if (!TownyPacifistSettings.allowPacifistInArenas()) return;

        Town town = TownyAPI.getInstance().getTown(event.getPlayer());
        if (town == null || !town.isNeutral()) return;

        UUID uuid = event.getPlayer().getUniqueId();
        boolean enteringArena = isArena(event.getTo());
        boolean leavingArena  = isArena(event.getFrom());

        if (enteringArena && !leavingArena) {
            if (arenaNotified.add(uuid)) {
                Resident resident = TownyAPI.getInstance().getResident(event.getPlayer());
                event.getPlayer().sendMessage(msg("townypacifist.arena-pvp-allowed", resident));
            }
        } else if (!enteringArena && leavingArena) {
            arenaNotified.remove(uuid);
        }
    }

    private boolean isArena(WorldCoord coord) {
        if (coord == null) return false;
        TownBlock block = coord.getTownBlockOrNull();
        return block != null && block.getType().equals(TownBlockType.ARENA);
    }

    private static String msg(String key, Resident resident, Object... args) {
        Translatable t = Translatable.of(key, args);
        return resident != null ? t.forLocale(resident) : t.defaultLocale();
    }
}
