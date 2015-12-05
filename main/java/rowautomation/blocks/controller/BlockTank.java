package rowautomation.blocks.controller;

import rowautomation.tileentities.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTank extends BlockBase{
	public static IIcon EndIcon;
	public static IIcon FillingSidesIcon;
    public static IIcon DrainingSidesIcon;
    
	public BlockTank() {
		super();
		this.setBlockName("TankBlock");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		TileEntityTank ThisTileEntity=(TileEntityTank) world.getTileEntity(x, y, z);
		if(ThisTileEntity.Drain){
			ThisTileEntity.Drain=false;
			world.markBlockForUpdate(x, y, z);
			return true;
		}else{
			ThisTileEntity.Drain=true;
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
    	EndIcon=par1IconRegister.registerIcon("rowam:blockbase");
    	FillingSidesIcon=par1IconRegister.registerIcon("rowam:fillingblock");
    	DrainingSidesIcon=par1IconRegister.registerIcon("rowam:drainingblock");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side){
    	if(side==0 || side==1){
    		return this.EndIcon;
    	}else{
    		TileEntityTank ThisTileEntity=(TileEntityTank) blockAccess.getTileEntity(x, y, z);
    		if(ThisTileEntity.Drain){
    			return this.DrainingSidesIcon;
    		}else{
    			return this.FillingSidesIcon;
    		}
		}
	}
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
    	if(par1==0 || par1==1){
    		return this.EndIcon;
    	}else{
    		return this.FillingSidesIcon;
    	}
	}
}
