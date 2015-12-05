package rowautomation.blocks.decorative;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFullCrossing extends Block{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	public IIcon rampIcon;
	public IIcon centerIcon;
	
	public BlockFullCrossing() {
		super(Material.rock);
		this.blockHardness=-1;
		this.setCreativeTab(ROWAM.tabROWAM);
		this.setBlockBounds(0, 0, 0, 1, 0.2F, 1);
		this.setBlockName("FullCrossingBlock");
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item){
		int blockMetadata=world.getBlockMetadata(x, y, z);
		float playerRotation=player.rotationYaw%360;
		if(playerRotation < 0){
			playerRotation += 360;
		}
		if(playerRotation<45 || playerRotation>=315){//south
			blockMetadata=0;
		}else if(playerRotation<135){//east
			blockMetadata=1;
		}else if(playerRotation<225){//north
			blockMetadata=2;
		}else{//west
			blockMetadata=3;
		}
		world.setBlockMetadataWithNotify(x, y, z, blockMetadata, 2);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ){
		int metadata = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, metadata > 3 ?  metadata - 4 : metadata + 4, 2);
        return true;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister){
		rampIcon = par1IconRegister.registerIcon("rowam:crossingblock");
		centerIcon = par1IconRegister.registerIcon("rowam:crossingblock_center");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(side == 0){
    		return rampIcon;
    	}else{
    		return centerIcon;
    	}
	}	
	
    @Override
    public boolean isOpaqueCube() {
            return false;
    }
	
	@Override
	public int getRenderType(){
        return renderID;
    }
}
