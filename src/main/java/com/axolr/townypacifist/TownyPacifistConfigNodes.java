package com.axolr.townypacifist;

public enum TownyPacifistConfigNodes {

    ENABLED(
        "enabled",
        "true",
        "# Enable or disable all TownyPacifist protection.",
        "# Set to false to temporarily disable the plugin without removing it."),

    ALLOW_PACIFIST_IN_ARENAS(
        "allow-pacifist-in-arenas",
        "true",
        "",
        "# Allow peaceful town members to PvP inside Towny arena plots.",
        "# Players are notified once when they enter an arena."),

    ALLOW_PACIFIST_IN_CAPTURE_SITES(
        "allow-pacifist-in-capture-sites",
        "true",
        "",
        "# Allow peaceful town members to fight inside TownyCaptureSites plots.",
        "# Players are notified once when they enter a capture site.",
        "# Requires TownyCaptureSites to be installed.");

    private final String root;
    private final String def;
    private final String[] comments;

    TownyPacifistConfigNodes(String root, String def, String... comments) {
        this.root = root;
        this.def = def;
        this.comments = comments;
    }

    public String getRoot()     { return root; }
    public String getDefault()  { return def; }
    public String[] getComments() {
        return comments != null ? comments : new String[]{""};
    }
}
