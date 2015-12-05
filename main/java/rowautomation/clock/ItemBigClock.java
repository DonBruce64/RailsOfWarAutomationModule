package rowautomation.clock;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBigClock extends Item{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	
	public ItemBigClock(){
		super();
		this.setCreativeTab(ROWAM.tabROWAM);
		this.setUnlocalizedName("BigClock");
	}
	
	 public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int hitSide, float hitX, float hitY, float hitZ){
	        if (hitSide == 0 || hitSide == 1){
	            return false;
	        }else{
	            int direction = Direction.facingToDirection[hitSide];
	            EntityHanging entity = new EntityBigClock(world, x, y, z, direction);
	            if (!player.canPlayerEdit(x, y, z, hitSide, stack)){
	                return false;
	            }else{
	                if (entity != null && entity.onValidSurface()){
	                    if (!world.isRemote){
	                        world.spawnEntityInWorld(entity);
	                    }
	                }
	                
	                return true;
	            }
	        }
	    }
	
	 @Override
	 @SideOnly(Side.CLIENT)
	 public void registerIcons(IIconRegister register){
		 itemIcon = register.registerIcon("rowam:bigclock");
	 }
}