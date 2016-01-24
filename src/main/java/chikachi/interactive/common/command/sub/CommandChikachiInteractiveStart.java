package chikachi.interactive.common.command.sub;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.ActionDispatchEventListener;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;

import java.util.List;

public class CommandChikachiInteractiveStart extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveStart() {
        super("start");
    }

    @Override
    public void execute(EntityPlayer player, String[] args) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());
        Robot robot = game.getRobotInstance();

        if (robot != null) {
            if (robot.isConnecting()) {
                try {
                    // Disconnect to try reconnecting, in case something is hanging
                    robot.disconnect();
                } catch (InterruptedException ignored) {
                }
            } else if (robot.isOpen()) {
                player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.start.alreadyOpen"));
                return;
            }
        }

        robot = game.getRobot(true);
        robot.on(Protocol.Report.class, new ActionDispatchEventListener(ChikachiInteractive.instance.manager));
        player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.start.success"));
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
