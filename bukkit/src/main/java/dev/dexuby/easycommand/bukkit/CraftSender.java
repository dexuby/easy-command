package dev.dexuby.easycommand.bukkit;

import dev.dexuby.easycommand.common.Sender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftSender implements Sender {

    public static CraftSender fromPlayer(@NotNull final Player player) {

        return new CraftSender();

    }

}
