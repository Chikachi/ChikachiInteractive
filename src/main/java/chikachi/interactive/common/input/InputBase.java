package chikachi.interactive.common.input;

import chikachi.lib.common.utils.ReflectionUtils;
import net.minecraft.nbt.NBTTagCompound;
import pro.beam.interactive.net.packet.Protocol;

import java.lang.reflect.InvocationTargetException;

public abstract class InputBase {
    protected final int id;

    public InputBase(int id) {
        this.id = id;
    }

    /**
     * Used by InputTactile for testing purpose
     *
     * @param id Tactile ID
     * @return Boolean
     */
    public boolean isMet(int id) {
        return false;
    }

    public boolean isMet(Protocol.Report event) {
        return false;
    }

    public int getId() {
        return this.id;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setString("class", this.getClass().getName());
        tagCompound.setInteger("id", this.id);

        return tagCompound;
    }

    public void fromNBT(NBTTagCompound tagCompound) {
    }

    public static InputBase instantiateFromNBT(NBTTagCompound tagCompound) {
        Class<?> inputClass = ReflectionUtils.GetClass(tagCompound.getString("class"));

        if (inputClass != null) {
            try {
                InputBase instance = (InputBase) inputClass.getDeclaredConstructor(new Class[]{int.class}).newInstance(tagCompound.getInteger("id"));
                instance.fromNBT(tagCompound);
                return instance;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
