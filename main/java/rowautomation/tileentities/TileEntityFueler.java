package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.row.registry.RoWConfig;
import net.row.stock.core.RoWLocomotive;
import net.row.stock.core.RoWRollingStock;
import net.row.stock.tender.TenderBase;

public class TileEntityFueler extends TileEntityBase{
	private int transferCooldown=0;
	
	@Override
	public void updateEntity(){
		changeOpStatus(false);
		RoWRollingStock stock = (RoWRollingStock) getFuelableStock();
		if(stock==null){return;}
		if(stock.fuel + RoWConfig.fuelValue < stock.maxFuel){
			if(getBlockMetadata()==0){//not in creative mode
				if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
				IInventory chest = getNearbyChest();
				if(chest==null){return;}
				for(int i=0;i<chest.getSizeInventory();i++){
					ItemStack chestItemStack = chest.getStackInSlot(i);
					if(chestItemStack==null){continue;}
					if(chestItemStack.getItem().getClass().equals(net.minecraft.item.ItemCoal.class)){
						changeOpStatus(true);
						if(transferCooldown>8){
							transferCooldown=0;
							chestItemStack=chest.decrStackSize(i, 1);
							stock.fuel += RoWConfig.fuelValue;
						}else{
							++transferCooldown;
						}
						break;
					}
				}
			}else{
				stock.fuel = stock.maxFuel;
			}
		}
	}
	
	public Entity getFuelableStock(){
		Entity stock=getNearbyStock(RoWLocomotive.class, range);
		if(stock==null){
			stock=getNearbyStock(TenderBase.class, range);
		}
		return stock;
	}
	
	private IInventory getNearbyChest(){
		for(int i=0;i<6;++i){
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
			if(tile != null){
				if(IInventory.class.isAssignableFrom(tile.getClass())){
					return (IInventory) tile;
				}
			}
		}
		return null;
	}
}
