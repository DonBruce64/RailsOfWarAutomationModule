package rowautomation.tileentities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.row.stock.cart.CartIII2L12;
import net.row.stock.cart.CartNTV;
import net.row.stock.core.RoWLocomotive;
import net.row.stock.core.RoWRollingStock;

public class TileEntityStation extends TileEntityBase{
	public int opMode=1;
	public int reverseSet=5;
	public int ticks=1;
	public int tickDelay=500;
	public int whistleMode=1;
	public int loadingOps=0;
	public int unloadingOps=100;
	public float whistleVolume=1;
	public float whistlePitch=1;
	public long scheduledTime=0;
	public String locoLabel="";
	
	@Override
	public void updateEntity(){
		changeOpStatus(false);
		RoWLocomotive locomotive = (RoWLocomotive) getNearbyStock(RoWLocomotive.class, range);
		if(locomotive==null){return;}
		
		if(locomotive.reverse != 0){return;}
		if(!this.locoLabel.equals("") && !locomotive.label.equals(this.locoLabel)){return;}
		changeOpStatus(true);
		if(!locomotive.isBrakeOn){
			locomotive.isBrakeOn = true;
			unmountEntities();
			if(whistleMode==2 || whistleMode==4){blowWhistle(locomotive);}
		}
		if(opMode==1){
			++ticks;
			if(ticks<tickDelay){return;}
		}
		if(opMode==2 && (worldObj.getWorldTime()%24000) != scheduledTime){return;}
		if(opMode==3 && (this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)==0)){return;}
		
		locomotive.reverse = reverseSet;
		locomotive.isBrakeOn = false;
		mountEntities();
		if(whistleMode==3 || whistleMode==4){blowWhistle(locomotive);}
		changeOpStatus(false);
		ticks=1;
	}
	
	private void blowWhistle(Entity locomotive){
		String whistleName="";
		if(locomotive.getClass().getName().contains("Ov")){whistleName="row:whistle_ov";}
		if(locomotive.getClass().getName().contains("Yer")){whistleName="row:whistle_yer";}
		if(locomotive.getClass().getName().contains("Cher")){whistleName="row:whistle_cher";}
		worldObj.playSound(locomotive.posX, locomotive.posY, locomotive.posZ, whistleName, whistleVolume, whistlePitch, true);
	}
	
	private void mountEntities(){
		List stockList = getAllNearbyStock(RoWRollingStock.class, stationCartRange);
		for(int i=0; i<stockList.size(); ++i){
			Entity stock=(Entity) stockList.get(i);
			if(stock.riddenByEntity==null && isValidMountingStock(stock, loadingOps)){
				if(stock.motionX<1 && stock.motionZ<1){
					double[] mountingOffset = getMountingOffset(stock, loadingOps);
					AxisAlignedBB loadingArea;
					if(mountingOffset[0]==0){
						loadingArea=stock.boundingBox.expand(5, 5, 5);
					}else{
						loadingArea=stock.boundingBox.offset(mountingOffset[0], mountingOffset[1], mountingOffset[2]).expand(2, 2, 2);
					}
					double testDistance;
					double shortestDistance = 10;
					Entity closestEntity = null;
					List entityList = this.worldObj.getEntitiesWithinAABBExcludingEntity(stock, loadingArea);
					for(int j=0; j<entityList.size(); ++j){
						Entity testEntity = (Entity) entityList.get(j);
						if(isValidMountingEntity(testEntity, loadingOps)){
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
		List stockList = getAllNearbyStock(RoWRollingStock.class, stationCartRange);
		for(int i=0; i<stockList.size(); ++i){
			Entity stock=(Entity) stockList.get(i);
			if(stock.motionX<10 && stock.motionZ<10 && stock.riddenByEntity!=null){
				if(isValidMountingStock(stock, unloadingOps) && isValidMountingEntity(stock.riddenByEntity, unloadingOps)){
					stock.riddenByEntity.yOffset=0.0F;
					stock.riddenByEntity.mountEntity(null);
					mountingOffset=getMountingOffset(stock, unloadingOps);
					stock.riddenByEntity.moveEntity(mountingOffset[0], mountingOffset[1], mountingOffset[2]);
					stock.riddenByEntity.fallDistance=0.0F;
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
    		return stock instanceof RoWLocomotive;
    	}else if(ops%100>=20){
    		return stock instanceof CartIII2L12;
    	}else if(ops%100>=10){
    		return stock instanceof CartNTV;
    	}else if(ops%100>=0){
    		return stock instanceof RoWLocomotive || stock instanceof CartIII2L12 || stock instanceof CartNTV;
    	}else{
    		return false;
    	}
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
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.opMode=tagcompound.getInteger("opMode");
	    this.ticks=tagcompound.getInteger("ticks");
	    this.tickDelay=tagcompound.getInteger("tickDelay");
	    this.reverseSet=tagcompound.getInteger("reverseSet");
	    this.whistleMode=tagcompound.getInteger("whistleMode");
	    this.loadingOps=tagcompound.getInteger("loadingOps");
	    this.unloadingOps=tagcompound.getInteger("unloadingOps");
	    this.whistleVolume=tagcompound.getFloat("whistleVolume");
	    this.whistlePitch=tagcompound.getFloat("whistlePitch");
	    this.scheduledTime=tagcompound.getLong("scheduledTime");
	    this.locoLabel = tagcompound.getString("locoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setInteger("opMode", this.opMode);
		tagcompound.setInteger("ticks", this.ticks);
		tagcompound.setInteger("tickDelay", this.tickDelay);
		tagcompound.setInteger("reverseSet", this.reverseSet);
		tagcompound.setInteger("whistleMode", this.whistleMode);
		tagcompound.setInteger("loadingOps", this.loadingOps);
		tagcompound.setInteger("unloadingOps", this.unloadingOps);
		tagcompound.setFloat("whistleVolume", this.whistleVolume);
		tagcompound.setFloat("whistlePitch", this.whistlePitch);
		tagcompound.setLong("scheduledTime",this.scheduledTime);
		tagcompound.setString("locoLabel", this.locoLabel);
	}
}