package chikachi.interactive.common.action;

import chikachi.lib.common.utils.MapUtils;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ActionBasePotion extends ActionBase {
    protected boolean stackable = false;
    protected int duration = 10;
    protected int amplifier = 0;

    protected void setPotionData(MapUtils<String> utils) {
        this.stackable = utils.getBoolean("stackable", false);
        this.duration = utils.getInteger("duration", 10);
        this.amplifier = utils.getInteger("amplifier", 0);
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setBoolean("stackable", this.stackable);
        tagCompound.setInteger("duration", this.duration);
        tagCompound.setInteger("amplifier", this.amplifier);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.stackable = tagCompound.getBoolean("stackable");
        this.duration = tagCompound.getInteger("duration");
        this.amplifier = tagCompound.getInteger("amplifier");
    }
}
