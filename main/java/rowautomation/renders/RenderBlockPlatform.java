package rowautomation.renders;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockPlatform extends RenderBlockBase{	
    
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int metadata=world.getBlockMetadata(x, y, z);
		int attachedBlockXOffset = (metadata & 3)==2 ? -1 : (metadata & 3)==0 ? 1 : 0;
		int attachedBlockZOffset = (metadata & 3)==3 ? -1 : (metadata & 3)==1 ? 1 : 0;
		int attachedBlockYOffset = Minecraft.getMinecraft().theWorld.getBlock(x, y-1, z).equals(block) ? -1 : 0;
		Block attachedBlock=world.getBlock(x+attachedBlockXOffset, y+attachedBlockYOffset, z+attachedBlockZOffset);
    	int attachedBlockMetadata=world.getBlockMetadata(x+attachedBlockXOffset, y+attachedBlockYOffset, z+attachedBlockZOffset);
    	IIcon sidesIcon = !attachedBlock.equals(Blocks.air) ? attachedBlock.getIcon(2, attachedBlockMetadata) : Blocks.stone.getIcon(2, 0);
        IIcon topIcon = block.getIcon(0, 0);
        
		float topU = topIcon.getMaxU();
		float topu = topIcon.getMinU();
		float topV = topIcon.getMaxV();
		float topv = topIcon.getMinV();
		float sideU = sidesIcon.getMaxU();
		float sideu = sidesIcon.getMinU();
		float sideV = sidesIcon.getMaxV();
		float sidev = sidesIcon.getMinV();
		
		float yMax = (metadata & 4) == 4 ? 0.5F : 1;
		float yMin = (metadata & 8) == 8 ? 0.5F : 0;
		float uPartial = sideU - 0.375F*(sideU - sideu);
		float vMax = (metadata & 12)==0 ? sideV : sideV-0.5F*(sideV-sidev);
		double[][][] render = new double[][][]{
			{{0,yMax,0,topu,topV},{0,yMax,1,topu,topv},{1,yMax,1,topU,topv},{1,yMax,0,topU,topV}},
			{{0,yMin,0.375,sideu,vMax},{0,yMax,0.375,sideu,sidev},{1,yMax,0.375,sideU,sidev},{1,yMin,0.375,sideU,vMax}},
			{{1,yMin,1,sideu,vMax},{1,yMax,1,sideu,sidev},{0,yMax,1,sideU,sidev},{0,yMin,1,sideU,vMax}},
			{{0,yMin,1,sideu,vMax},{0,yMax,1,sideu,sidev},{0,yMax,0.375,uPartial,sidev},{0,yMin,0.375,uPartial,vMax}},
			{{1,yMin,0.375,sideu,vMax},{1,yMax,0.375,sideu,sidev},{1,yMax,1,uPartial,sidev},{1,yMin,1,uPartial,vMax}},
			{{0,0,1,sideu,sideV},{0,0,0.375,sideu,sidev},{1,0,0.375,sideU,sideV},{1,0,1,sideU,vMax}}
			};
		
		render = rotateRender(render, Math.toRadians(90*(metadata%4)-90), 6);
		tessellator.addTranslation(x, y, z);
		tessellator.setColorOpaque(255, 255, 255);		
		for(int i=0; i<5; ++i){
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z) - (i!=0 ? 2097152 : 0));
			for(int j=0; j<4; ++j){
				tessellator.addVertexWithUV(render[i][j][0], render[i][j][1], render[i][j][2], render[i][j][3], render[i][j][4]);
			}
		}
		tessellator.addTranslation(-x, -y, -z);
        return true;
	}
}
