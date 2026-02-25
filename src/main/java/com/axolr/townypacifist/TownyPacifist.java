package com.axolr.townypacifist;

import com.axolr.townypacifist.listeners.DamageListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TownyPacifist extends JavaPlugin {

    private FileConfiguration lang;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadLang();
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getLogger().info("TownyPacifist v" + getDescription().getVersion() + " enabled! (lang: "
                + getConfig().getString("language", "en_US") + ")");
    }

    @Override
    public void onDisable() {
        getLogger().info("TownyPacifist disabled.");
    }

    // -------------------------------------------------------------------------
    // Lang system
    // -------------------------------------------------------------------------

    private void loadLang() {
        // Always save bundled lang files so users can edit them
        saveResource("lang/en_US.yml", false);
        saveResource("lang/pt_BR.yml", false);

        String language = getConfig().getString("language", "en_US");
        File langFile = new File(getDataFolder(), "lang/" + language + ".yml");

        if (!langFile.exists()) {
            getLogger().warning("Language file 'lang/" + language + ".yml' not found! Falling back to en_US.");
            langFile = new File(getDataFolder(), "lang/en_US.yml");
        }

        lang = YamlConfiguration.loadConfiguration(langFile);
        getLogger().info("Loaded language: " + langFile.getName());
    }

    /**
     * Returns a translated, color-formatted message from the active lang file.
     * Placeholders are passed as key-value pairs: msg("key", "player", "Steve")
     */
    public String msg(String key, String... placeholders) {
        String text = lang.getString("messages." + key, "&cMissing message: " + key);
        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            text = text.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
        }
        return text.replace("&", "\u00A7");
    }
}
