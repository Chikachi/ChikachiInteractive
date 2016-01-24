package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class ActionTime extends ActionBase {
    private boolean addition = false;
    private int time = 1800;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        long time = entityPlayer.getEntityWorld().getWorldTime();

        if (this.addition) {
            time += this.time;
        } else {
            time = this.time;
        }

        entityPlayer.getEntityWorld().setWorldTime(time);
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.addition = utils.getBoolean("addition", false);
        this.time = utils.getInteger("time", 1800);

        return true;
    }
}
