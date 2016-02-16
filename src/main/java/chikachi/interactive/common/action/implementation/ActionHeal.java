package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

    @Override
    public String getGuiText() {
        return "Heal " + (this.includeSaturation ? " and Saturate" : "");
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setBoolean("saturation", this.includeSaturation);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.includeSaturation = tagCompound.getBoolean("saturation");
    }
}
