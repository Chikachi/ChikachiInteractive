package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.List;

public class ActionDeleteWorld extends ActionBase {
    @Override
    public void execute(EntityPlayer entityPlayer) {
        MinecraftServer server = MinecraftServer.getServer();
        List playerEntityList = server.getConfigurationManager().playerEntityList;

        for (Object playerEntity : playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) playerEntity;
            player.playerNetServerHandler.kickPlayerFromServer(StatCollector.translateToLocal("msg.ChikachiInteractive.deleteWorld.kick"));
        }

        server.deleteWorldAndStopServer();
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        return true;
    }

    @Override
    public String getGuiText() {
        return "Delete World";
    }
}
