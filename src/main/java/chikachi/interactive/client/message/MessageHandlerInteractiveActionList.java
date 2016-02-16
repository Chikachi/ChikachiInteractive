package chikachi.interactive.client.message;

import chikachi.interactive.client.gui.GuiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.input.InputBase;
import chikachi.interactive.common.message.MessageInteractiveActionList;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class MessageHandlerInteractiveActionList implements IMessageHandler<MessageInteractiveActionList, IMessage> {
    @Override
    public IMessage onMessage(MessageInteractiveActionList message, MessageContext ctx) {
        Map<Integer, Tuple> inputMap = new HashMap<Integer, Tuple>();
        Map<InputBase, ActionBase> actions = message.getActions();

        Set<Map.Entry<InputBase, ActionBase>> actionEntries = actions.entrySet();
        for (Map.Entry<InputBase, ActionBase> actionEntry : actionEntries) {
            int inputId = actionEntry.getKey().getId();

            inputMap.put(inputId, new Tuple(actionEntry.getKey(), actionEntry.getValue()));
        }

        GuiInteractive.inputs = inputMap;
        return null;
    }
}
