package chikachi.interactive.client.event;

import chikachi.interactive.common.Blacklist;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EventTooltip {
    private final static boolean showClass = false;

    private String NBTToString(NBTBase nbtBase) {
        int tagType = nbtBase.getId();

        switch (tagType) {
            case net.minecraftforge.common.util.Constants.NBT.TAG_BYTE:
                return String.format("%s%s (byte)%s", EnumChatFormatting.WHITE, ((NBTTagByte) nbtBase).func_150287_d(), EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_SHORT:
                return String.format("%s%s (short)%s", EnumChatFormatting.WHITE, ((NBTTagShort) nbtBase).func_150287_d(), EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_LONG:
                return String.format("%s%s (long)%s", EnumChatFormatting.WHITE, ((NBTTagLong) nbtBase).func_150291_c(), EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_FLOAT:
                return String.format("%s%s (float)%s", EnumChatFormatting.WHITE, ((NBTTagFloat) nbtBase).func_150288_h(), EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_DOUBLE:
                return String.format("%s%s (double)%s", EnumChatFormatting.WHITE, ((NBTTagDouble) nbtBase).func_150286_g(), EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_BYTE_ARRAY:
                String byteString = "";
                byte[] byteArray = ((NBTTagByteArray) nbtBase).func_150292_c();
                for (byte byteVal : byteArray) {
                    if (byteString.length() > 0) {
                        byteString += ",";
                    }
                    byteString += byteVal;
                }
                return String.format("%sbyte[%s]%s", EnumChatFormatting.WHITE, byteString, EnumChatFormatting.GRAY);
            case net.minecraftforge.common.util.Constants.NBT.TAG_LIST:
                NBTTagList tagList = ((NBTTagList) nbtBase);

                return String.format("%s%s%s", EnumChatFormatting.WHITE, tagList.toString(), EnumChatFormatting.GRAY);
            default:
                return EnumChatFormatting.WHITE + nbtBase.toString() + EnumChatFormatting.GRAY;
        }
    }

    private void NBTToTooltip(List<String> tooltip, String prefix, NBTTagCompound tagCompound) {
        Set keys = tagCompound.func_150296_c();
        for (Object keyObj : keys) {
            String key = (String) keyObj;
            NBTBase value = tagCompound.getTag(key);
            if (value instanceof NBTTagCompound) {
                tooltip.add(String.format("%s%s : {", prefix, key));
                NBTToTooltip(tooltip, " " + prefix, (NBTTagCompound) value);
                tooltip.add(String.format("%s}", prefix));
            } else if (value instanceof NBTTagList) {
                tooltip.add(String.format("%s%s : [", prefix, key));
                NBTTagList tagList = ((NBTTagList) value);

                int tagType = tagList.func_150303_d();

                for (int i = 0, j = tagList.tagCount(); i < j; i++) {
                    switch (tagType) {
                        case net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND:
                            tooltip.add(String.format("%s {", prefix));
                            NBTToTooltip(tooltip, "  " + prefix, tagList.getCompoundTagAt(i));
                            if (i == j - 1) {
                                tooltip.add(String.format("%s }", prefix));
                            } else {
                                tooltip.add(String.format("%s },", prefix));
                            }
                            break;
                        default:
                            tooltip.add(String.format("%s - %s (%s)", prefix, "Unknown tag type", tagType));
                            break;
                    }
                }

                tooltip.add(String.format("%s]", prefix));
            } else {
                if (showClass) {
                    tooltip.add(String.format("%s%s : %s %s", prefix, key, NBTToString(value), value.getClass().getName()));
                } else {
                    tooltip.add(String.format("%s%s : %s", prefix, key, NBTToString(value)));
                }
            }
        }

    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            if (event.itemStack.getItem() != null) {
                String status = EnumChatFormatting.GREEN + "WHITELISTED";

                String itemName = Item.itemRegistry.getNameForObject(event.itemStack.getItem());
                if (itemName != null) {
                    int chance = Blacklist.getChanceModifier(itemName, event.itemStack.getItemDamage());
                    if (Blacklist.isBlacklisted(itemName, event.itemStack.getItemDamage())) {
                        status = EnumChatFormatting.DARK_RED + "BLACKLISTED";
                    } else if (chance < 100) {
                        status = EnumChatFormatting.YELLOW + Integer.toString(chance) + "% CHANCE";
                    }
                }

                Collections.addAll(
                        event.toolTip,
                        "",
                        EnumChatFormatting.BOLD + "Chikachi Interactive",
                        "- Random Item Status: " + status
                );
            }
        }
    }
}
