package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBasePotion;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ActionRandomPotion extends ActionBasePotion {
    private final static Random random = new Random();
    private boolean usePositive;
    private boolean useNegative;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        List<Integer> potions = new ArrayList<Integer>();

        for (Potion potion : Potion.potionTypes) {
            if (potion == null) continue;
            if (potion.isBadEffect()) {
                if (this.useNegative) {
                    potions.add(potion.getId());
                }
            } else {
                if (this.usePositive) {
                    potions.add(potion.getId());
                }
            }
        }

        int numPotions = potions.size();

        if (numPotions > 0) {
            entityPlayer.addPotionEffect(new PotionEffect(potions.get(random.nextInt(numPotions)), this.duration * 20, this.amplifier));
        } else {
            ChikachiInteractive.Log("Couldn't find any potions", true);
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.usePositive = utils.getBoolean("positive", true);
        this.useNegative = utils.getBoolean("negative", true);

        this.setPotionData(utils);

        if (!this.usePositive && !this.useNegative) {
            ChikachiInteractive.Log("Missing potion types", true);
            return false;
        }

        return true;
    }

    @Override
    public String getGuiText() {
        return "Give Random Potion Effect (" + (this.usePositive && this.useNegative ? "All effects" : (this.useNegative ? "Negative effects" : "Positive effects")) + " - " + this.duration + " seconds - " + this.amplifier + " amplifier)";
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setBoolean("positive", this.usePositive);
        tagCompound.setBoolean("negative", this.useNegative);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.usePositive = tagCompound.getBoolean("positive");
        this.useNegative = tagCompound.getBoolean("negative");
    }
}
