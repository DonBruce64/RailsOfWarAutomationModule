package rowautomation.blocks.controller;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rowautomation.tileentities.TileEntityFreight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFreight extends BlockBase{
	public static IIcon endIcon;
	public static IIcon loadingSidesIcon;
    public static IIcon unloadingSidesIcon;
    
	public BlockFreight() {
		super();
		this.setBlockName("FreightBlock");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		TileEntityFreight thisTileEntity = (TileEntityFreight) world.getTileEntity(x, y, z);
		if(thisTileEntity.load){
			thisTileEntity.load=false;
			world.markBlockForUpdate(x, y, z);
			return true;
		}else{
			thisTileEntity.load=true;
			world.markBlockForUpdate(x, y, z);
			return true;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityFreight();
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
    	endIcon=par1IconRegister.registerIcon("rowam:blockbase");
    	loadingSidesIcon=par1IconRegister.registerIcon("rowam:loadingblock");
    	unloadingSidesIcon=par1IconRegister.registerIcon("rowam:unloadingblock");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side){
    	if(side==0 || side==1){
    		return this.endIcon;
    	}else{
    		TileEntityFreight thisTileEntity = (TileEntityFreight) blockAccess.getTileEntity(x, y, z);
    		if(thisTileEntity.load){
    			return this.loadingSidesIcon;
    		}else{
    			return this.unloadingSidesIcon;
    		}
		}
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(side==0 || side==1){
    		return this.endIcon;
    	}else{
    		return this.loadingSidesIcon;
    	}
	}
}
