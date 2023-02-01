package dev.dexuby.easycommand.craft;

import com.sun.istack.internal.NotNull;
import dev.dexuby.easycommand.Sender;
import org.bukkit.entity.Player;

public class CraftSender implements Sender {

    public static CraftSender fromPlayer(@NotNull final Player player) {

        return new CraftSender();

    }

}
