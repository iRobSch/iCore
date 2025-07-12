package me.irobsch.veluxcore.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * A utility class for safely logging messages defined in lang.yml.
 * <p>
 * This helper supports optional placeholder replacement and skips logging
 * entirely if the message is missing or explicitly disabled (i.e. set to an empty string).
 */
public class MessageHelper {

    /**
     * Logs a message from the provided lang.yml configuration using the given path and placeholder map.
     * <p>
     * If the message is null or blank, no log entry is made.
     *
     * @param plugin       the JavaPlugin instance used to access the logger
     * @param lang         the language configuration file
     * @param path         the path to the message in the language config (e.g. "console.load-module")
     * @param placeholders a map of placeholder keys and values to replace in the message (e.g. {module} → "Scoreboard")
     */
    public static void logFromLang(JavaPlugin plugin, FileConfiguration lang, String path, Map<String,
            String> placeholders) {
        String raw = lang.getString(path);

        if (raw != null && !raw.isBlank()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                raw = raw.replace(entry.getKey(), entry.getValue());
            }
            plugin.getLogger().info(raw);
        }
    }

    /**
     * Logs a message from lang.yml without any placeholder replacement.
     * <p>
     * If the message is null or blank, no log entry is made.
     *
     * @param plugin the JavaPlugin instance used to access the logger
     * @param lang   the language configuration file
     * @param path   the path to the message in the language config (e.g. "console.on-disable")
     */
    public static void logFromLang(JavaPlugin plugin, FileConfiguration lang, String path) {
        logFromLang(plugin, lang, path, Map.of());
    }
}