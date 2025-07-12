package me.irobsch.veluxcore.module;

import me.irobsch.veluxcore.module.impl.ScoreboardModule;
import me.irobsch.veluxcore.util.FileManager;
import me.irobsch.veluxcore.util.MessageHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles registration, loading, and unloading of all core modules used by the plugin.
 * <p>
 * This class ensures that each module follows a consistent lifecycle.
 */
public class ModuleManager {

    private final JavaPlugin plugin;
    private final FileConfiguration lang;
    private final FileManager fileManager;
    private final List<CoreModule> modules = new ArrayList<>();

    /**
     * Constructs a new ModuleManager for the given plugin instance.
     *
     * @param plugin the main plugin instance
     * @param lang   the language configuration file
     */
    public ModuleManager(JavaPlugin plugin, FileConfiguration lang, FileManager fileManager) {
        this.plugin = plugin;
        this.lang = lang;
        this.fileManager = fileManager;
    }

    /**
     * Discovers and loads all available modules whose configuration specifies {@code enabled: true}.
     * <p>
     * This method initialises all known modules, loads their configuration from the appropriate
     * module-specific file, and invokes their {@code load()} method if enabled.
     * <p>
     * Enabled modules are stored and later unloaded during plugin shutdown.
     */
    public void loadModules() {
        List<CoreModule> availableModules = List.of(
                new ScoreboardModule()
        );

        for (CoreModule module : availableModules) {
            FileConfiguration config = fileManager.loadModuleConfig(module.getName());

            if (config.getBoolean("enabled")) {
                module.load(config);
                modules.add(module);
                Map<String, String> placeholders = Map.of("{module}", module.getName());
                MessageHelper.logFromLang(plugin, lang, "console.load-module", placeholders);
            }
        }
    }

    /**
     * Unloads all registered modules.
     * <p>
     * Each module's {@code unload()} method is called in the order they were registered.
     */
    public void unloadModules() {
        for (CoreModule module : modules) {
            module.unload();
            Map<String, String> placeholders = Map.of("{module}", module.getName());
            MessageHelper.logFromLang(plugin, lang, "console.unload-module", placeholders);
        }
        modules.clear();
    }

    /**
     * Returns the list of all registered modules.
     *
     * @return a list of CoreModule instances currently managed
     */
    public List<CoreModule> getModules() {
        return modules;
    }
}