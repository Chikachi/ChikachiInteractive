package chikachi.interactive.client;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.client.event.EventTooltip;
import chikachi.interactive.client.message.MessageHandlerInteractiveActionEdit;
import chikachi.interactive.client.message.MessageHandlerInteractiveActionList;
import chikachi.interactive.common.CommonProxy;
import chikachi.interactive.common.Constants;
import chikachi.interactive.common.message.MessageInteractiveActionEdit;
import chikachi.interactive.common.message.MessageInteractiveActionList;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void onPreInit() {
        super.onPreInit();

        ChikachiInteractive.network.registerMessage(MessageHandlerInteractiveActionList.class, MessageInteractiveActionList.class, Constants.NETWORK_CLIENT_LIST, Side.CLIENT);
        ChikachiInteractive.network.registerMessage(MessageHandlerInteractiveActionEdit.class, MessageInteractiveActionEdit.class, Constants.NETWORK_CLIENT_EDIT, Side.CLIENT);

        MinecraftForge.EVENT_BUS.register(new EventTooltip());
    }
}
