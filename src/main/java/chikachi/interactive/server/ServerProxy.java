package chikachi.interactive.server;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.Constants;
import chikachi.interactive.server.message.MessageHandlerInteractiveActionEdit;
import chikachi.interactive.common.CommonProxy;
import chikachi.interactive.common.message.MessageInteractiveActionEdit;
import cpw.mods.fml.relauncher.Side;

public class ServerProxy extends CommonProxy {
    @Override
    public void onPreInit() {
        super.onPreInit();

        ChikachiInteractive.network.registerMessage(MessageHandlerInteractiveActionEdit.class, MessageInteractiveActionEdit.class, Constants.NETWORK_SERVER_EDIT, Side.SERVER);
    }
}
