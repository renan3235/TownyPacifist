package com.axolr.townypacifist;

import com.palmergames.bukkit.config.CommentedConfiguration;
import com.palmergames.util.FileMgmt;

import java.nio.file.Path;

public final class TownyPacifistSettings {

    private static CommentedConfiguration config;

    public static void loadConfig(TownyPacifist plugin) throws Exception {
        Path configPath = plugin.getDataFolder().toPath().resolve("config.yml");

        if (!FileMgmt.checkOrCreateFile(configPath.toString()))
            throw new Exception("Could not create config.yml!");

        config = new CommentedConfiguration(configPath, plugin);
        if (!config.load())
            throw new Exception("Could not load config.yml!");

        setDefaults(configPath, plugin);
        config.save();
    }

    private static void setDefaults(Path configPath, TownyPacifist plugin) {
        CommentedConfiguration newConfig = new CommentedConfiguration(configPath, plugin);
        newConfig.load();

        for (TownyPacifistConfigNodes node : TownyPacifistConfigNodes.values()) {
            if (node.getComments().length > 0)
                newConfig.addComment(node.getRoot(), node.getComments());
            String existing = config.getString(node.getRoot());
            newConfig.set(node.getRoot(), existing != null ? existing : node.getDefault());
        }

        config = newConfig;
    }

    public static boolean isEnabled() {
        return Boolean.parseBoolean(
            config.getString(TownyPacifistConfigNodes.ENABLED.getRoot(), "true"));
    }

    public static boolean allowPacifistInArenas() {
        return Boolean.parseBoolean(
            config.getString(TownyPacifistConfigNodes.ALLOW_PACIFIST_IN_ARENAS.getRoot(), "true"));
    }
}
