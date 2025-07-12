package me.irobsch.veluxcore;

import me.irobsch.veluxcore.module.ModuleManager;
import me.irobsch.veluxcore.util.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class VeluxCore extends JavaPlugin {
    private FileManager fileManager;
    private ModuleManager moduleManager;
    private FileConfiguration lang;

    @Override
    public void onEnable() {
        fileManager = new FileManager(this);
        fileManager.init();

        lang = fileManager.saveDefaultLang();

        moduleManager = new ModuleManager(this, lang, fileManager);
        moduleManager.loadModules();

        // Register the startup message in the console.
        String version = getDescription().getVersion();
        String rawMessage = lang.getString("console.on-enable");
        String message = rawMessage.replace("{version}", version);
        getLogger().info(message);
    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();

        // Register the shutdown message in the console.
        String disableMessage = lang.getString("console.on-disable");
        getLogger().info(disableMessage);
    }
}
