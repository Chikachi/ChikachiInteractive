package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import java.util.HashMap;

public class ActionGive extends ActionBase {
    private Item item;
    private int amount;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        if (this.item == null) return;
        ItemStack itemStack = new ItemStack(this.item, this.amount);
        entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.give", this.amount, item.getItemStackDisplayName(itemStack)));
        if (!entityPlayer.inventory.addItemStackToInventory(itemStack)) {
            entityPlayer.entityDropItem(itemStack, 0.5f);
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        String itemPath = utils.getString("item");

        if (itemPath == null) {
            ChikachiInteractive.Log("Missing item", true);
            return false;
        }

        this.amount = Math.max(1, utils.getInteger("amount", 1));

        Object obj = Item.itemRegistry.getObject(itemPath);

        if (obj == null) {
            obj = Block.blockRegistry.getObject(itemPath);

            if (obj == null) {
                ChikachiInteractive.Log(String.format("Not an Item or Block : %s", itemPath), true);
                return false;
            }

            this.item = Item.getItemFromBlock((Block) obj);
            return true;
        } else {
            this.item = (Item) obj;
            return true;
        }
    }
}
