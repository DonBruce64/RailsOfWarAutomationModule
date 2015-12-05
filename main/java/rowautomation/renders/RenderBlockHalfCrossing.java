package rowautomation.renders;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockHalfCrossing extends RenderBlockBase{
	private Tessellator tessellator = Tessellator.instance;

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		IIcon rampIcon;
		int metadata = world.getBlockMetadata(x, y, z);
		if(metadata > 3){
			rampIcon = world.getBlock(x, y-1, z).getIcon(1, world.getBlockMetadata(x, y-1, z));
			if(rampIcon == null){
				rampIcon = block.getIcon(0, 0);
			}
		}else{
			rampIcon = block.getIcon(0, 0);
		}
		float rU = rampIcon.getMaxU();
		float ru = rampIcon.getMinU();
		float rV = rampIcon.getMaxV();
		float rv = rampIcon.getMinV();
		float deltarU = rU-ru;
		float deltarV = rV-rv;
		double[][][] render = new double[][][]{
			{{0,0,0,ru,rv},{0,0.4,1.2,rU-deltarU*.2,rv},{1,0.4,1.2,rU-deltarU*.2,rV},{1,0,0,ru,rV}},//ramp
			{{0,0,1.2,ru,rV},{0,0.4,1.2,rU,rV},{0,0,0,rU,rv},{0,0,0,ru,rv}},//right triangle
			{{1,0.4,1.2,rU,rV},{1,0,1.2,ru,rV},{1,0,0,rU,rv},{1,0,0,ru,rv}},//left triangle
			{{0,0,1.55,ru,rV-deltarV*.70},{0,0.4,1.55,rU,rV-deltarV*.70},{0,0.4,1.2,rU,rv},{0,0,1.2,ru,rv}},//right square
			{{1,0,1.2,ru,rv},{1,0.4,1.2,rU,rv},{1,0.4,1.55,rU,rV-deltarV*.70},{1,0,1.55,ru,rV-deltarV*.70}},//left square
			{{0,0.4,1.2,ru+deltarU*.8,rv},{0,0.4,1.55,rU,rv},{1,0.4,1.55,rU,rV},{1,0.4,1.2,ru+deltarU*.8,rV}},//top
			{{0,0,1.55,ru,rv},{1,0,1.55,ru,rV-deltarV*.30},{1,0.4,1.55,rU,rV-deltarV*.30},{0,0.4,1.55,rU,rv}}//backplate
		};
		
		render=rotateRender(render, Math.toRadians(90*(metadata & 3)), 7);	
		tessellator.addTranslation(x, y, z);
		tessellator.setColorOpaque(255, 255, 255);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z) -  2097152);
		for(int i=0; i<7; ++i){
			for(int j=0; j<4; ++j){
				tessellator.addVertexWithUV(render[i][j][0], render[i][j][1], render[i][j][2], render[i][j][3], render[i][j][4]);
			}
		}
		tessellator.addTranslation(-x, -y, -z);		
		return true;
	}
}
