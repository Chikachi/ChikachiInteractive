package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.structure.StructureBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

// TODO: Finish this
public class ActionStructure extends ActionBase {
    private StructureBase blueprint;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        this.blueprint.build(entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        ChikachiInteractive.Log("ActionStructure is not ready yet!", true);
        /*if (structurePath == null) {
            ChikachiInteractive.Log("Missing data", true);
            return false;
        }

        Class structureClass = ReflectionUtils.GetClass(structurePath);
        if (structureClass != null) {
            try {
                this.blueprint = (StructureBase) structureClass.newInstance();
                return true;
            } catch (InstantiationException e) {
                ChikachiInteractive.Log(String.format("Could not instantiate structure %s", structurePath), true);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                ChikachiInteractive.Log(String.format("Could not instantiate structure %s", structurePath), true);
                e.printStackTrace();
            }
        } else {
            ChikachiInteractive.Log(String.format("Structure %s not found", structurePath), true);
        }*/

        return false;
    }

    @Override
    public String getGuiText() {
        return "Structure";
    }
}
