package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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
		Entity locomotive=getNearbyStock("loco",range);
		if(locomotive==null){return;}
		NBTTagCompound locomotiveNBT=getStockNBT(locomotive);
		if(locomotiveNBT==null){return;}
		if(locomotiveNBT.getString("label").equals(this.locoLabel) && !(locomotiveNBT.getBoolean("switched")==this.switched)){
			locomotiveNBT.setBoolean("switched", this.switched);
			setStockNBT(locomotive, locomotiveNBT);
		}
		if(!locomotiveNBT.getString("label").equals(this.locoLabel) && locomotiveNBT.getBoolean("switched")==this.switched && locked){
			locomotiveNBT.setBoolean("switched", !this.switched);
			setStockNBT(locomotive, locomotiveNBT);
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
			if(worldObj.loadedTileEntityList.get(i).getClass().getName().equals("net.row.tileentity.TileEntityTrackNormal")){
				TileEntity track=(TileEntity) worldObj.loadedTileEntityList.get(i);
				NBTTagCompound trackNBTTag=getStockNBT(track);
				if(Math.abs(track.xCoord-this.xCoord)<=range && Math.abs(track.yCoord-this.yCoord)<=range && Math.abs(track.zCoord-this.zCoord)<=range){
					int mid=trackNBTTag.getInteger("mid");
					if((mid>=15 && mid<=20)||(mid>=27 && mid <=32)){
						if(!trackNBTTag.getBoolean("act") && switchStatus){
							trackNBTTag.setBoolean("act",true);
							setStockNBT(track,trackNBTTag);
							return;
						}
						if(trackNBTTag.getBoolean("act") && !switchStatus){
							trackNBTTag.setBoolean("act",false);
							setStockNBT(track,trackNBTTag);
							return;
						}
					}					
				}
			}
		}
	}
}