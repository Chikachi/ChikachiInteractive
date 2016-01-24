package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import chikachi.lib.common.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;

// FIXME: Fix this!
public class ActionTeleportDimension extends ActionBase {
    private int dimension = 0;
    private boolean specificPosition = false;
    private int x;
    private int y;
    private int z;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (entityPlayer instanceof EntityPlayerMP) {
            if (this.specificPosition) {
                PlayerUtils.TeleportPlayerToDimensionAndPosition(this.dimension, (EntityPlayerMP) entityPlayer, this.x, this.y, this.z);
            } else {
                PlayerUtils.TeleportPlayerToDimensionSpawn(this.dimension, (EntityPlayerMP) entityPlayer);
            }
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.dimension = utils.getInteger("dimension", 0);

        if (utils.hasKey("x", Integer.class) && utils.hasKey("y", Integer.class) && utils.hasKey("z", Integer.class)) {
            this.x = utils.getInteger("x", 0);
            this.y = utils.getInteger("y", 0);
            this.z = utils.getInteger("z", 0);
            this.specificPosition = true;
        }

        if (DimensionManager.getWorld(this.dimension) == null) {
            ChikachiInteractive.Log(String.format("Invalid dimension id : %s", this.dimension), true);
            return false;
        }

        return true;
    }
}
