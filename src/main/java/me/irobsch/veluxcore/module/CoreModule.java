package me.irobsch.veluxcore.module;

import org.bukkit.configuration.file.FileConfiguration;

public interface CoreModule {
    void load(FileConfiguration config);

    void unload();

    String getName();
}
