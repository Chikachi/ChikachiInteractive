package chikachi.interactive.common.command.sub;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import chikachi.lib.common.command.sub.CommandChikachiBasePlayer;
import chikachi.lib.common.utils.IntegerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class CommandChikachiInteractiveTest extends CommandChikachiBasePlayer {
    public CommandChikachiInteractiveTest() {
        super("test");
    }

    @Override
    public void execute(EntityPlayerMP player, String[] args) {
        if (args.length == 0) {
            player.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.test.usage"));
        } else {
            int times = 1;
            if (args.length > 1) {
                times = IntegerUtils.parseInteger(args[1], 1, 1);
            }
            TetrisForgeConnector tetrisForgeConnector = TetrisForgeConnector.getInstance();
            Game game = tetrisForgeConnector.getGame(player.getDisplayName());
            if (game != null) {
                ActionManager manager = game.getManager();

                while (times > 0) {
                    manager.dispatch(IntegerUtils.parseInteger(args[0], 0));
                    times--;
                }
            }
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
