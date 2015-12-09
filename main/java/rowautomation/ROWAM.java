package rowautomation;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import rowautomation.guis.GUIHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ROWAM.MODID, name = ROWAM.MODNAME, version = ROWAM.MODVER)
public class ROWAM{
	public static final String MODID="rowam";
	public static final String MODNAME="Rails of War Automation Module";
	public static final String MODVER="5.5.0";
	
	public static final Chunkloader chunkLoader = new Chunkloader();
	public static final SimpleNetworkWrapper ROWAMNet = NetworkRegistry.INSTANCE.newSimpleChannel("ROWAMNet");
	public static final CreativeTabs tabROWAM = new CreativeTabs("tabROWAM") {
	    @Override
		@SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	    	return Item.getItemFromBlock((Block)Block.blockRegistry.getObject("rowam:SignalBlock"));
	    }
	};
	
	public enum GUIs{
	    Pointer,
	    Signal,
	    Station;
	}
	
	@Instance(value = ROWAM.MODID)
	public static ROWAM instance;
	@SidedProxy(clientSide="rowautomation.ClientProxy", serverSide="rowautomation.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void Init(FMLInitializationEvent event){		
		MinecraftForge.EVENT_BUS.register(chunkLoader);
		FMLCommonHandler.instance().bus().register(chunkLoader);
		ForgeChunkManager.setForcedChunkLoadingCallback(ROWAM.instance, chunkLoader);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		proxy.init();
	}
}