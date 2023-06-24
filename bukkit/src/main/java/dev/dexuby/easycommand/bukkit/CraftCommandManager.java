package dev.dexuby.easycommand.bukkit;

import dev.dexuby.easycommand.common.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;

public class CraftCommandManager implements CommandManager {

    private SimpleCommandMap simpleCommandMap;

    @Override
    public boolean initialize() {

        try {
            final Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            this.simpleCommandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer().getPluginManager());
            return true;
        } catch (final NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
