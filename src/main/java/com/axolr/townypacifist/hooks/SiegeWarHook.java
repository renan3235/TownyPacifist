package com.axolr.townypacifist.hooks;

import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.gmail.goosius.siegewar.utils.SiegeWarTownPeacefulnessUtil;
import com.palmergames.bukkit.towny.object.Town;

/**
 * Wrapper for SiegeWar's peaceful town system.
 * This class is only loaded at runtime when SiegeWar is present,
 * preventing ClassNotFoundException on servers without SiegeWar.
 */
public class SiegeWarHook {

    public static boolean isTownPeaceful(Town town) {
        return SiegeWarSettings.getWarCommonPeacefulTownsEnabled()
            && SiegeWarTownPeacefulnessUtil.isTownPeaceful(town);
    }
}
