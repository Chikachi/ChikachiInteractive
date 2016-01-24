package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import chikachi.lib.common.utils.IntegerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class CommandChikachiInteractiveTest extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveTest() {
        super("test");
    }

    @Override
    public void execute(EntityPlayer player, String[] args) {
        if (args.length == 0) {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.test.usage"));
        } else {
            TetrisForgeConnector.getInstance().getGame(player.getDisplayName()).getManager().dispatch(IntegerUtils.parseInteger(args[0], 0));
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
