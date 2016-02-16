package chikachi.interactive.client.message;

import chikachi.interactive.common.message.MessageInteractiveActionEdit;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MessageHandlerInteractiveActionEdit implements IMessageHandler<MessageInteractiveActionEdit, IMessage> {
    @Override
    public IMessage onMessage(MessageInteractiveActionEdit message, MessageContext ctx) {
        return null;
    }
}