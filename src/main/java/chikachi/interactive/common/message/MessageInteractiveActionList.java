package chikachi.interactive.common.message;

import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.action.ActionManager;
import chikachi.interactive.common.input.InputBase;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageInteractiveActionList implements IMessage {
    private int count;
    private NBTTagCompound tagCompound;

    @SuppressWarnings("unused")
    public MessageInteractiveActionList() {
    }

    public MessageInteractiveActionList(ActionManager manager) {
        setActionManager(manager);
    }

    public MessageInteractiveActionList setActionManager(ActionManager manager) {
        this.tagCompound = new NBTTagCompound();
        this.count = 0;

        NBTTagList actionList = new NBTTagList();

        Set<Map.Entry<InputBase, ActionBase>> actionEntries = manager.getActions().entrySet();
        for (Map.Entry<InputBase, ActionBase> actionEntry : actionEntries) {
            InputBase input = actionEntry.getKey();
            ActionBase action = actionEntry.getValue();

            NBTTagCompound actionCompound = new NBTTagCompound();

            actionCompound.setTag("input", input.toNBT());
            actionCompound.setTag("action", action.toNBT());

            actionList.appendTag(actionCompound);

            this.count++;
        }

        this.tagCompound.setTag("actions", actionList);

        return this;
    }

    public int getCount() {
        return this.count;
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }

    public Map<InputBase, ActionBase> getActions() {
        Map<InputBase, ActionBase> actions = new HashMap<InputBase, ActionBase>();

        if (this.tagCompound.hasKey("actions")) {
            NBTTagList list = this.tagCompound.getTagList("actions", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < this.count; i++) {
                NBTTagCompound compound = list.getCompoundTagAt(i);

                InputBase input = InputBase.fromNBT((NBTTagCompound) compound.getTag("input"));

                if (input != null) {
                    ActionBase action = ActionBase.instantiateFromNBT((NBTTagCompound) compound.getTag("action"));

                    if (action != null) {
                        actions.put(input, action);
                    }
                }
            }
        }

        return actions;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.count = buf.readInt();

        if (this.count > 0) {
            this.tagCompound = ByteBufUtils.readTag(buf);
        } else {
            this.tagCompound = new NBTTagCompound();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.count);

        if (this.count > 0) {
            ByteBufUtils.writeTag(buf, this.tagCompound);
        }
    }
}
