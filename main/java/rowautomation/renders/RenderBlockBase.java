package rowautomation.renders;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public abstract class RenderBlockBase implements ISimpleBlockRenderingHandler{
	protected Tessellator tessellator = Tessellator.instance;
	
	protected double[][][] rotateRender(double[][][] points, double angle, int numShapes){
		double[][][] rotated = new double[numShapes][4][5];
		for(int i=0; i<numShapes; ++i){
			for(int j=0; j<4; ++j){
				rotated[i][j][0] = Math.cos(angle)*(points[i][j][0]-0.5)-Math.sin(angle)*(points[i][j][2]-0.5)+0.5;
				rotated[i][j][2] = Math.sin(angle)*(points[i][j][0]-0.5)+Math.cos(angle)*(points[i][j][2]-0.5)+0.5;
				rotated[i][j][1] = points[i][j][1];
				rotated[i][j][3] = points[i][j][3];
				rotated[i][j][4] = points[i][j][4];
			}
		}
		return rotated;
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){}
	public boolean shouldRender3DInInventory(int modelId){return false;}
	public int getRenderId(){return 0;}
}
