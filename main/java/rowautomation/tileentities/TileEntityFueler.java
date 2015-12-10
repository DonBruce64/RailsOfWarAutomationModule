package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFueler extends TileEntityBase{
	private int transferCooldown=0;
	
	@Override
	public void updateEntity(){
		changeOpStatus(false);
		Entity stock = getFuelableStock();
		if(stock==null){return;}
		NBTTagCompound stockNBT = getStockNBT(stock);
		if(stockNBT==null){return;}
		int currentFuel = stockNBT.getInteger("fuel");
		int maxFuel = 0;
		boolean tender = false;
		if(stock.getClass().getName().equals("net.row.stock.tender.TenderOv")){
			maxFuel=5735;
			tender=true;
		}else{
			if(stock.getClass().getName().equals("net.row.stock.loco.LocoCherepanov")){maxFuel=2500;}
			if(stock.getClass().getName().equals("net.row.stock.loco.LocoYer")){maxFuel=10000;}
			if(stock.getClass().getName().equals("net.row.stock.loco.LocoOv")){maxFuel=5000;}
		}
		++transferCooldown;
		if(getBlockMetadata()==0){//not in creative mode
			if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
			if(currentFuel+80>maxFuel){return;}
			TileEntityChest chest = getNearbyChest();
			if(chest==null){return;}
			for(int i=0;i<chest.getSizeInventory();i++){
				ItemStack chestItemStack = chest.getStackInSlot(i);
				if(chestItemStack==null){continue;}
				if(chestItemStack.getItem().getClass().equals(net.minecraft.item.ItemCoal.class)){
					changeOpStatus(true);
					if(transferCooldown>8){
						transferCooldown=0;
						chestItemStack=chest.decrStackSize(i, 1);
						fuelStock(false, tender, stock, stockNBT, maxFuel, currentFuel);
					}
					break;
				}
			}
		}else{
			if(tender && transferCooldown<8){return;}
			transferCooldown=0;
			fuelStock(true, tender, stock, stockNBT, maxFuel, currentFuel);
		}
	}
	
	public Entity getFuelableStock(){
		Entity stock=getNearbyStock("loco",range);
		if(stock==null){
			stock=getNearbyStock("tender",range);
		}
		return stock;
	}
	
	private TileEntityChest getNearbyChest(){
		///if(IFluidHandler.class.isAssignableFrom(testTileEntity.getClass())){
		for(int i=0;i<6;++i){
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			TileEntity tile = worldObj.getTileEntity(xCoord+direction.offsetX, yCoord+direction.offsetY, zCoord+direction.offsetZ);
			if(tile instanceof TileEntityChest){
				return (TileEntityChest) tile;
			}
		}
		return null;
	}
	
	private void fuelStock(boolean creative, boolean tender, Entity stock, NBTTagCompound stockNBT, int maxFuel, int currentFuel){
		if(tender){
			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, stock.posX, stock.posY+3, stock.posZ, new ItemStack(Items.coal, creative ? (maxFuel-currentFuel)/10 : 8)));			
		}else{
			stockNBT.setInteger("fuel", creative ? maxFuel : currentFuel+80);
			setStockNBT(stock, stockNBT);
		}
	}
}
