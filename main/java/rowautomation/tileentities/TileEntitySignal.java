package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.row.stock.core.RoWLocomotive;

public class TileEntitySignal extends TileEntityBase{
	public int reverseMax=999;
	public int reverseMin=-1;
	public int reverseSet=0;
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
		int normalizedReverse = (int) Math.abs(100F*locomotive.reverse/locomotive.maxReverse);
		if(normalizedReverse>reverseMax || normalizedReverse<reverseMin){
			if(normalizedReverse!=Math.abs(reverseSet)){
				locomotive.reverse =  (int) (getSign(locomotive.reverse)*(reverseSet/100F)*locomotive.maxReverse);
				changeOpStatus(true);
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
	    this.reverseSet=tagcompound.getInteger("reverseSet");
	    this.locoLabel = tagcompound.getString("locoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setInteger("reverseMax", this.reverseMax);
		tagcompound.setInteger("reverseMin", this.reverseMin);
		tagcompound.setInteger("reverseSet", this.reverseSet);
		tagcompound.setString("locoLabel", this.locoLabel);
	}
}
