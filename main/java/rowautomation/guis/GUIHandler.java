package rowautomation.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import rowautomation.ROWAM;
import rowautomation.tileentities.TileEntityPointer;
import rowautomation.tileentities.TileEntitySignal;
import rowautomation.tileentities.TileEntityStation;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID==ROWAM.GUIs.Pointer.ordinal()){return new GUIPointer((TileEntityPointer) world.getTileEntity(x, y, z));}
		if(ID==ROWAM.GUIs.Signal.ordinal()){return new GUISignal((TileEntitySignal) world.getTileEntity(x, y, z));}
		if(ID==ROWAM.GUIs.Station.ordinal()){return new GUIStation((TileEntityStation) world.getTileEntity(x, y, z));}
		return null;
	}
}
