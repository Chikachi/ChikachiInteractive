package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBasePotion;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.HashMap;

public class ActionPotion extends ActionBasePotion {
    private int potionId;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        int duration = this.duration * 20;
        int amplifier = this.amplifier;
        if (this.stackable) {
            PotionEffect currentEffect = entityPlayer.getActivePotionEffect(Potion.potionTypes[this.potionId]);
            if (currentEffect != null) {
                duration += currentEffect.getDuration();
                amplifier += currentEffect.getAmplifier();
            }
        }
        entityPlayer.addPotionEffect(new PotionEffect(this.potionId, duration, amplifier));
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.potionId = utils.getInteger("id", 0);

        this.setPotionData(utils);

        if (this.potionId < 0 || this.potionId >= Potion.potionTypes.length || Potion.potionTypes[this.potionId] == null) {
            ChikachiInteractive.Log("Invalid potion id", true);
            return false;
        }

        return true;
    }
}
