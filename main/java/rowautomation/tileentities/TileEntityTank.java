package rowautomation.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.row.RoW;
import net.row.network.PacketFuel;
import net.row.stock.cart.CartNT;

public class TileEntityTank extends TileEntityBase{
	public boolean drain=true;

	@Override
	public void updateEntity(){
		if(!worldObj.isRemote){
			if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
			boolean foundTank = false;
			changeOpStatus(false);
			for(int i=0;i<6;++i){
				ForgeDirection tankDirection = ForgeDirection.getOrientation(i);
				TileEntity testTileEntity = worldObj.getTileEntity(xCoord+tankDirection.offsetX, yCoord+tankDirection.offsetY, zCoord+tankDirection.offsetZ);
				if(testTileEntity!=null){
					if(IFluidHandler.class.isAssignableFrom(testTileEntity.getClass())){
						IFluidHandler tankContainer = (IFluidHandler) testTileEntity;
						CartNT tankCart = (CartNT) getNearbyStock(CartNT.class, range);
						if(tankCart==null){return;}
						
						if(this.drain){//drain tank cart					
							if(tankCart.fuel==0){return;}
							FluidStack fluidToDrain = new FluidStack(tankCart.fuelType,  Math.min(tankCart.fuel, 100));
							if(tankContainer.canFill(tankDirection.getOpposite(), fluidToDrain.getFluid())){
								tankContainer.fill(tankDirection.getOpposite(), fluidToDrain, true);
								tankCart.fuel -= fluidToDrain.amount;
							}else{
								return;
							}
						}else{//fill tank cart
							if(tankCart.fuel>=96000){return;}
							FluidStack tankFluidData = tankContainer.drain(tankDirection.getOpposite(), 1, false);
							if(tankFluidData == null){
								return;
							}else if(tankCart.fuel == 0){//empty tank cart
								this.setCartColor(tankFluidData.getFluid(), tankCart);
								tankCart.fuelType = tankFluidData.fluidID;
							}else if(tankCart.fuelType != tankFluidData.fluidID){
								return;
							}
							FluidStack fluidToFill = new FluidStack(tankCart.fuelType, Math.min(96000 - tankCart.fuel, 100));
	
							if(tankContainer.canDrain(tankDirection.getOpposite(), fluidToFill.getFluid())){
								fluidToFill = tankContainer.drain(tankDirection.getOpposite(), fluidToFill.amount, true);
								if(fluidToFill == null){
									return;
								}else{
									tankCart.fuel += fluidToFill.amount;
									RoW.network.sendToAll(new PacketFuel(tankCart.getEntityId(), tankCart.fuel));
								}
							}else{
								return;
							}
						}
						changeOpStatus(true);
					}
				}
			}
		}
	}
	
	private void setCartColor(Fluid tankFluid, CartNT cart){
		if(tankFluid.getName().equals("water")){
			cart.colour = 2437522;
		}else if(tankFluid.getName().equals("lava")){
			cart.colour = 16711680;
		}else if(tankFluid.getName().equals("oil")){
			cart.colour = 3158064;
		}else if(tankFluid.getName().equals("fuel")){
			cart.colour = 14602026;
		}else{
			cart.colour = tankFluid.getColor();
		}
		cart.sendUpdateToClient();
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.drain=tagcompound.getBoolean("drain");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("drain", this.drain);
	}
}