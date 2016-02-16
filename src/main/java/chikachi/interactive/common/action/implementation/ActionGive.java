package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

public class ActionGive extends ActionBase {
    private String itemPath;
    private Item item;
    private String itemName;
    private int amount;
    private int damage;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (this.item == null) return;
        ItemStack itemStack = new ItemStack(this.item, this.amount, this.damage);
        entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.give", this.amount, item.getItemStackDisplayName(itemStack)));
        if (!entityPlayer.inventory.addItemStackToInventory(itemStack)) {
            entityPlayer.entityDropItem(itemStack, 0.5f);
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.itemPath = utils.getString("item");
        this.damage = utils.getInteger("damage", 0);

        if (this.itemPath == null) {
            ChikachiInteractive.Log("Missing item", true);
            return false;
        }

        this.amount = Math.max(1, utils.getInteger("amount", 1));

        Object obj = Item.itemRegistry.getObject(this.itemPath);

        if (obj == null) {
            obj = Block.blockRegistry.getObject(this.itemPath);

            if (obj == null) {
                ChikachiInteractive.Log(String.format("Not an Item or Block : %s", this.itemPath), true);
                return false;
            }

            this.item = Item.getItemFromBlock((Block) obj);
            this.itemName = this.item.getItemStackDisplayName(new ItemStack(this.item, 1, this.damage));
            return true;
        } else {
            this.item = (Item) obj;
            this.itemName = this.item.getItemStackDisplayName(new ItemStack(this.item, 1, this.damage));
            return true;
        }
    }

    @Override
    public String getGuiText() {
        return "Give (" + amount + "x " + this.itemName + EnumChatFormatting.WHITE + ")";
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setString("item", this.itemPath);
        tagCompound.setInteger("damage", this.damage);
        tagCompound.setInteger("amount", this.amount);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.itemPath = tagCompound.getString("item");
        this.damage = tagCompound.getInteger("damage");
        this.amount = tagCompound.getInteger("amount");

        Object obj = Item.itemRegistry.getObject(itemPath);

        if (obj == null) {
            obj = Block.blockRegistry.getObject(itemPath);

            if (obj == null) {
                ChikachiInteractive.Log(String.format("Not an Item or Block : %s", itemPath), true);
                return;
            }

            this.item = Item.getItemFromBlock((Block) obj);
            this.itemName = this.item.getItemStackDisplayName(new ItemStack(this.item, 1, this.damage));
        } else {
            this.item = (Item) obj;
            this.itemName = this.item.getItemStackDisplayName(new ItemStack(this.item, 1, this.damage));
        }
    }
}
