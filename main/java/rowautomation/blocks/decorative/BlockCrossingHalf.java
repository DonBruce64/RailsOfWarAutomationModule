package rowautomation.blocks.decorative;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockCrossingHalf extends BlockCrossingBase{
	
	public BlockCrossingHalf() {
		super();
		this.setBlockName("HalfCrossingBlock");
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(meta < 8){
    		return sideIcon;
    	}else{
    		return super.getIcon(side, meta);
    	}
	}	
}
