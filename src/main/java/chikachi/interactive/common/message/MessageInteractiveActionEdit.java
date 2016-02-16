package chikachi.interactive.common.message;

import chikachi.interactive.common.action.ActionBase;
import chikachi.interactive.common.input.InputBase;
import chikachi.lib.common.utils.GameProfileUtils;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class MessageInteractiveActionEdit implements IMessage {
    private UUID playerUuid;
    private EditType editType = EditType.NEW;
    private NBTTagCompound inputData;
    private NBTTagCompound actionData;

    public MessageInteractiveActionEdit setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
        return this;
    }

    public MessageInteractiveActionEdit setEditType(EditType editType) {
        this.editType = editType;
        return this;
    }

    public MessageInteractiveActionEdit setInput(InputBase input) {
        this.inputData = input.toNBT();
        return this;
    }

    public MessageInteractiveActionEdit setAction(ActionBase action) {
        this.actionData = action.toNBT();
        return this;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public GameProfile getPlayerGameProfile() {
        return GameProfileUtils.UUIDToGameProfile(this.playerUuid);
    }

    public EntityPlayer getPlayer() {
        return GameProfileUtils.GetPlayerFromUUID(this.playerUuid);
    }

    public EditType getEditType() {
        return this.editType;
    }

    public NBTTagCompound getInputData() {
        return this.inputData;
    }

    public NBTTagCompound getActionData() {
        return this.actionData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.editType = EditType.fromValue(buf.readInt());
        this.inputData = ByteBufUtils.readTag(buf);
        this.actionData = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.editType.getValue());
        ByteBufUtils.writeTag(buf, this.inputData);
        ByteBufUtils.writeTag(buf, this.actionData);
    }

    public enum EditType {
        ERROR(-1),
        NEW(0),
        EDIT(1),
        DELETE(2);

        private final int value;

        EditType(int value) {
            this.value = value;
        }

        public static EditType fromValue(int value) {
            switch (value) {
                case 0:
                    return NEW;
                case 1:
                    return EDIT;
                case 2:
                    return DELETE;
                default:
                    return ERROR;
            }
        }

        public int getValue() {
            return this.value;
        }
    }
}
