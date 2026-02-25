package com.axolr.townypacifist.listeners;

import com.axolr.townypacifist.TownyPacifist;
import com.palmergames.bukkit.towny.event.TranslationLoadEvent;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.nio.file.Path;
import java.util.Map;

/**
 * Re-injects TownyPacifist translations whenever Towny reloads its language system.
 */
public class TownyEventListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onTranslationLoad(TranslationLoadEvent event) {
        TownyPacifist plugin = TownyPacifist.getPlugin();
        Path langPath = plugin.getDataFolder().toPath().resolve("lang");
        try {
            TranslationLoader loader = new TranslationLoader(langPath, plugin, TownyPacifist.class);
            loader.load();
            for (Map.Entry<String, Map<String, String>> lang : loader.getTranslations().entrySet())
                for (Map.Entry<String, String> msg : lang.getValue().entrySet())
                    event.addTranslation(lang.getKey(), msg.getKey(), msg.getValue());
        } catch (TownyInitException e) {
            plugin.getLogger().warning("Failed to reload translations: " + e.getMessage());
        }
    }
}
