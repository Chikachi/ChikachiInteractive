package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.HashMap;

public class ActionTest extends ActionBase {
    @Override
    public void execute(EntityPlayer entityPlayer) {
        entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.test"));
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        return true;
    }

    @Override
    public String getGuiText() {
        return "Test";
    }
}