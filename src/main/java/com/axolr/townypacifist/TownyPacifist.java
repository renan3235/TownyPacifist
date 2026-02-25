package com.axolr.townypacifist;

import com.axolr.townypacifist.listeners.DamageListener;
import com.axolr.townypacifist.listeners.TownyEventListener;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public final class TownyPacifist extends JavaPlugin {

    private static TownyPacifist instance;
    private static boolean siegeWarEnabled = false;
    private static boolean captureSitesEnabled = false;

    @Override
    public void onEnable() {
        instance = this;

        try {
            TownyPacifistSettings.loadConfig(this);
        } catch (Exception e) {
            getLogger().severe("Failed to load config: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadTranslations();

        siegeWarEnabled = getServer().getPluginManager().getPlugin("SiegeWar") != null;
        if (siegeWarEnabled) getLogger().info("SiegeWar detected — peaceful integration active.");

        captureSitesEnabled = getServer().getPluginManager().getPlugin("TownyCaptureSites") != null;
        if (captureSitesEnabled) getLogger().info("TownyCaptureSites detected — capture site integration active.");

        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new TownyEventListener(), this);

        getLogger().info("TownyPacifist v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TownyPacifist disabled.");
    }

    public static TownyPacifist getPlugin() {
        return instance;
    }

    public static boolean isSiegeWarEnabled() {
        return siegeWarEnabled;
    }

    public static boolean isCaptureSitesEnabled() {
        return captureSitesEnabled;
    }

    public void loadTranslations() {
        Path langPath = getDataFolder().toPath().resolve("lang");
        try {
            TranslationLoader loader = new TranslationLoader(langPath, this, TownyPacifist.class);
            loader.load();
            TownyAPI.getInstance().addTranslations(this, loader.getTranslations());
        } catch (TownyInitException e) {
            getLogger().severe("Failed to load translations: " + e.getMessage());
        }
    }
}
