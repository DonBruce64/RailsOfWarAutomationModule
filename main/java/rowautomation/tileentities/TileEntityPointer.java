package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPointer extends TileEntityBase{
	public Boolean Locked=true;
	public Boolean Redstone=false;
	public Boolean Spring=false;
	public Boolean Switched=false;
	public String LocoLabel="";
	private Entity Locomotive;
	private NBTTagCompound LocomotiveNBT;
	
	@Override
	public void updateEntity(){
		if(this.Redstone){
			if(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)>0){
				changePointer(true);
			}else{
				changePointer(false);
			}
			return;
		}else if(this.Spring){
			if(Switched){
				changePointer(true);
			}else{
				changePointer(false);
			}
			return;
		}
		if(LocoLabel==""){return;}
		Locomotive=getNearbyStock("loco",Range);
		if(Locomotive==null){return;}
		LocomotiveNBT=getStockNBT(Locomotive);
		if(LocomotiveNBT==null){return;}
		if(LocomotiveNBT.getString("label").equals(this.LocoLabel) && !(LocomotiveNBT.getBoolean("switched")==this.Switched)){
			LocomotiveNBT.setBoolean("switched", this.Switched);
			setStockNBT(Locomotive, LocomotiveNBT);
		}
		if(!LocomotiveNBT.getString("label").equals(this.LocoLabel) && LocomotiveNBT.getBoolean("switched")==this.Switched && Locked){
			LocomotiveNBT.setBoolean("switched", !this.Switched);
			setStockNBT(Locomotive, LocomotiveNBT);
		}
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.Locked=tagcompound.getBoolean("Locked");
	    this.Redstone=tagcompound.getBoolean("Redstone");
	    this.Spring=tagcompound.getBoolean("Spring");
	    this.Switched=tagcompound.getBoolean("Switched");
	    this.LocoLabel=tagcompound.getString("LocoLabel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("Locked", this.Locked);
		tagcompound.setBoolean("Redstone", this.Redstone);
		tagcompound.setBoolean("Spring", this.Spring);
		tagcompound.setBoolean("Switched", this.Switched);
		tagcompound.setString("LocoLabel", this.LocoLabel);
	}
	
	private void changePointer(boolean switchStatus){
		for(int i=0;i<this.worldObj.loadedTileEntityList.size();++i){
			if(this.worldObj.loadedTileEntityList.get(i).getClass().getName().equals("net.row.tileentity.TileEntityTrackNormal")){
				TileEntity Track=(TileEntity) this.worldObj.loadedTileEntityList.get(i);
				NBTTagCompound TrackNBTTag=getStockNBT(Track);
				if(Math.abs(Track.xCoord-this.xCoord)<=Range && Math.abs(Track.yCoord-this.yCoord)<=Range && Math.abs(Track.zCoord-this.zCoord)<=Range){
					int Mid=TrackNBTTag.getInteger("mid");
					if((Mid>=15 && Mid<=20)||(Mid>=27 && Mid <=32)){
						if(!TrackNBTTag.getBoolean("act") && switchStatus){
							TrackNBTTag.setBoolean("act",true);
							setStockNBT(Track,TrackNBTTag);
							return;
						}
						if(TrackNBTTag.getBoolean("act") && !switchStatus){
							TrackNBTTag.setBoolean("act",false);
							setStockNBT(Track,TrackNBTTag);
							return;
						}
					}					
				}
			}
		}
	}
}