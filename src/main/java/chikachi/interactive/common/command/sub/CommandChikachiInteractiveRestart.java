package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.ActionDispatchEventListener;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;

import java.util.List;

public class CommandChikachiInteractiveRestart extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveRestart() {
        super("restart");
    }

    @Override
    public void execute(EntityPlayerMP player, String[] args) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());
        Robot robot = game.getRobotInstance();

        if (robot != null) {
            if (robot.isConnecting() || robot.isOpen()) {
                try {
                    // Disconnect to try reconnecting, in case something is hanging
                    robot.disconnect();
                } catch (InterruptedException ignored) {
                }
            }
        }

        String twoFactor = args.length > 0 ? args[0] : null;

        robot = game.getRobot(true, twoFactor);
        if (robot == null) {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.restart.failed"));
            return;
        }

        robot.on(Protocol.Report.class, new ActionDispatchEventListener(game.getManager()));

        player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.restart.success"));
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
