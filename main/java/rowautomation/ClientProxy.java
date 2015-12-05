package rowautomation;

import rowautomation.blocks.decorative.BlockFullCrossing;
import rowautomation.blocks.decorative.BlockHalfCrossing;
import rowautomation.blocks.decorative.BlockPlatform;
import rowautomation.clock.EntityBigClock;
import rowautomation.clock.RenderEntityBigClock;
import rowautomation.renders.RenderBlockFullCrossing;
import rowautomation.renders.RenderBlockHalfCrossing;
import rowautomation.renders.RenderBlockPlatform;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy{
	@Override
	public void init(){
		super.init();
		RenderingRegistry.registerBlockHandler(BlockFullCrossing.renderID, new RenderBlockFullCrossing());
		RenderingRegistry.registerBlockHandler(BlockHalfCrossing.renderID, new RenderBlockHalfCrossing());
		RenderingRegistry.registerBlockHandler(BlockPlatform.renderID, new RenderBlockPlatform());
		RenderingRegistry.registerEntityRenderingHandler(EntityBigClock.class, new RenderEntityBigClock());
	}
}
