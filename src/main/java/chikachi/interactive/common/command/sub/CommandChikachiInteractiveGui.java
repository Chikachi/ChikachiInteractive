package chikachi.interactive.common.command.sub;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Game;
import chikachi.interactive.common.GuiHandler;
import chikachi.interactive.common.message.MessageInteractiveActionList;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandChikachiInteractiveGui extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveGui() {
        super("gui");
    }

    @Override
    public void execute(EntityPlayerMP player, String[] args) {
        Game game = TetrisForgeConnector.getInstance().getGame(player.getDisplayName());

        if (game != null) {
            MessageInteractiveActionList message = new MessageInteractiveActionList(game.getManager());
            ChikachiInteractive.network.sendTo(message, player);
            player.openGui(ChikachiInteractive.instance, GuiHandler.INTERACTIVE_ID, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(EntityPlayer player) {
        return player instanceof EntityPlayerMP;
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
