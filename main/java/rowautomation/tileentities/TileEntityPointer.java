package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.row.stock.core.RoWLocomotive;
import net.row.stock.core.RoWRollingStock;
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
			if(getAllNearbyStock(RoWRollingStock.class, range).isEmpty()){
				if(switched){
					changePointer(true);
				}else{
					changePointer(false);
				}
			}
			return;
		}
		if(locoLabel==""){return;}
		RoWLocomotive loco = (RoWLocomotive) getNearbyStock(RoWLocomotive.class, range);
		if(loco==null){return;}
		if(loco.label.equals(this.locoLabel) && !(loco.onActiveSwitch==this.switched)){
			loco.onActiveSwitch = this.switched;
			changePointer(this.switched);
		}
		if(!loco.label.equals(this.locoLabel) && loco.onActiveSwitch==this.switched && locked){
			loco.onActiveSwitch = !this.switched;
			changePointer(!this.switched);
		}
	}
	
	private void changePointer(boolean switchStatus){
		for(int i=-5;i<=5;++i){
			for(int j=-5;j<=5;++j){
				for(int k=-5;k<=5;++k){
					TileEntity tile = worldObj.getTileEntity(this.xCoord + i, this.yCoord + j, this.zCoord + k);
					if(tile instanceof TileEntityTrackNormal){
						TileEntityTrackNormal track=(TileEntityTrackNormal) tile;
						if((track.type>=15 && track.type<=20)||(track.type>=27 && track.type<=32)){
							if(!track.activated && switchStatus){
								track.activated = true;
							}else if(track.activated && !switchStatus){
								track.activated = false;
							}
							return;
						}
					}
				}
			}
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
}