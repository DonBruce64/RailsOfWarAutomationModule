package rowautomation.tileentities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity{
	public boolean FinishedOperation=false;
	public int Range=4;
	public int DetectorRange=6;
	public int StationCartRange=25;
	
	public Entity getNearbyStock(String StockName, int StockRange){
		for(int i=0;i<this.worldObj.loadedEntityList.size();++i){
			if(this.worldObj.loadedEntityList.get(i).getClass().getName().startsWith("net.row.stock."+StockName)){
				Entity Stock=(Entity) this.worldObj.loadedEntityList.get(i);
				if(Math.abs(Stock.posX-this.xCoord)<StockRange && Math.abs(Stock.posY-this.yCoord)<StockRange && Math.abs(Stock.posZ-this.zCoord)<StockRange){
					return Stock;
				}
			}
		}
		return null;
	}
	
	public List getAllNearbyStock(String StockName, int StockRange){
		Entity Stock;
		List StockList = new ArrayList();
		for(int i=0;i<this.worldObj.loadedEntityList.size();++i){
			Stock=(Entity) this.worldObj.loadedEntityList.get(i);
			if(Stock.getClass().getName().startsWith("net.row.stock."+StockName)){
				if(Math.abs(Stock.posX-this.xCoord)<StockRange && Math.abs(Stock.posY-this.yCoord)<StockRange && Math.abs(Stock.posZ-this.zCoord)<StockRange){
					StockList.add(Stock);
				}
			}
		}
		return StockList;
	}
	
	public NBTTagCompound getStockNBT(Object Stock){
		Method[] StockMethods=Stock.getClass().getMethods();
		NBTTagCompound StockNBTTag=new NBTTagCompound();
		for(int i=0;i<StockMethods.length;i++){
			//if(StockMethods[i].getName()=="writeToNBT"){
			if(StockMethods[i].getName()=="func_98035_c" || StockMethods[i].getName()=="func_145841_b"){
				try {
					StockMethods[i].invoke(Stock, StockNBTTag);
					return StockNBTTag;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	
	public void setStockNBT(Object Stock, NBTTagCompound StockNBTTag){
		Method[] StockMethods=Stock.getClass().getMethods();
		for(int i=0;i<StockMethods.length;i++){
			//if(StockMethods[i].getName()=="readFromNBT"){
			if(StockMethods[i].getName()=="func_70020_e" || StockMethods[i].getName()=="func_145839_a"){
				try {
					StockMethods[i].invoke(Stock, StockNBTTag);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void changeOpStatus(boolean SetStatus){
		if(SetStatus){
			FinishedOperation=true;
		}else{
			FinishedOperation=false;
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
}
