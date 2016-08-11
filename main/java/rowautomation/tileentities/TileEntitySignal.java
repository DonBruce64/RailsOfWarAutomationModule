package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.row.stock.core.RoWLocomotive;

public class TileEntitySignal extends TileEntityBase{
	public int reverseMax=999;
	public int reverseMin=-1;
	public int reverseSetMax=0;
	public int reverseSetMin=0;
	public String locoLabel="";
	private int cooldown=0;
	
	@Override
	public void updateEntity(){
		if(cooldown>0){
			--cooldown;
			return;
		}
		changeOpStatus(false);
		if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
		RoWLocomotive locomotive = (RoWLocomotive) getNearbyStock(RoWLocomotive.class,range);
		if(locomotive==null){return;}
		if(!this.locoLabel.equals("") && !locomotive.label.equals(this.locoLabel)){return;}
		if(Math.abs(locomotive.reverse)>reverseMax){
			if(Math.abs(locomotive.reverse)!=Math.abs(reverseSetMax)){
				locomotive.reverse =  (int) (getSign(locomotive.reverse)*(reverseSetMax/100F)*locomotive.maxReverse);
				changeOpStatus(true);
				cooldown=10;
			}
		}else if(Math.abs(locomotive.reverse)<reverseMin){
			if(Math.abs(locomotive.reverse)!=Math.abs(reverseSetMin)){
				locomotive.reverse = (int) (getSign(locomotive.reverse)*(reverseSetMin/100F)*locomotive.maxReverse);
				cooldown=10;
			}
		}
	}
	
	private int getSign(int i){
		return i < 0 ? -1 : 1;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.reverseMax=tagcompound.getInteger("reverseMax");
	    this.reverseMin=tagcompound.getInteger("reverseMin");
	    this.reverseSetMax=tagcompound.getInteger("reverseSetMax");
	    this.reverseSetMin=tagcompound.getInteger("reverseSetMin");
	    this.locoLabel = tagcompound.getString("locoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setInteger("reverseMax", this.reverseMax);
		tagcompound.setInteger("reverseMin", this.reverseMin);
		tagcompound.setInteger("reverseSetMax", this.reverseSetMax);
		tagcompound.setInteger("reverseSetMin", this.reverseSetMin);
		tagcompound.setString("locoLabel", this.locoLabel);
	}
}
