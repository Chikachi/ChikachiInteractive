package chikachi.interactive.common;

import chikachi.interactive.ChikachiInteractive;
import cpw.mods.fml.common.network.NetworkRegistry;

public abstract class CommonProxy {
    public void onPreInit() {
        ChikachiInteractive.network = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
    }

    public void onInit() {
    }

    public void onPostInit() {
    }
}
