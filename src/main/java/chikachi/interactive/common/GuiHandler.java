package chikachi.interactive.common;

import chikachi.interactive.client.gui.GuiInteractive;
import chikachi.interactive.server.container.ContainerInteractive;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int INTERACTIVE_ID = 0;

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case INTERACTIVE_ID:
                return new GuiInteractive();
            default:
                return null;
        }
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case INTERACTIVE_ID:
                return new ContainerInteractive();
            default:
                return null;
        }
    }
}
