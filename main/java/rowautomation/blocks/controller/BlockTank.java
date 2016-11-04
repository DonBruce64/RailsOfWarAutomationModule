package rowautomation.blocks.controller;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rowautomation.tileentities.TileEntityTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTank extends BlockBase{
	public static IIcon endIcon;
	public static IIcon fillingSidesIcon;
    public static IIcon drainingSidesIcon;
    
	public BlockTank() {
		super();
		this.setBlockName("TankBlock");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		TileEntityTank thisTileEntity = (TileEntityTank) world.getTileEntity(x, y, z);
		if(thisTileEntity.drain){
			thisTileEntity.drain=false;
			world.markBlockForUpdate(x, y, z);
			return true;
		}else{
			thisTileEntity.drain=true;
			world.markBlockForUpdate(x, y, z);
			return true;
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTank();
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
    	endIcon=par1IconRegister.registerIcon("rowam:blockbase");
    	fillingSidesIcon=par1IconRegister.registerIcon("rowam:fillingblock");
    	drainingSidesIcon=par1IconRegister.registerIcon("rowam:drainingblock");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side){
    	if(side==0 || side==1){
    		return this.endIcon;
    	}else{
    		TileEntityTank thisTileEntity = (TileEntityTank) blockAccess.getTileEntity(x, y, z);
    		if(thisTileEntity.drain){
    			return this.drainingSidesIcon;
    		}else{
    			return this.fillingSidesIcon;
    		}
		}
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(side==0 || side==1){
    		return this.endIcon;
    	}else{
    		return this.fillingSidesIcon;
    	}
	}
}
