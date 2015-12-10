package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

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
		Entity locomotive=getNearbyStock("loco",range);
		if(locomotive==null){return;}
		NBTTagCompound locomotiveNBT=getStockNBT(locomotive);
		if(locomotiveNBT==null){return;}
		int currentReverse = locomotiveNBT.getInteger("reverse");
		if(!this.locoLabel.equals("") && !locomotiveNBT.getString("label").equals(this.locoLabel)){return;}
		if(Math.abs(currentReverse)>reverseMax){
			if(Math.abs(currentReverse)!=Math.abs(reverseSetMax)){
				locomotiveNBT.setInteger("reverse", sign(currentReverse)*reverseSetMax);
				setStockNBT(locomotive, locomotiveNBT);
				changeOpStatus(true);
				cooldown=10;
			}
		}else if(Math.abs(currentReverse)<reverseMin){
			if(Math.abs(currentReverse)!=Math.abs(reverseSetMin)){
				locomotiveNBT.setInteger("reverse", sign(currentReverse)*reverseSetMin);
				setStockNBT(locomotive, locomotiveNBT);
				changeOpStatus(true);
				cooldown=10;
			}
		}
	}
	
	int sign(int i) {
		if (i < 0){
			return -1;
	    }else{
	    	return 1;
		}
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