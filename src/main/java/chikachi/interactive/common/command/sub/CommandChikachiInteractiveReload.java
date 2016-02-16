package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import chikachi.lib.common.utils.PlayerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandChikachiInteractiveReload extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveReload() {
        super("reload");
    }

    @Override
    public void execute(EntityPlayerMP player, String[] args) {
        if (TetrisForgeConnector.getInstance().loadConfig()) {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.reload.success"));
        } else {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.reload.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(EntityPlayer player) {
        if (PlayerUtils.IsOP(player)) return true;

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
