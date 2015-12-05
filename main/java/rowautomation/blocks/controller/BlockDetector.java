package rowautomation.blocks.controller;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import rowautomation.tileentities.TileEntityDetector;

public class BlockDetector extends BlockBase{
	
	public BlockDetector() {
		super();
		this.setBlockName("DetectorBlock");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDetector();
	}
}
