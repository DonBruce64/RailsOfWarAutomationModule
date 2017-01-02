package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.row.stock.core.RoWLocomotive;
import net.row.stock.core.plugin.RegulatorA;

public class TileEntitySignal extends TileEntityBase{
	public String locoLabel="";
	public int angle=0;
	public int reverseMax=999;
	public int reverseMin=-1;
	public int reverseSet=0;
	public int regulatorMax=999;
	public int regulatorMin=-1;
	public int regulatorSet=0;
	
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
		int normalizedYaw = (int) (locomotive.rotationYaw%360 + 360 + (Math.signum(locomotive.projectedSpeed) == -1 ? -180 : 0))%360;
		if(!(normalizedYaw + 5 > angle && normalizedYaw - 5 < angle)){return;}
		int normalizedReverse = (int) Math.abs(100F*locomotive.reverse/locomotive.maxReverse);
		if(normalizedReverse>reverseMax && normalizedReverse<reverseMin){
			if(normalizedReverse!=Math.abs(reverseSet)){
				locomotive.reverse =  (int) (getSign(locomotive.reverse)*(reverseSet/100F)*locomotive.maxReverse);
				changeOpStatus(true);
				cooldown=10;
			}
		}
		if(locomotive instanceof RegulatorA){
			RegulatorA locoReg = (RegulatorA) locomotive;
			int normalizedRegulator = (int) (100F*locoReg.getRegulator()/locoReg.getRegulatorMax());
			if(normalizedRegulator>regulatorMax && normalizedRegulator<regulatorMin){
				locoReg.setRegulator(locoReg.getRegulatorMax()*regulatorSet/100);
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
		this.locoLabel = tagcompound.getString("locoLabel");
		this.angle=tagcompound.getInteger("angle");
		this.reverseMax=tagcompound.getInteger("reverseMax");
	    this.reverseMin=tagcompound.getInteger("reverseMin");
	    this.reverseSet=tagcompound.getInteger("reverseSet");
	    this.regulatorMax=tagcompound.getInteger("regulatorMax");
	    this.regulatorMin=tagcompound.getInteger("regulatorMin");
	    this.regulatorSet=tagcompound.getInteger("regulatorSet");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setString("locoLabel", this.locoLabel);
		tagcompound.setInteger("angle", this.angle);
		tagcompound.setInteger("reverseMax", this.reverseMax);
		tagcompound.setInteger("reverseMin", this.reverseMin);
		tagcompound.setInteger("reverseSet", this.reverseSet);
		tagcompound.setInteger("regulatorMax", this.regulatorMax);
		tagcompound.setInteger("regulatorMin", this.regulatorMin);
		tagcompound.setInteger("regulatorSet", this.regulatorSet);
	}
}
