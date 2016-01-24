package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.util.HashMap;

public class ActionKick extends ActionBase {
    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (entityPlayer instanceof EntityPlayerMP) {
            ((EntityPlayerMP) entityPlayer).playerNetServerHandler.kickPlayerFromServer(StatCollector.translateToLocal("msg.ChikachiInteractive.kick"));
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        return true;
    }
}
