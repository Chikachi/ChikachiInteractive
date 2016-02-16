package chikachi.interactive.common.action;

import chikachi.lib.common.utils.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public abstract class ActionBase {
    private ActionManager manager;
    private int cooldown = 0;

    public void setManager(ActionManager manager) {
        this.manager = manager;
    }

    public boolean execute() {
        EntityPlayer entityPlayer = manager.getEntityPlayer();

        if (entityPlayer != null) {
            execute(entityPlayer);
            return true;
        }

        return false;
    }

    public abstract void execute(EntityPlayer entityPlayer);

    public abstract boolean setData(HashMap<String, Object> data);

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ActionBase setCooldown(int cooldown) {
        this.cooldown = cooldown;

        return this;
    }

    public abstract String getGuiText();

    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setString("class", getClass().getName());
        tagCompound.setInteger("cooldown", this.cooldown);

        return tagCompound;
    }

    protected void fromNBT(NBTTagCompound tagCompound) {
    }

    public static ActionBase instantiateFromNBT(NBTTagCompound tagCompound) {
        Class<?> actionClass = ReflectionUtils.GetClass(tagCompound.getString("class"));

        if (actionClass != null) {
            try {
                ActionBase instance = (ActionBase) actionClass.newInstance();
                instance.setCooldown(tagCompound.getInteger("cooldown"));
                instance.fromNBT(tagCompound);
                return instance;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
