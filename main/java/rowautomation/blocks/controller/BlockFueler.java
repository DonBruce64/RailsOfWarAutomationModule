package rowautomation.blocks.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import rowautomation.tileentities.TileEntityFueler;
import rowautomation.tileentities.TileEntityTank;

public class BlockFueler extends BlockBase{
    
	public BlockFueler() {
		super();
		this.setBlockName("FuelingBlock");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		TileEntityFueler thisTileEntity = (TileEntityFueler) world.getTileEntity(x, y, z);
		int blockMetadata = thisTileEntity.getBlockMetadata();
		if(player.capabilities.isCreativeMode){
			if(blockMetadata==0){
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);
				world.markBlockForUpdate(x, y, z);
				if(world.isRemote){player.addChatMessage(new ChatComponentText("Setting block to creative operation"));}
				return true;
			}else{
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				world.markBlockForUpdate(x, y, z);
				if(world.isRemote){player.addChatMessage(new ChatComponentText("Setting block to regular operation"));}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFueler();
	}
}
