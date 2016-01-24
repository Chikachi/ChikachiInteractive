package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

import java.util.HashMap;

public class ActionHeal extends ActionBase {
    private boolean includeSaturation = false;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        entityPlayer.setHealth(entityPlayer.getMaxHealth());

        if (this.includeSaturation) {
            FoodStats foodStats = entityPlayer.getFoodStats();
            foodStats.setFoodLevel(20);
            foodStats.setFoodSaturationLevel(20);
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.includeSaturation = utils.getBoolean("saturation", false);

        return true;
    }
}
