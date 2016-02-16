package chikachi.interactive.server.message;

import chikachi.interactive.common.Game;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.message.MessageInteractiveActionEdit;
import chikachi.interactive.common.tetris.TetrisForgeConnector;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;

@SideOnly(Side.SERVER)
public class MessageHandlerInteractiveActionEdit implements IMessageHandler<MessageInteractiveActionEdit, IMessage> {
    @Override
    public IMessage onMessage(MessageInteractiveActionEdit message, MessageContext ctx) {
        MessageInteractiveActionEdit.EditType type = message.getEditType();
        Game game = TetrisForgeConnector.getInstance().getGame(message.getPlayerGameProfile().getName());

        if (game == null) {
            return null;
        }

        switch (type) {
            case NEW:
                NBTTagCompound inputData = message.getInputData();
                InputBase input = InputBase.instantiateFromNBT(inputData);

                if (input != null) {
                    NBTTagCompound actionData = message.getActionData();
                    ActionBase action = ActionBase.instantiateFromNBT(actionData);

                    if (action != null) {
                        game.getManager().addAction(input, action);
                    }
                }
                break;
            case EDIT:
                break;
            case DELETE:
                break;
        }

        return null;
    }
}
