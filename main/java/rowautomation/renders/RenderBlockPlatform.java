package rowautomation.renders;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockPlatform extends RenderBlockBase{	
	private int metadata;
	private int attachedBlockMetadata;
	private int attachedBlockXOffset;
	private int attachedBlockYOffset;
	private int attachedBlockZOffset;
	private float topU;
	private float topu;
	private float topV;
	private float topv;
	private float sideU;
	private float sideu;
	private float sideV;
	private float sidev;
	private float uPartial;
	private float vMax;
	private float yMax;
	private float yMin;
	private IIcon topIcon;
	private IIcon sidesIcon;
	Block attachedBlock;
	private double[][][] renders;
	private Tessellator tessellator = Tessellator.instance;
    
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		metadata=world.getBlockMetadata(x, y, z);
		attachedBlockXOffset = (metadata & 3)==2 ? -1 : (metadata & 3)==0 ? 1 : 0;
		attachedBlockZOffset = (metadata & 3)==3 ? -1 : (metadata & 3)==1 ? 1 : 0;
		attachedBlockYOffset = Minecraft.getMinecraft().theWorld.getBlock(x, y-1, z).equals(block) ? -1 : 0;
		attachedBlock=world.getBlock(x+attachedBlockXOffset, y+attachedBlockYOffset, z+attachedBlockZOffset);
    	attachedBlockMetadata=world.getBlockMetadata(x+attachedBlockXOffset, y+attachedBlockYOffset, z+attachedBlockZOffset);
    	sidesIcon = !attachedBlock.equals(Blocks.air) ? attachedBlock.getIcon(2, attachedBlockMetadata) : Blocks.stone.getIcon(2, 0);
        topIcon = block.getIcon(0, 0);
        
		topU = topIcon.getMaxU();
		topu = topIcon.getMinU();
		topV = topIcon.getMaxV();
		topv = topIcon.getMinV();
        sideU = sidesIcon.getMaxU();
    	sideu = sidesIcon.getMinU();
    	sideV = sidesIcon.getMaxV();
    	sidev = sidesIcon.getMinV();
		
		yMax = (metadata & 4) == 4 ? 0.5F : 1;
		yMin = (metadata & 8) == 8 ? 0.5F : 0;
		uPartial = sideU - 0.375F*(sideU - sideu);
		vMax = (metadata & 12)==0 ? sideV : sideV-0.5F*(sideV-sidev);
		renders = rotateRender(
			new double[][][]{
			{{0,yMax,0,topu,topV},{0,yMax,1,topu,topv},{1,yMax,1,topU,topv},{1,yMax,0,topU,topV}},
			{{0,yMin,0.375,sideu,vMax},{0,yMax,0.375,sideu,sidev},{1,yMax,0.375,sideU,sidev},{1,yMin,0.375,sideU,vMax}},
			{{1,yMin,1,sideu,vMax},{1,yMax,1,sideu,sidev},{0,yMax,1,sideU,sidev},{0,yMin,1,sideU,vMax}},
			{{0,yMin,1,sideu,vMax},{0,yMax,1,sideu,sidev},{0,yMax,0.375,uPartial,sidev},{0,yMin,0.375,uPartial,vMax}},
			{{1,yMin,0.375,sideu,vMax},{1,yMax,0.375,sideu,sidev},{1,yMax,1,uPartial,sidev},{1,yMin,1,uPartial,vMax}},
			{{0,0,1,sideu,sideV},{0,0,0.375,sideu,sidev},{1,0,0.375,sideU,sideV},{1,0,1,sideU,vMax}}
			},Math.toRadians(90*(metadata%4)-90), 6);
		
		tessellator.addTranslation(x, y, z);
		tessellator.setColorOpaque(255, 255, 255);		
		for(int i=0; i<5; ++i){
			tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z) - (i!=0 ? 2097152 : 0));
			for(int j=0; j<4; ++j){
				tessellator.addVertexWithUV(renders[i][j][0], renders[i][j][1], renders[i][j][2], renders[i][j][3], renders[i][j][4]);
			}
		}
		tessellator.addTranslation(-x, -y, -z);
        return true;
	}
}
