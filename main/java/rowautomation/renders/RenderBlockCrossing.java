package rowautomation.renders;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import rowautomation.blocks.decorative.BlockCrossingCenter;
import rowautomation.blocks.decorative.BlockCrossingFull;
import rowautomation.blocks.decorative.BlockCrossingHalf;

public class RenderBlockCrossing extends RenderBlockBase{	
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		IIcon centerIcon = null;
		IIcon sideIcon = null;
		IIcon topIcon = null;
		int blockMetadata = world.getBlockMetadata(x, y, z);
		int lowerBlockMetadata = world.getBlockMetadata(x, y-1, z);

		if(blockMetadata > 3){
			centerIcon = world.getBlock(x, y-1, z).getIcon(1, lowerBlockMetadata);
			sideIcon = world.getBlock(x, y-1, z).getIcon(2, lowerBlockMetadata);
			topIcon = world.getBlock(x, y-1, z).getIcon(1, lowerBlockMetadata);
		}
		if(centerIcon == null || sideIcon == null || topIcon == null){
			centerIcon = block.getIcon(0, blockMetadata + 8);
			topIcon = block.getIcon(1, blockMetadata + 8);
			sideIcon = block.getIcon(2, blockMetadata + 8);
		}

		float centerU = centerIcon.getMaxU();
		float centeru = centerIcon.getMinU();
		float centerV = centerIcon.getMaxV();
		float centerv = centerIcon.getMinV();
		float topU = topIcon.getMaxU();
		float topu = topIcon.getMinU();
		float topV = topIcon.getMaxV();
		float topv = topIcon.getMinV();
		float sideU = sideIcon.getMaxU();
		float sideu = sideIcon.getMinU();
		float sideV = sideIcon.getMaxV();
		float sidev = sideIcon.getMinV();
		
		float deltacenterU = centerU-centeru;
		float deltacenterV = centerV-centerv;
		float deltatopU = topU-topu;
		float deltatopV = topV-topv;
		float deltasideU = sideU-sideu;
		float deltasideV = sideV-sidev;
		
