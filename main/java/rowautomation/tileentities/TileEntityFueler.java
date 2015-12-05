package rowautomation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityFueler extends TileEntityBase{
	private boolean tender;
	private int MaxFuel;
	private int CurrentFuel;
	private int TransferCooldown=0;
	private double tenderX;
	private double tenderY;
	private double tenderZ;
	private Entity Stock;
	private TileEntityChest Chest;
	private NBTTagCompound StockNBT;
	private ItemStack ChestItemStack;
	
	@Override
	public void updateEntity(){
		changeOpStatus(false);
		Stock=getFuelableStock();
		if(Stock==null){return;}
		StockNBT=getStockNBT(Stock);
		if(StockNBT==null){return;}
		CurrentFuel=StockNBT.getInteger("fuel");
		MaxFuel=0;
		if(Stock.getClass().getName().equals("net.row.stock.tender.TenderOv")){
			MaxFuel=5735;
			tender=true;
		}else{
			if(Stock.getClass().getName().equals("net.row.stock.loco.LocoCherepanov")){MaxFuel=2500;}
			if(Stock.getClass().getName().equals("net.row.stock.loco.LocoYer")){MaxFuel=10000;}
			if(Stock.getClass().getName().equals("net.row.stock.loco.LocoOv")){MaxFuel=5000;}
			tender=false;
		}
		++TransferCooldown;
		if(this.getBlockMetadata()==0){//not in creative mode
			if(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)>0){return;}
			if(CurrentFuel+80>MaxFuel){return;}
			Chest=getNearbyChest();
			if(Chest==null){return;}
			for(int i=0;i<Chest.getSizeInventory();i++){
				ChestItemStack=Chest.getStackInSlot(i);
				if(ChestItemStack==null){continue;}
				if(ChestItemStack.getItem().getClass().equals(net.minecraft.item.ItemCoal.class)){
					changeOpStatus(true);
					if(TransferCooldown>=8){
						TransferCooldown=0;
						ChestItemStack=Chest.decrStackSize(i, 1);
						fuelStock(false);
					}
					break;
				}
			}
		}else{
			if(tender && TransferCooldown<8){return;}
			TransferCooldown=0;
			fuelStock(true);
		}
	}
	
	public Entity getFuelableStock(){
		Entity Stock=getNearbyStock("loco",Range);
		if(Stock==null){
			Stock=getNearbyStock("tender",Range);
		}
		return Stock;
	}
	
	public TileEntityChest getNearbyChest(){
		Block NeighborBlock;
		TileEntityChest NeighborChest;
		ForgeDirection Direction;
		for(int i=0;i<6;++i){
			Direction=ForgeDirection.getOrientation(i);
			NeighborBlock=this.getWorldObj().getBlock(this.xCoord+Direction.offsetX,this.yCoord+Direction.offsetY,this.zCoord+Direction.offsetZ);
			if(NeighborBlock.getClass().equals(net.minecraft.block.BlockChest.class)){
				return (TileEntityChest) this.getWorldObj().getTileEntity(this.xCoord+Direction.offsetX,this.yCoord+Direction.offsetY,this.zCoord+Direction.offsetZ);
			}
		}
		return null;
	}
	
	public void fuelStock(boolean creative){
		if(tender){
			if(creative){
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, Stock.posX, Stock.posY+3, Stock.posZ, new ItemStack(Items.coal, (MaxFuel-CurrentFuel)/10)));
			}else{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, Stock.posX, Stock.posY+3, Stock.posZ, new ItemStack(Items.coal, 8)));
			}			
		}else{
			if(creative){
				StockNBT.setInteger("fuel", MaxFuel);
			}else{
				StockNBT.setInteger("fuel", CurrentFuel+80);
			}
			setStockNBT(Stock, StockNBT);
		}
	}
}
