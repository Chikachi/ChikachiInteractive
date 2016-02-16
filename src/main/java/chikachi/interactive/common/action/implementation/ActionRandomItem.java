package chikachi.interactive.common.action.implementation;

import chikachi.interactive.common.Blacklist;
import chikachi.interactive.common.action.ActionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;

import java.util.*;

public class ActionRandomItem extends ActionBase {
    private Random random = new Random();

    @Override
    public void execute(EntityPlayer entityPlayer) {
        List<Object> items = new ArrayList<Object>();

        Set names = Item.itemRegistry.getKeys();
        for (Object itemName : names) {
            if (itemName instanceof String && !Blacklist.isBlacklisted((String) itemName)) {
                Item i = (Item) Item.itemRegistry.getObject(itemName);

                List subTypes = new ArrayList();
                i.getSubItems(i, null, subTypes);
                for (Object subType : subTypes) {
                    if (subType instanceof ItemStack) {
                        int metadata = ((ItemStack) subType).getItemDamage();
                        if (!Blacklist.isBlacklisted((String) itemName, metadata)) {
                            int chanceModifier = Blacklist.getChanceModifier((String) itemName, metadata);

                            if (chanceModifier < 100 && chanceModifier < random.nextInt(100) + 1) {
                                continue;
                            }

                            items.add(subType);
                        }
                    }
                }
            }
        }

        int tries = 0;
        ItemStack itemStack = null;

        while (itemStack == null) {
            Object itemObj = items.get(random.nextInt(items.size()));
            if (itemObj instanceof ItemStack) {
                itemStack = (ItemStack) itemObj;
            } else {
                if (tries++ > 10) {
                    entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.random.failed"));
                    return;
                }
            }
        }

        if (entityPlayer.inventory.addItemStackToInventory(itemStack)) {
            entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.random", 1, itemStack.getItem().getItemStackDisplayName(itemStack)));
        } else if (entityPlayer.entityDropItem(itemStack, 0.5f) != null) {
            entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.random", 1, itemStack.getItem().getItemStackDisplayName(itemStack)));
        } else {
            entityPlayer.addChatMessage(new ChatComponentTranslation("chat.ChikachiInteractive.random.failed"));
        }
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        return true;
    }

    @Override
    public String getGuiText() {
        return "Give Random Item";
    }
}
