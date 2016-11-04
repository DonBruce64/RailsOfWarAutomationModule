package rowautomation.tileentities;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.row.stock.cart.CartCherepanovTub;

public class TileEntityFreight extends TileEntityBase{
	public boolean load=true;
	private int transferCooldown=0;

	@Override
	public void updateEntity(){
		if(!worldObj.isRemote){
			if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
			if(transferCooldown>8){
				transferCooldown=0;
			}else{
				++transferCooldown;
				return;
			}
			
			boolean foundChest = false;
			changeOpStatus(false);
			IInventory tubInventory = (CartCherepanovTub) getNearbyStock(CartCherepanovTub.class, range);
			if(tubInventory == null){return;}
			for(int i=0;i<6;++i){
				ForgeDirection chestDirection = ForgeDirection.getOrientation(i);
				TileEntity testTileEntity = worldObj.getTileEntity(xCoord+chestDirection.offsetX, yCoord+chestDirection.offsetY, zCoord+chestDirection.offsetZ);
				if(testTileEntity!=null){
					if(IInventory.class.isAssignableFrom(testTileEntity.getClass())){
						IInventory chest = (IInventory) testTileEntity;
						if(this.load){
							for(int j=0; j<chest.getSizeInventory(); ++j){
								ItemStack chestStack = chest.getStackInSlot(j);
								if(chestStack == null){continue;}
								for(int k=0; k<tubInventory.getSizeInventory(); ++k){
									ItemStack tubStack = tubInventory.getStackInSlot(k);
									if(tubStack != null){
										if(tubStack.isItemEqual(chestStack)){
											if(tubStack.getMaxStackSize() - tubStack.stackSize > 0){
												++tubStack.stackSize;
												--chestStack.stackSize;
												if(chestStack.stackSize == 0){
													chest.setInventorySlotContents(j, null);
												}
												changeOpStatus(true);
												return;
											}
										}
									}else{
										if(chestStack.getItem() instanceof ItemBlock){
											tubInventory.setInventorySlotContents(k, new ItemStack(chestStack.getItem(), 1, chestStack.getItemDamage()));
											--chestStack.stackSize;
											changeOpStatus(true);
											return;
										}
									}
								}
							}
						}else{
							for(int j=0; j<tubInventory.getSizeInventory(); ++j){
								ItemStack tubStack = tubInventory.getStackInSlot(j);
								if(tubStack == null){continue;}
								for(int k=0; k<chest.getSizeInventory(); ++k){
									ItemStack chestStack = chest.getStackInSlot(k);
									if(chestStack != null){
										if(chestStack.isItemEqual(tubStack)){
											if(chestStack.getMaxStackSize() - chestStack.stackSize > 0){
												++chestStack.stackSize;
												--tubStack.stackSize;
												if(tubStack.stackSize == 0){
													tubInventory.setInventorySlotContents(j, null);
												}
												changeOpStatus(true);
												return;
											}
										}
									}else{
										if(tubStack.getItem() instanceof ItemBlock){
											chest.setInventorySlotContents(k, new ItemStack(tubStack.getItem(), 1, tubStack.getItemDamage()));
											--tubStack.stackSize;
											changeOpStatus(true);
											return;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.load=tagcompound.getBoolean("load");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("load", this.load);
	}
}