		int numberObjects = 0;
		double[][][] render = null;
		if(block instanceof BlockCrossingCenter){
			numberObjects = 5;
			render = new double[][][]{
					{{1,0.4,-0.55,sideU,sidev},{1,0.4,1.55,sideu,sidev},{1,0,1.55,sideu,sideV-deltasideV*.6},{1,0,-0.55,sideU,sideV-deltasideV*.6}},//right side
					{{0,0.4,1.55,sideU,sidev},{0,0.4,-0.55,sideu,sidev},{0,0,-0.55,sideu,sideV-deltasideV*.6},{0,0,1.55,sideU,sideV-deltasideV*.6}},//left side
					{{0,0.4,-0.55,sideU,sidev},{1,0.4,-0.55,sideu,sidev},{1,0,-0.55,sideu,sideV-deltasideV*.6},{0,0,-0.55,sideU,sideV-deltasideV*.6}},//front side
					{{1,0.4,1.55,sideU,sidev},{0,0.4,1.55,sideu,sidev},{0,0,1.55,sideu,sideV-deltasideV*.6},{1,0,1.55,sideU,sideV-deltasideV*.6}},//back side
					{{0,0.4,-0.55,centeru,centerv},{0,0.4,1.55,centerU,centerv},{1,0.4,1.55,centerU,centerV},{1,0.4,-0.55,centeru,centerV}}//top
					
				};
		}else if(block instanceof BlockCrossingFull){
			numberObjects = 12;
			render = new double[][][]{
					{{0,0.4,1.2,topu+deltatopU*.2,topV},{1,0.4,1.2,topu+deltatopU*.2,topv},{1,0,0,topU,topv},{0,0,0,topU,topV}},//ramp
					{{1,0,0,sideU,sidev},{1,0.4,1.2,sideu,sidev},{1,0,1.2,sideu,sideV-deltasideV*.7},{1,0,0,sideU,sideV-deltasideV*.7}},//left triangle
					{{0,0.4,1.2,sideU,sidev},{0,0,0,sideu,sideV-deltasideV*.7},{0,0,0,sideu,sideV-deltasideV*.7},{0,0,1.2,sideU,sideV-deltasideV*.7}},//right triangle					
					{{0,0.4,1.55,sideU-deltasideU*.65,sidev},{0,0.4,1.2,sideu,sidev},{0,0,1.2,sideu,sideV-deltasideV*.7},{0,0,1.55,sideU-deltasideU*.65,sideV-deltasideV*.7}},//right square
					{{1,0.4,1.2,sideU,sidev},{1,0.4,1.55,sideu+deltasideU*.65,sidev},{1,0,1.55,sideu+deltasideU*.65,sideV-deltasideV*.7},{1,0,1.2,sideU,sideV-deltasideV*.7}},//left square					
					{{0,0.4,1.55,topu,topV},{1,0.4,1.55,topu,topv},{1,0.4,1.2,topU-deltatopU*.8,topv},{0,0.4,1.2,topU-deltatopU*.8,topV}},//top
					{{1,0.4,1.55,sideU,sidev},{0,0.4,1.55,sideu,sidev},{0,0,1.55,sideu,sideV-deltasideV*.70},{1,0,1.55,sideU,sideV-deltasideV*.70}},//backplate
					
					{{1,0.4,1.9,sideU,sidev},{1,0.4,3.1,sideu,sidev},{1,0,3.1,sideu,sideV-deltasideV*.6},{1,0,1.9,sideU,sideV-deltasideV*.6}},//right side
					{{0,0.4,3.1,sideU,sidev},{0,0.4,1.9,sideu,sidev},{0,0,1.9,sideu,sideV-deltasideV*.6},{0,0,3.1,sideU,sideV-deltasideV*.6}},//left side
					{{0,0.4,1.9,sideU,sidev},{1,0.4,1.9,sideu,sidev},{1,0,1.9,sideu,sideV-deltasideV*.6},{0,0,1.9,sideU,sideV-deltasideV*.6}},//front side
					{{1,0.4,3.1,sideU,sidev},{0,0.4,3.1,sideu,sidev},{0,0,3.1,sideu,sideV-deltasideV*.6},{1,0,3.1,sideU,sideV-deltasideV*.6}},//back side
					{{0,0.4,1.9,centeru,centerv},{0,0.4,3.1,centerU,centerv},{1,0.4,3.1,centerU,centerV},{1,0.4,1.9,centeru,centerV}}//top
				};
		}else if(block instanceof BlockCrossingHalf){
			numberObjects = 7;
			render = new double[][][]{
					{{0,0.4,1.2,topu+deltatopU*.2,topV},{1,0.4,1.2,topu+deltatopU*.2,topv},{1,0,0,topU,topv},{0,0,0,topU,topV}},//ramp
					{{1,0,0,sideU,sidev},{1,0.4,1.2,sideu,sidev},{1,0,1.2,sideu,sideV-deltasideV*.7},{1,0,0,sideU,sideV-deltasideV*.7}},//left triangle
					{{0,0.4,1.2,sideU,sidev},{0,0,0,sideu,sideV-deltasideV*.7},{0,0,0,sideu,sideV-deltasideV*.7},{0,0,1.2,sideU,sideV-deltasideV*.7}},//right triangle					
					{{0,0.4,1.55,sideU-deltasideU*.65,sidev},{0,0.4,1.2,sideu,sidev},{0,0,1.2,sideu,sideV-deltasideV*.7},{0,0,1.55,sideU-deltasideU*.65,sideV-deltasideV*.7}},//right square
					{{1,0.4,1.2,sideU,sidev},{1,0.4,1.55,sideu+deltasideU*.65,sidev},{1,0,1.55,sideu+deltasideU*.65,sideV-deltasideV*.7},{1,0,1.2,sideU,sideV-deltasideV*.7}},//left square					
					{{0,0.4,1.55,topu,topV},{1,0.4,1.55,topu,topv},{1,0.4,1.2,topU-deltatopU*.8,topv},{0,0.4,1.2,topU-deltatopU*.8,topV}},//top
					{{1,0.4,1.55,sideU,sidev},{0,0.4,1.55,sideu,sidev},{0,0,1.55,sideu,sideV-deltasideV*.70},{1,0,1.55,sideU,sideV-deltasideV*.70}}//backplate
				};
		}else{
			return false;
		}
		render=rotateRender(render, Math.toRadians(90*(blockMetadata & 3)), numberObjects);	
		tessellator.addTranslation(x, y, z);
		tessellator.setColorOpaque(255, 255, 255);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		for(int i=0; i<numberObjects; ++i){
			for(int j=0; j<4; ++j){
				tessellator.addVertexWithUV(render[i][j][0], render[i][j][1], render[i][j][2], render[i][j][3], render[i][j][4]);
			}
		}
		tessellator.addTranslation(-x, -y, -z);		
		return true;
	}
}
