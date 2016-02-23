package rowautomation.tileentities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity{
	public boolean finishedOperation=false;
	public int range=4;
	public int detectorRange=6;
	public int stationCartRange=25;
	
	public Entity getNearbyStock(Class stockClass, int stockRange){
		List<Entity> entityList = getAllNearbyStock(stockClass, stockRange);
		if(entityList.size() > 0){
			return entityList.get(0);
		}else{
			return null;
		}
	}
	
	public List getAllNearbyStock(Class stockClass, int stockRange){
		return this.worldObj.getEntitiesWithinAABB(stockClass, this.getRenderBoundingBox().expand(range, range, range));
	}
	
	public void changeOpStatus(boolean setStatus){
		if(setStatus){
			finishedOperation=true;
		}else{
			finishedOperation=false;
		}
		this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		this.readFromNBT(packet.func_148857_g());
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.finishedOperation=tagcompound.getBoolean("finishedOperation");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("finishedOperation", this.finishedOperation);
	}
}
