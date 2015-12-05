package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySignal extends TileEntityBase{
	public int ReverseMax=999;
	public int ReverseMin=-1;
	public int ReverseSetMax=0;
	public int ReverseSetMin=0;
	public String LocoLabel="";
	private int Cooldown=0;
	private Entity Locomotive;
	private NBTTagCompound LocomotiveNBT;
	
	@Override
	public void updateEntity(){
		if(Cooldown>0){
			--Cooldown;
			return;
		}
		changeOpStatus(false);
		if(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)>0){return;}
		Locomotive=getNearbyStock("loco",this.Range);
		if(Locomotive==null){return;}
		LocomotiveNBT=getStockNBT(Locomotive);
		if(LocomotiveNBT==null){return;}
		int CurrentReverse=LocomotiveNBT.getInteger("reverse");
		if(!this.LocoLabel.equals("") && !LocomotiveNBT.getString("label").equals(this.LocoLabel)){return;}
		if(Math.abs(CurrentReverse)>ReverseMax){
			if(Math.abs(CurrentReverse)!=Math.abs(ReverseSetMax)){
				LocomotiveNBT.setInteger("reverse", sign(CurrentReverse)*ReverseSetMax);
				setStockNBT(Locomotive, LocomotiveNBT);
				changeOpStatus(true);
				Cooldown=10;
			}
		}else if(Math.abs(CurrentReverse)<ReverseMin){
			if(Math.abs(CurrentReverse)!=Math.abs(ReverseSetMin)){
				LocomotiveNBT.setInteger("reverse", sign(CurrentReverse)*ReverseSetMin);
				setStockNBT(Locomotive, LocomotiveNBT);
				changeOpStatus(true);
				Cooldown=10;
			}
		}
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.FinishedOperation=tagcompound.getBoolean("FinishedOperation");
	    this.ReverseMax=tagcompound.getInteger("ReverseMax");
	    this.ReverseMin=tagcompound.getInteger("ReverseMin");
	    this.ReverseSetMax=tagcompound.getInteger("ReverseSetMax");
	    this.ReverseSetMin=tagcompound.getInteger("ReverseSetMin");
	    this.LocoLabel = tagcompound.getString("LocoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("FinishedOperation", this.FinishedOperation);
		tagcompound.setInteger("ReverseMax", this.ReverseMax);
		tagcompound.setInteger("ReverseMin", this.ReverseMin);
		tagcompound.setInteger("ReverseSetMax", this.ReverseSetMax);
		tagcompound.setInteger("ReverseSetMin", this.ReverseSetMin);
		tagcompound.setString("LocoLabel", this.LocoLabel);
	}
	
	int sign(int i) {
		if (i < 0){
			return -1;
	    }else{
	    	return 1;
		}
	}	
}