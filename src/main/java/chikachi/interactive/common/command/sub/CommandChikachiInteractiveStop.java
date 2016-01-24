package chikachi.interactive.common.command.sub;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import chikachi.lib.common.utils.PlayerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import pro.beam.interactive.robot.Robot;

import java.util.List;

public class CommandChikachiInteractiveStop extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveStop() {
        super("stop");
    }

    @Override
    public void execute(EntityPlayer player, String[] args) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());
        Robot robot = game.getRobotInstance();
        if (robot != null) {
            if (robot.isConnecting() || robot.isOpen()) {
                try {
                    robot.disconnect();
                    player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.stop.success"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.stop.failed").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                } finally {
                    ChikachiInteractive.instance.robot = null;
                }
            } else {
                player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.stop.notConnected"));
            }
        } else {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.stop.notConnected"));
        }
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
