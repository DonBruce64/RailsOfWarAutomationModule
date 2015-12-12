package rowautomation.tileentities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rowautomation.Chunkloader;

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
	
	public Entity getNearbyStock(String stockName, int stockRange){
		for(Entity stock : Chunkloader.stockList){
			if(stock.getClass().getName().startsWith("net.row.stock."+stockName)){
				if(Math.abs(stock.posX-this.xCoord)<stockRange && Math.abs(stock.posY-this.yCoord)<stockRange && Math.abs(stock.posZ-this.zCoord)<stockRange){
					return stock;
				}
			}
		}
		return null;
	}
	
	public List getAllNearbyStock(String stockName, int stockRange){
		List stockList = new ArrayList();
		for(Entity stock : Chunkloader.stockList){
			if(stock.getClass().getName().startsWith("net.row.stock."+stockName)){
				if(Math.abs(stock.posX-this.xCoord)<stockRange && Math.abs(stock.posY-this.yCoord)<stockRange && Math.abs(stock.posZ-this.zCoord)<stockRange){
					stockList.add(stock);
				}
			}
		}
		return stockList;
	}
	
	public NBTTagCompound getStockNBT(Object stock){
		Method[] stockMethods = stock.getClass().getMethods();
		NBTTagCompound stockNBTTag=new NBTTagCompound();
		for(int i=0;i<stockMethods.length;i++){
			if(stockMethods[i].getName()=="writeToNBT"){
			//if(stockMethods[i].getName()=="func_98035_c" || stockMethods[i].getName()=="func_145841_b"){
				try {
					stockMethods[i].invoke(stock, stockNBTTag);
					return stockNBTTag;
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
	
	
	
	public void setStockNBT(Object stock, NBTTagCompound stockNBTTag){
		Method[] stockMethods=stock.getClass().getMethods();
		for(int i=0;i<stockMethods.length;i++){
			if(stockMethods[i].getName()=="readFromNBT"){
			//if(stockMethods[i].getName()=="func_70020_e" || stockMethods[i].getName()=="func_145839_a"){
				try {
					stockMethods[i].invoke(stock, stockNBTTag);
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
