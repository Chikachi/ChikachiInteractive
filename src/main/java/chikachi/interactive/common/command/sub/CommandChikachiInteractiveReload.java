package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandChikachiInteractiveReload extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveReload() {
        super("reload");
    }

    @Override
    public void execute(EntityPlayer player, String[] args) {
        if (TetrisForgeConnector.getInstance().loadConfig()) {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.reload.success"));
        } else {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.reload.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(EntityPlayer player) {
        //return player.getDisplayName().equalsIgnoreCase(Game.minecraftUsername);
        return true;
        //FIXME: Fix it
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
