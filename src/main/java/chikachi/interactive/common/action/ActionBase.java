package chikachi.interactive.common.action;

import net.minecraft.entity.player.EntityPlayer;

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
        return cooldown == 0;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ActionBase setCooldown(int cooldown) {
        this.cooldown = cooldown;

        return this;
    }
}
