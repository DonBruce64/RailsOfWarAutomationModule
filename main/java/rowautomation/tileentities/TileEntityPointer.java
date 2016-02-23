package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.row.stock.core.RoWLocomotive;
import net.row.tileentity.TileEntityTrackNormal;

public class TileEntityPointer extends TileEntityBase{
	public Boolean locked=true;
	public Boolean redstone=false;
	public Boolean spring=false;
	public Boolean switched=false;
	public String locoLabel="";
	
	@Override
	public void updateEntity(){
		if(redstone){
			if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){
				changePointer(true);
			}else{
				changePointer(false);
			}
			return;
		}else if(spring){
			if(switched){
				changePointer(true);
			}else{
				changePointer(false);
			}
			return;
		}
		if(locoLabel==""){return;}
		RoWLocomotive loco = (RoWLocomotive) getNearbyStock(RoWLocomotive.class,range);
		if(loco==null){return;}
		if(loco.label.equals(this.locoLabel) && !(loco.onActiveSwitch==this.switched)){
			loco.onActiveSwitch = this.switched;
		}
		if(!loco.label.equals(this.locoLabel) && loco.onActiveSwitch==this.switched && locked){
			loco.onActiveSwitch = !this.switched;
		}
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.locked=tagcompound.getBoolean("locked");
	    this.redstone=tagcompound.getBoolean("redstone");
	    this.spring=tagcompound.getBoolean("spring");
	    this.switched=tagcompound.getBoolean("switched");
	    this.locoLabel=tagcompound.getString("locoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("locked", this.locked);
		tagcompound.setBoolean("redstone", this.redstone);
		tagcompound.setBoolean("spring", this.spring);
		tagcompound.setBoolean("switched", this.switched);
		tagcompound.setString("locoLabel", this.locoLabel);
	}
	
	private void changePointer(boolean switchStatus){
		for(int i=0;i<worldObj.loadedTileEntityList.size();++i){
			if(worldObj.loadedTileEntityList.get(i) instanceof TileEntityTrackNormal){
				TileEntityTrackNormal track=(TileEntityTrackNormal) worldObj.loadedTileEntityList.get(i);
				if(Math.abs(track.xCoord-this.xCoord)<=range && Math.abs(track.yCoord-this.yCoord)<=range && Math.abs(track.zCoord-this.zCoord)<=range){
					if((track.type>=15 && track.type<=20)||(track.type>=27 && track.type<=32)){
						if(!track.activated && switchStatus){
							track.activated = true;
						}else if(track.activated && !switchStatus){
							track.activated = false;
						}
					}					
				}
			}
		}
	}
}