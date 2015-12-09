package rowautomation.blocks.decorative;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockCrossingCenter extends BlockCrossingBase{
	
	public BlockCrossingCenter() {
		super();
		this.setBlockName("CenterCrossingBlock");
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	if(meta < 8){
    		return centerIcon;
    	}else{
    		return super.getIcon(side, meta);
    	}
	}	
}
