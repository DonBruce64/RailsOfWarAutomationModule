package rowautomation.tileentities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityStation extends TileEntityBase{
	public int OpMode=1;
	public int ReverseSet=5;
	public int Ticks=1;
	public int TickDelay=500;
	public int WhistleMode=1;
	public int LoadingOps=0;
	public int UnloadingOps=100;
	public float WhistleVolume=1;
	public float WhistlePitch=1;
	public long ScheduledTime=0;
	public String LocoLabel="";
	private Entity Locomotive;
	private NBTTagCompound LocomotiveNBT;
	
	@Override
	public void updateEntity(){
		changeOpStatus(false);
		Locomotive=getNearbyStock("loco", Range);
		if(Locomotive==null){return;}
		LocomotiveNBT=getStockNBT(Locomotive);
		if(!(LocomotiveNBT.getInteger("reverse")==0)){return;}
		if(!this.LocoLabel.equals("") && !LocomotiveNBT.getString("label").equals(this.LocoLabel)){return;}
		changeOpStatus(true);
		if(LocomotiveNBT.getBoolean("brake")==false){
			LocomotiveNBT.setBoolean("brake",true);
			setStockNBT(Locomotive, LocomotiveNBT);
			unmountEntities();
			if(WhistleMode==2 || WhistleMode==4){blowWhistle();}
		}
		if(OpMode==1){
			++Ticks;
			if(Ticks<TickDelay){return;}
		}
		if(OpMode==2 && (worldObj.getWorldTime()%24000) != ScheduledTime){return;}
		if(OpMode==3 && (this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)==0)){return;}
		LocomotiveNBT.setInteger("reverse", ReverseSet);
		LocomotiveNBT.setBoolean("brake",false);
		setStockNBT(Locomotive, LocomotiveNBT);
		mountEntities();
		if(WhistleMode==3 || WhistleMode==4){blowWhistle();}
		changeOpStatus(false);
		Ticks=1;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.FinishedOperation=tagcompound.getBoolean("FinishedOperation");
	    this.OpMode=tagcompound.getInteger("OpMode");
	    this.Ticks=tagcompound.getInteger("Ticks");
	    this.TickDelay=tagcompound.getInteger("TickDelay");
	    this.ReverseSet=tagcompound.getInteger("ReverseSet");
	    this.WhistleMode=tagcompound.getInteger("WhistleMode");
	    this.LoadingOps=tagcompound.getInteger("LoadingOps");
	    this.UnloadingOps=tagcompound.getInteger("UnloadingOps");
	    this.WhistleVolume=tagcompound.getFloat("WhistleVolume");
	    this.WhistlePitch=tagcompound.getFloat("WhistlePitch");
	    this.ScheduledTime=tagcompound.getLong("ScheduledTime");
	    this.LocoLabel = tagcompound.getString("LocoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("FinishedOperation", this.FinishedOperation);
		tagcompound.setInteger("OpMode", this.OpMode);
		tagcompound.setInteger("Ticks", this.Ticks);
		tagcompound.setInteger("TickDelay", this.TickDelay);
		tagcompound.setInteger("ReverseSet", this.ReverseSet);
		tagcompound.setInteger("WhistleMode", this.WhistleMode);
		tagcompound.setInteger("LoadingOps", this.LoadingOps);
		tagcompound.setInteger("UnloadingOps", this.UnloadingOps);
		tagcompound.setFloat("WhistleVolume", this.WhistleVolume);
		tagcompound.setFloat("WhistlePitch", this.WhistlePitch);
		tagcompound.setLong("ScheduledTime",this.ScheduledTime);
		tagcompound.setString("LocoLabel", this.LocoLabel);
	}
	
	private void blowWhistle(){
		String WhistleName="";
		if(Locomotive.getClass().getName().contains("Ov")){WhistleName="row:whistle_ov";}
		if(Locomotive.getClass().getName().contains("Yer")){WhistleName="row:whistle_yer";}
		if(Locomotive.getClass().getName().contains("Cher")){WhistleName="row:whistle_cher";}
		worldObj.playSound(Locomotive.posX, Locomotive.posY, Locomotive.posZ, WhistleName, WhistleVolume, WhistlePitch, true);
	}
	
	private void mountEntities(){
		double testDistance;
		double shortestDistance;
		double[] mountingOffset;
		Entity stock;
		Entity testEntity;
		Entity closestEntity;
		List EntityList;
		List StockList = getAllNearbyStock("", StationCartRange);
		AxisAlignedBB LoadingArea;
		for(int i=0; i<StockList.size(); ++i){
			stock=(Entity) StockList.get(i);
			if(stock.riddenByEntity==null && isValidMountingStock(stock, LoadingOps)){
				if(stock.motionX<1 && stock.motionZ<1){
					mountingOffset=getMountingOffset(stock, LoadingOps);
					if(mountingOffset[0]==0){
						LoadingArea=stock.boundingBox.expand(5, 5, 5);
					}else{
						LoadingArea=stock.boundingBox.offset(mountingOffset[0], mountingOffset[1], mountingOffset[2]).expand(2, 2, 2);
					}
					closestEntity=null;
					shortestDistance=10;
					EntityList = this.worldObj.getEntitiesWithinAABBExcludingEntity(stock, LoadingArea);
					for(int j=0; j<EntityList.size(); ++j){
						testEntity=(Entity) EntityList.get(j);
						if(isValidMountingEntity(testEntity, LoadingOps)){
							testDistance=testEntity.getDistanceToEntity(stock);
							if(testDistance<shortestDistance){
								closestEntity=testEntity;
								shortestDistance=testDistance;
							}
						}
					}
					if(closestEntity!=null){
						closestEntity.mountEntity(stock);
					}
				}
			}
		}
	}
	
	private void unmountEntities(){
		double[] mountingOffset;
		Entity stock;
		Entity ridingEntity;
		List StockList = getAllNearbyStock("", StationCartRange);
		for(int i=0; i<StockList.size(); ++i){
			stock=(Entity) StockList.get(i);
			ridingEntity=stock.riddenByEntity;
			if(stock.motionX<10 && stock.motionZ<10 && ridingEntity!=null){
				if(isValidMountingStock(stock, UnloadingOps) && isValidMountingEntity(ridingEntity, UnloadingOps)){
					ridingEntity.yOffset=0.0F;
					ridingEntity.mountEntity(null);
					mountingOffset=getMountingOffset(stock, UnloadingOps);
					ridingEntity.moveEntity(mountingOffset[0], mountingOffset[1], mountingOffset[2]);
					ridingEntity.fallDistance=0.0F;
				}
			}
		}
	}
	
	private boolean isValidMountingEntity(Entity entity, int ops){
    	if(ops%10>=5){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntityChicken.class)){return true;}
    	}else if(ops%10>=4){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntityPig.class)){return true;}
    	}else if(ops%10>=3){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntitySheep.class)){return true;}
    	}else if(ops%10>=2){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntityCow.class)){return true;}
    	}else if(ops%10>=1){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntityHorse.class)){return true;}
    	}else if(ops%10>=0){
    		if(entity.getClass().equals(net.minecraft.entity.passive.EntityVillager.class)){return true;}
    	}
    	return false;
	}
	
	private boolean isValidMountingStock(Entity stock, int ops){
    	if(ops%100>=30){
    		if(stock.getClass().getName().contains("loco")){return true;}
    	}else if(ops%100>=20){
    		if(stock.getClass().getName().contains("CartIII")){return true;}
    	}else if(ops%100>=10){
    		if(stock.getClass().getName().contains("NTV")){return true;}
    	}else if(ops%100>=0){
    		if(stock.getClass().getName().contains("loco")){return true;}
    		if(stock.getClass().getName().contains("CartIII")){return true;}
    		if(stock.getClass().getName().contains("NTV")){return true;}
    	}
    	return false;
	}
	
	private double[] getMountingOffset(Entity stock, int ops){
		double xOffset=3*Math.cos(Math.toRadians(stock.rotationYaw));
		double zOffset=3*Math.sin(Math.toRadians(stock.rotationYaw));
    	if(ops%1000>=200){
    		return new double[] {xOffset, -1, zOffset};
    	}else if(ops%1000>=100){
    		return new double[] {xOffset*-1, -1, zOffset*-1};
    	}
    	else{
    		return new double[] {0, -1, 0};
    	}
	}
}