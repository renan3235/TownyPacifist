package com.axolr.townypacifist.hooks;

import io.github.townyadvanced.townycapturesites.CaptureSitesService;
import com.palmergames.bukkit.towny.object.WorldCoord;
import org.bukkit.Location;

/**
 * Wrapper for TownyCaptureSites integration.
 * Only loaded at runtime when TownyCaptureSites is present.
 */
public class CaptureSitesHook {

    public static boolean isCaptureSite(WorldCoord coord) {
        if (coord == null) return false;
        return CaptureSitesService.getInstance().hasCaptureSite(coord);
    }

    public static boolean isCaptureSite(Location location) {
        if (location == null) return false;
        return CaptureSitesService.getInstance().hasCaptureSite(location);
    }
}
