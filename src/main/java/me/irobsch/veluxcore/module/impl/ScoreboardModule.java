package me.irobsch.veluxcore.module.impl;

import me.irobsch.veluxcore.module.CoreModule;
import org.bukkit.configuration.file.FileConfiguration;

public class ScoreboardModule implements CoreModule {

    private FileConfiguration config;

    @Override
    public void load(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void unload() {

    }

    @Override
    public String getName() {
        return "Scoreboard";
    }
}
