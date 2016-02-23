package rowautomation;

import rowautomation.blocks.controller.BlockDetector;
import rowautomation.blocks.controller.BlockFueler;
import rowautomation.blocks.controller.BlockPointer;
import rowautomation.blocks.controller.BlockSignal;
import rowautomation.blocks.controller.BlockStation;
import rowautomation.blocks.controller.BlockTank;
import rowautomation.network.PacketPointer;
import rowautomation.network.PacketSignal;
import rowautomation.network.PacketStation;
import rowautomation.tileentities.TileEntityDetector;
import rowautomation.tileentities.TileEntityFueler;
import rowautomation.tileentities.TileEntityPointer;
import rowautomation.tileentities.TileEntitySignal;
import rowautomation.tileentities.TileEntityStation;
import rowautomation.tileentities.TileEntityTank;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy{
	public void init(){
		initBlocks();
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
