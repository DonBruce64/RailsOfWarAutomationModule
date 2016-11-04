package rowautomation.blocks.controller;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import rowautomation.ROWAM.GUIs;
import rowautomation.tileentities.TileEntityPointer;

public class BlockPointer extends BlockBase{

	public BlockPointer() {
		super();
		this.setBlockName("PointerBlock");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityPointer();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            if(world.getTileEntity(x, y, z) != null){
                player.openGui(ROWAM.instance, GUIs.Pointer.ordinal(), world, x, y, z);
            }
        }
        return true;
	}
}
