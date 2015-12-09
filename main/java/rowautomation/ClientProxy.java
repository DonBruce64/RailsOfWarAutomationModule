package rowautomation;

import rowautomation.blocks.decorative.BlockCrossingCenter;
import rowautomation.blocks.decorative.BlockCrossingFull;
import rowautomation.blocks.decorative.BlockCrossingHalf;
import rowautomation.blocks.decorative.BlockPlatform;
import rowautomation.clock.EntityBigClock;
import rowautomation.clock.RenderEntityBigClock;
import rowautomation.renders.RenderBlockCrossing;
import rowautomation.renders.RenderBlockPlatform;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy{
	@Override
	public void init(){
		super.init();
		RenderingRegistry.registerBlockHandler(BlockCrossingFull.renderID, new RenderBlockCrossing());
		RenderingRegistry.registerBlockHandler(BlockCrossingHalf.renderID, new RenderBlockCrossing());
		RenderingRegistry.registerBlockHandler(BlockCrossingCenter.renderID, new RenderBlockCrossing());
		RenderingRegistry.registerBlockHandler(BlockPlatform.renderID, new RenderBlockPlatform());
		RenderingRegistry.registerEntityRenderingHandler(EntityBigClock.class, new RenderEntityBigClock());
	}
}
