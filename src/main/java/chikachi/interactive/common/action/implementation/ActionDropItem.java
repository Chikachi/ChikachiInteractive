package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ActionDropItem extends ActionBase {
    private static final Random random = new Random();
    private String type = "HOLDING";

    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (this.type.equalsIgnoreCase("HOLDING")) {
            entityPlayer.dropOneItem(true);
        } else if (this.type.equalsIgnoreCase("ALL")) {
            entityPlayer.inventory.dropAllItems();
        } else if (this.type.equalsIgnoreCase("ARMOR")) {
            for (int i = 0, j = entityPlayer.inventory.armorInventory.length; i < j; i++) {
                if (entityPlayer.inventory.armorInventory[i] != null) {
                    entityPlayer.dropPlayerItemWithRandomChoice(entityPlayer.inventory.armorInventory[i], true);
                    entityPlayer.inventory.armorInventory[i] = null;
                }
            }
        } else if (this.type.equalsIgnoreCase("RANDOM")) {
            List<Integer> inventorySpots = new ArrayList<Integer>();
            List<Integer> armorSpots = new ArrayList<Integer>();

            for (int i = 0, j = entityPlayer.inventory.mainInventory.length; i < j; i++) {
                if (entityPlayer.inventory.mainInventory[i] != null) {
                    inventorySpots.add(i);
                }
            }

            for (int i = 0, j = entityPlayer.inventory.armorInventory.length; i < j; i++) {
                if (entityPlayer.inventory.armorInventory[i] != null) {
                    armorSpots.add(i);
                }
            }

            if (inventorySpots.size() > 0 || armorSpots.size() > 0) {
                boolean useMainInventory = armorSpots.size() == 0 || (inventorySpots.size() > 0 && armorSpots.size() > 0 && random.nextDouble() < 0.8);
                if (useMainInventory) {
                    int index = inventorySpots.get(random.nextInt(inventorySpots.size()));
                    entityPlayer.dropPlayerItemWithRandomChoice(entityPlayer.inventory.mainInventory[index], true);
                    entityPlayer.inventory.mainInventory[index] = null;
                } else {
                    int index = armorSpots.get(random.nextInt(armorSpots.size()));
                    entityPlayer.dropPlayerItemWithRandomChoice(entityPlayer.inventory.armorInventory[index], true);
                    entityPlayer.inventory.armorInventory[index] = null;
                }
            }
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        String type = utils.getString("type", "HOLDING");
        if (type.equalsIgnoreCase("HOLDING")) {
            this.type = "HOLDING";
        } else if (type.equalsIgnoreCase("ALL")) {
            this.type = "ALL";
        } else if (type.equalsIgnoreCase("ARMOR")) {
            this.type = "ARMOR";
        } else if (type.equalsIgnoreCase("RANDOM")) {
            this.type = "RANDOM";
        } else {
            ChikachiInteractive.Log(String.format("Invalid type : %s", type), true);
            return false;
        }

        return true;
    }

    @Override
    public String getGuiText() {
        return "Drop Item (" + this.type + ")";
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setString("type", this.type);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.type = tagCompound.getString("type");
    }
}
