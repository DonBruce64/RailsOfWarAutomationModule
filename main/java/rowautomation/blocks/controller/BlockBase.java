package rowautomation.blocks.controller;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import rowautomation.Chunkloader;
import rowautomation.ROWAM;
import rowautomation.tileentities.TileEntityBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBase extends BlockContainer implements ITileEntityProvider{
    public Map<String, IIcon> IconMap = new HashMap<String, IIcon>();
    
	public BlockBase() {
		super(Material.rock);
		this.blockHardness=-1;
		this.setCreativeTab(ROWAM.tabROWAM);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		Chunkloader.addBlockTicket(this, world, x, y, z);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta){
		Chunkloader.removeBlockTicket(this);
	}
	
	@Override
    public boolean hasComparatorInputOverride(){
		return true;
	}
	
	@Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int q){
		TileEntityBase ThisTileEntity=(TileEntityBase) world.getTileEntity(x, y, z);
		int RedstoneStrength=0;
		if(ThisTileEntity.finishedOperation){RedstoneStrength=15;}
		return RedstoneStrength;
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
		return null;
	}
	
    @Override
    public boolean hasTileEntity(int metadata){
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
        IconMap.put("FuelingBlock", par1IconRegister.registerIcon("rowam:fuelingblock"));
        IconMap.put("PointerBlock", par1IconRegister.registerIcon("rowam:pointerblock"));
        IconMap.put("SignalBlock", par1IconRegister.registerIcon("rowam:signalblock"));
        IconMap.put("StationBlock", par1IconRegister.registerIcon("rowam:stationblock"));
        IconMap.put("TankBlock", par1IconRegister.registerIcon("rowam:fillingblock"));
        IconMap.put("DetectorBlock", par1IconRegister.registerIcon("rowam:detectorblock"));
        IconMap.put("EndIcon", par1IconRegister.registerIcon("rowam:blockbase"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2){
    	if(par1==0 || par1==1){
    		return this.IconMap.get("EndIcon");
    	}else{
    		return this.IconMap.get(this.getUnlocalizedName().substring(5));
    	}
	}	
}
