package rowautomation.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGravelWand extends Item{
	public ItemGravelWand(){
		super();
		this.setCreativeTab(ROWAM.tabROWAM);
		this.setUnlocalizedName("GravelWand");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		for(int i=0; i<world.loadedTileEntityList.size(); ++i){
			TileEntity tile = (TileEntity) world.loadedTileEntityList.get(i);
			if(tile.getClass().getName().contains("net.row.tileentity.TileEntity")){
				world.setBlock(tile.xCoord, tile.yCoord - 1 , tile.zCoord, Blocks.gravel);
			}
		}
		return stack;
	}
	
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void registerIcons(IIconRegister register){
		 itemIcon = register.registerIcon("rowam:gravelwand");
	 }
}
