package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import pro.beam.interactive.robot.Robot;

import java.util.List;

public class CommandChikachiInteractiveStatus extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveStatus() {
        super("status");
    }

    @Override
    public void execute(EntityPlayer player, String[] args) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());
        Robot robot = game.getRobotInstance();

        String statusMsg = "chat.ChikachiInteractive.status.closed";
        if (robot != null) {
            if (robot.isConnecting()) {
                statusMsg = "chat.ChikachiInteractive.status.connecting";
            }
            if (robot.isOpen()) {
                statusMsg = "chat.ChikachiInteractive.status.open";
            }
            if (robot.isClosing()) {
                statusMsg = "chat.ChikachiInteractive.status.closing";
            }
        }

        player.addChatMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("chat.ChikachiInteractive.status"), StatCollector.translateToLocal(statusMsg))));
    }

    @Override
    public boolean canCommandSenderUseCommand(EntityPlayer player) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());
        if (game != null) {
            ActionManager manager = game.getManager();
            return manager != null;
        }
        return false;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {
        return false;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
