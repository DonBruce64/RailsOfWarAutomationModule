package rowautomation;

import rowautomation.blocks.controller.BlockDetector;
import rowautomation.blocks.controller.BlockFueler;
import rowautomation.blocks.controller.BlockPointer;
import rowautomation.blocks.controller.BlockSignal;
import rowautomation.blocks.controller.BlockStation;
import rowautomation.blocks.controller.BlockTank;
import rowautomation.blocks.decorative.BlockFullCrossing;
import rowautomation.blocks.decorative.BlockHalfCrossing;
import rowautomation.blocks.decorative.BlockPlatform;
import rowautomation.clock.EntityBigClock;
import rowautomation.clock.ItemBigClock;
import rowautomation.network.PacketPointer;
import rowautomation.network.PacketSignal;
import rowautomation.network.PacketStation;
import rowautomation.tileentities.TileEntityDetector;
import rowautomation.tileentities.TileEntityFueler;
import rowautomation.tileentities.TileEntityPointer;
import rowautomation.tileentities.TileEntitySignal;
import rowautomation.tileentities.TileEntityStation;
import rowautomation.tileentities.TileEntityTank;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy{
	public void init(){
		initBlocks();
		initItems();
		initEntites();
		initTileEntities();
		initPackets();
	}
	
	private static void initBlocks(){
		GameRegistry.registerBlock(new BlockSignal(), "SignalBlock");
	    GameRegistry.registerBlock(new BlockStation(), "StationBlock");
	    GameRegistry.registerBlock(new BlockPointer(), "PointerBlock");
	    GameRegistry.registerBlock(new BlockTank(), "TankBlock");
	    GameRegistry.registerBlock(new BlockFueler(), "FuelingBlock");
	    GameRegistry.registerBlock(new BlockDetector(), "DetectorBlock");
	    
	    GameRegistry.registerBlock(new BlockFullCrossing(), "FullCrossingBlock");
	    GameRegistry.registerBlock(new BlockHalfCrossing(), "HalfCrossingBlock");
	    GameRegistry.registerBlock(new BlockPlatform(), "PlatformBlock");
	}
	
	private static void initItems(){
		GameRegistry.registerItem(new ItemBigClock(), "BigClock");
	}
	
	private static void initEntites(){
		EntityRegistry.registerModEntity(EntityBigClock.class, "BigClock", 0, ROWAM.MODID, 160, Integer.MAX_VALUE, false);
	}
	
	private static void initTileEntities(){
	    GameRegistry.registerTileEntity(TileEntitySignal.class,"SignalTileEntity");
	    GameRegistry.registerTileEntity(TileEntityStation.class,"StationTileEntity");
	    GameRegistry.registerTileEntity(TileEntityPointer.class,"PointerTileEntity");
	    GameRegistry.registerTileEntity(TileEntityTank.class,"TankTileEntity");
	    GameRegistry.registerTileEntity(TileEntityFueler.class,"FuelingTileEntity");
	    GameRegistry.registerTileEntity(TileEntityDetector.class,"DetectorTileEntity");
	}
	
	private static void initPackets(){
		ROWAM.ROWAMNet.registerMessage(PacketPointer.PointerPacketHandler.class,  PacketPointer.class, 0, Side.SERVER);
		ROWAM.ROWAMNet.registerMessage(PacketStation.StationPacketHandler.class,  PacketStation.class, 1, Side.SERVER);
		ROWAM.ROWAMNet.registerMessage(PacketSignal.SignalPacketHandler.class,  PacketSignal.class, 2, Side.SERVER);
	}
}
