package me.irobsch.veluxcore.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Responsible for managing plugin files, including configuration and language files.
 * Ensures that required files and folders are created and loaded properly.
 */
public class FileManager {

    private final JavaPlugin plugin;

    /**
     * Constructs a new FileManager for the specified plugin.
     *
     * @param plugin the plugin instance this FileManager belongs to
     */
    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Initialises the file system by saving the default config.yml and creating the modules directory.
     */
    public void init() {
        plugin.saveDefaultConfig();
        createModulesFolder();
    }

    /**
     * Loads the default language file (lang.yml) from the plugin's resources.
     * If the file does not exist on disk, it is copied from the JAR.
     * Also sets fallback values from the embedded lang.yml.
     *
     * @return the loaded FileConfiguration for lang.yml
     */
    public FileConfiguration saveDefaultLang() {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");

        // Ensure the external lang.yml file exists.
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }

        FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);

        // Load defaults from the embedded lang.yml in the plugin JAR.
        InputStream defaultLangStream = plugin.getResource("lang.yml");
        if (defaultLangStream != null) {
            YamlConfiguration defaultLang = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(defaultLangStream));
            // Set fallback values in case keys are missing in lang.yml.
            lang.setDefaults(defaultLang);
        }

        return lang;
    }

    /**
     * Creates the modules/ directory inside the plugin's data folder if it does not already exist.
     * This folder is intended to store configuration files for plugin modules.
     */
    private void createModulesFolder() {
        File modulesFolder = new File(plugin.getDataFolder(), "modules");
        if (!modulesFolder.exists()) {
            if (!modulesFolder.mkdirs()) {
                plugin.getLogger().severe("Failed to create modules folder.");
            }
        }
    }

    /**
     * Loads a module configuration file from the modules directory.
     * If the file does not exist, it is copied from the plugin's resources.
     * Also sets fallback values from the embedded module file in the plugin JAR.
     *
     * @param name the name of the module (without .yml extension)
     * @return the loaded FileConfiguration for the specified module
     */
    public FileConfiguration loadModuleConfig(String name) {
        String fileName = name.toLowerCase() + ".yml";
        File moduleFile = new File(plugin.getDataFolder() + "/modules", fileName);

        // Ensure the module file exists, if not, copy it from the resources.
        if (!moduleFile.exists()) {
            plugin.saveResource("modules/" + fileName, false);
        }

        // Load the module configuration file.
        FileConfiguration moduleConfig = YamlConfiguration.loadConfiguration(moduleFile);
        // Load defaults from the embedded module file in the plugin JAR.
        InputStream defaultModuleStream = plugin.getResource("modules/" + fileName);
        if (defaultModuleStream != null) {
            YamlConfiguration defaultModule = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(defaultModuleStream));
            // Set fallback values in case keys are missing in the module config.
            moduleConfig.setDefaults(defaultModule);
        }

        return moduleConfig;
    }
}
