package rowautomation.blocks.decorative;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockCrossingFull extends BlockCrossingBase{

	public BlockCrossingFull() {
		super();
		this.setBlockName("FullCrossingBlock");
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(meta < 8){
    		return rampIcon;
    	}else{
    		return super.getIcon(side, meta);
    	}
	}	
}
