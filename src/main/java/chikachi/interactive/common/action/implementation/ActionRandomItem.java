package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.Blacklist;
import chikachi.interactive.common.action.ActionBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import java.util.*;

public class ActionRandomItem extends ActionBase {
    private Random random = new Random();

    @Override
    public void execute(EntityPlayer entityPlayer) {
        List<Object> items = new ArrayList<Object>();

        Set names = Item.itemRegistry.getKeys();
        for (Object itemName : names) {
            if (itemName instanceof String && !Blacklist.isBlacklisted((String)itemName)) {
                Item i = (Item) Item.itemRegistry.getObject(itemName);
                if (i.getHasSubtypes()) {
                    List subTypes = new ArrayList();
                    i.getSubItems(i, null, subTypes);
                    for (Object subType : subTypes) {
                        items.add(subType);
                    }
                } else {
                    items.add(i);
                }
            }
        }
        names = Block.blockRegistry.getKeys();
        for (Object blockName : names) {
            if (blockName instanceof String && !Blacklist.isBlacklisted((String)blockName)) {
                items.add(Block.blockRegistry.getObject(blockName));
            }
        }

        Item item = null;

        while (item == null) {
            Object itemObj = items.get(random.nextInt(items.size()));
            if (itemObj instanceof Item) {
                item = (Item) itemObj;
            } else if (itemObj instanceof Block) {
                item = Item.getItemFromBlock((Block) itemObj);
            }
        }

        ItemStack itemStack = new ItemStack(item);

        entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.random", 1, itemStack.getItem().getItemStackDisplayName(itemStack)));
        if (!entityPlayer.inventory.addItemStackToInventory(itemStack)) {
            entityPlayer.entityDropItem(itemStack, 0.5f);
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        return true;
    }
}
