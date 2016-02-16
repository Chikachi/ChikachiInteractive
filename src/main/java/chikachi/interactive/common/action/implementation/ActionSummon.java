package chikachi.interactive.common.action.implementation;

import chikachi.interactive.ChikachiInteractive;
import chikachi.interactive.common.action.ActionBase;
import chikachi.lib.common.utils.MapUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class ActionSummon extends ActionBase {
    private static final Random random = new Random();
    private String entity;
    private int amount = 1;
    private int radius = 5;

    @Override
    public void execute(EntityPlayer entityPlayer) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", entity);

        World world = entityPlayer.worldObj;
        Vec3 playerPos = Vec3.createVectorHelper(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);

        for (int i = 0; i < this.amount; i++) {
            Entity entity = EntityList.createEntityFromNBT(nbttagcompound, world);
            if (entity != null) {
                Vec3 spawnPos = getRandomPosition(world, playerPos, this.radius);

                if (spawnPos == null) {
                    entity.setDead();
                    break;
                }

                entity.setLocationAndAngles(spawnPos.xCoord, spawnPos.yCoord, spawnPos.zCoord, entity.rotationYaw, entity.rotationPitch);

                world.spawnEntityInWorld(entity);
            }
        }
    }

    private Vec3 getRandomPosition(World world, Vec3 playerPos, int radius) {
        Vec3 pos = Vec3.createVectorHelper(playerPos.xCoord, playerPos.yCoord, playerPos.zCoord);

        double addX = (random.nextDouble() - 0.5) * 2 * radius;
        double addZ = (random.nextDouble() - 0.5) * 2 * radius;

        pos = pos.addVector(addX, 0, addZ);

        Block block = world.getBlock((int) pos.xCoord, (int) pos.yCoord, (int) pos.zCoord);
        if (block.equals(Blocks.air)) {
            if (world.getBlock((int) pos.xCoord, (int) pos.yCoord + 1, (int) pos.zCoord).equals(Blocks.air)) {
                return pos;
            }
        }

        if (radius == 0) {
            return null;
        }

        return getRandomPosition(world, playerPos, radius - 1);
    }

    @Override
    public boolean setData(HashMap<String, Object> data) {
        MapUtils<String> utils = new MapUtils<String>(data);

        this.entity = utils.getString("entity");
        this.amount = utils.getInteger("amount", 1);
        this.radius = utils.getInteger("radius", 5);

        if (this.entity == null) {
            ChikachiInteractive.Log("Missing entity", true);
            return false;
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", this.entity);

        Entity entity = EntityList.createEntityFromNBT(nbttagcompound, MinecraftServer.getServer().getEntityWorld());

        if (entity == null) {
            ChikachiInteractive.Log(String.format("Invalid EntityName : %s", this.entity), true);
            return false;
        } else {
            entity.setDead();
            return true;
        }
    }

    @Override
    public String getGuiText() {
        return "Summon Entity (" + this.amount + "x " + this.entity + " within " + this.radius + "m radius)";
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound tagCompound = super.toNBT();

        tagCompound.setString("entity", this.entity);
        tagCompound.setInteger("entity", this.amount);
        tagCompound.setInteger("entity", this.radius);

        return tagCompound;
    }

    @Override
    protected void fromNBT(NBTTagCompound tagCompound) {
        super.fromNBT(tagCompound);

        this.entity = tagCompound.getString("entity");
        this.amount = tagCompound.getInteger("amount");
        this.radius = tagCompound.getInteger("radius");
    }
}
