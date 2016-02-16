package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S29PacketSoundEffect;

import java.util.HashMap;

public class ActionPlaySound extends ActionBase {
    private String sound;
    private float volume = 1;
    private float pitch = 1;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (entityPlayer instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityPlayer;
            entityPlayerMP.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(this.sound, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, this.volume, this.pitch));
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.sound = utils.getString("sound");
        this.volume = (float) Math.max(0, Math.min(10, utils.getDouble("volume", 1)));
        this.pitch = (float) Math.max(0, Math.min(2, utils.getDouble("pitch", 1)));

        if (this.sound == null) {
            ChikachiInteractive.Log("Missing sound", true);
            return false;
        }

        return true;
    }

    @Override
    public String getGuiText() {
        return "Play Sound (" + this.sound + " - " + (this.volume * 100) + "% volume - " + (this.pitch * 100) + " pitch)";
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setString("sound", this.sound);
        tagCompound.setFloat("volume", this.volume);
        tagCompound.setFloat("pitch", this.pitch);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.sound = tagCompound.getString("sound");
        this.volume = tagCompound.getFloat("volume");
        this.pitch = tagCompound.getFloat("pitch");
    }
}
