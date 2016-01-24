package chikachi.interactive.common.structure;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class StructureBase {
    private int sizeX, sizeY, sizeZ;

    public StructureBase(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeZ() {
        return this.sizeZ;
    }

    public void build(World world, int centerX, int bottomY, int centerZ) {
        int startX = centerX - (int) Math.ceil(getSizeX() / 2D);
        int startY = Math.max(1, Math.min(255 - getSizeY(), bottomY - 1));
        int startZ = centerZ - (int) Math.ceil(getSizeZ() / 2D);

        for (int x = startX, endX = startX + getSizeX(); x < endX; x++) {
            for (int z = startZ, endZ = startZ + getSizeZ(); z < endZ; z++) {
                for (int y = startY, endY = startY + getSizeY(); y < endY; y++) {
                    // TODO: Find out how to have a blueprint with actual blocks...
                    world.setBlock(x, y, z, Blocks.air);
                }
            }
        }
    }
}