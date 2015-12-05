package rowautomation.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlatform extends Block{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	private int blockMetadata;
	private float xBoundMin;
	private float yBoundMin;
	private float zBoundMin;
	private float xBoundMax;
	private float yBoundMax;
	private float zBoundMax;
	private IIcon blockIcon;
	//bits 1, and 2 represent the rotation angle
	//3 is for lower slabs, 4 is for upper slabs, use neither for full slab
	
	public BlockPlatform() {
		super(Material.rock);
		this.blockHardness=-1;
		this.setCreativeTab(ROWAM.tabROWAM);
		this.setBlockName("PlatformBlock");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float hity, float hitz){
		
		ItemStack currentItem=player.inventory.getCurrentItem();
		if(currentItem!=null){
			blockMetadata=world.getBlockMetadata(x, y, z);
			if(currentItem.isItemEqual(new ItemStack(this)) && hity==0.5F && (blockMetadata & 4)==4){
				world.setBlockMetadataWithNotify(x, y, z, blockMetadata-4, 2);
				if(!player.capabilities.isCreativeMode){
					player.inventory.getCurrentItem().stackSize=player.inventory.getCurrentItem().stackSize-1;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
		blockMetadata=world.getBlockMetadata(x, y, z);
		if((blockMetadata & 3)==0){xBoundMin=0.375F;}else{xBoundMin=0.0F;}////
		if((blockMetadata & 8)==8){yBoundMin=0.5F;}else{yBoundMin=0.0F;}
		if((blockMetadata & 3)==1){zBoundMin=0.375F;}else{zBoundMin=0.0F;}
		if((blockMetadata & 3)==2){xBoundMax=0.625F;}else{xBoundMax=1.0F;}
		if((blockMetadata & 4)==4){yBoundMax=0.5F;}else{yBoundMax=1.0F;}
		if((blockMetadata & 3)==3){zBoundMax=0.625F;}else{zBoundMax=1.0F;}
		this.setBlockBounds(xBoundMin, yBoundMin, zBoundMin, xBoundMax, yBoundMax, zBoundMax);
	}   	

	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int metadata){
		if(side==0){//placed under block
			return 8;
		}
		if(side==1){
			if(world.getBlock(x, y-1, z).equals(this)){//placed on top of another PlatformBlock
				return world.getBlockMetadata(x, y-1, z) | 4;
			}else{
				return 4;//player didn't attach block
			}
		}
		if(hity <= 0.5F){//low block
			if(side==2){return 4 | 1;}
			else if(side==3){return 4 | 3;}
			else if(side==4){return 4 | 0;}
			else{return 4 | 2;}
		}else{//high block
			if(side==2){return 8 | 1;}
			else if(side==3){return 8 | 3;}
			else if(side==4){return 8 | 0;}
			else{return 8 | 2;}
		}
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
    	blockIcon=par1IconRegister.registerIcon("rowam:platformedge");
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2){
    	return blockIcon;
	}	
    
    @Override
    public boolean isOpaqueCube() {
            return false;
    }
    
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @Override
    public int getRenderType() {
            return renderID;
    }
}
