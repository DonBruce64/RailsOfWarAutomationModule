package rowautomation.tileentities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityTank extends TileEntityBase{
	public boolean drain=true;

	@Override
	public void updateEntity(){
		if(worldObj.getBlockPowerInput(xCoord, yCoord, zCoord)>0){return;}
		boolean foundTank = false;
		changeOpStatus(false);
		for(int i=0;i<6;++i){
			ForgeDirection tankDirection = ForgeDirection.getOrientation(i);
			TileEntity testTileEntity = worldObj.getTileEntity(xCoord+tankDirection.offsetX, yCoord+tankDirection.offsetY, zCoord+tankDirection.offsetZ);
			if(testTileEntity!=null){
				if(IFluidHandler.class.isAssignableFrom(testTileEntity.getClass())){
					IFluidHandler tankContainer = (IFluidHandler) testTileEntity;
					Entity tankCart = getNearbyStock("cart.CartNT", range);
					if(tankCart==null){return;}
					NBTTagCompound tankCartNBT = this.getStockNBT(tankCart);
					int tankCartAmount = tankCartNBT.getInteger("PortalCooldown");
					int tankCartFluidColor = tankCartNBT.getInteger("colour");
					FluidStack tankCartFluid = getTankCartFluid(tankCartFluidColor, tankCartAmount);
					if(this.drain){//drain tank cart
						if(tankCartAmount==0 || tankCartFluid==null){return;}
						if(tankContainer.canFill(tankDirection.getOpposite(), tankCartFluid.getFluid())){
							tankContainer.fill(tankDirection.getOpposite(), tankCartFluid, true);
							tankCartNBT.setInteger("PortalCooldown", tankCartAmount-tankCartFluid.amount);
						}else{
							return;
						}
					}else{//fill tank cart
						if(tankCartAmount>=96000){return;}
						if(tankCartFluid==null){//empty tank cart
							tankCartFluid=tankContainer.drain(tankDirection.getOpposite(), 1, false);
							if(tankCartFluid==null){return;}
							tankCartFluidColor=getFluidColor(tankCartFluid);
							if(tankCartFluidColor==0){return;}
							tankCartNBT.setInteger("colour",tankCartFluidColor);
						}
						if(tankContainer.canDrain(tankDirection.getOpposite(), tankCartFluid.getFluid())){
							tankCartFluid=tankContainer.drain(tankDirection.getOpposite(), 100, true);
							if(tankCartFluid==null){return;}
							tankCartNBT.setInteger("PortalCooldown", tankCartAmount+tankCartFluid.amount);
						}else{
							return;
						}
					}
					setStockNBT(tankCart, tankCartNBT);
					changeOpStatus(true);
				}
			}
		}
	}
	
	public FluidStack getTankCartFluid(int tankCartColor, int tankCartAmount){
		if(tankCartColor==2437522){return new FluidStack(new Fluid("water"), Math.min(tankCartAmount,100));}
		if(tankCartColor==16711680){return new FluidStack(new Fluid("lava"), Math.min(tankCartAmount,100));}
		if(tankCartColor==3158064 && FluidRegistry.isFluidRegistered(new Fluid("oil"))){
			return new FluidStack(new Fluid("oil"), Math.min(tankCartAmount,100));
		}
		if(tankCartColor==14602026 && FluidRegistry.isFluidRegistered(new Fluid("fuel"))){
			return new FluidStack(new Fluid("fuel"), Math.min(tankCartAmount,100));
		}
		return null;
	}
	
	public int getFluidColor(FluidStack tankFluid){
		if(tankFluid==null){return 0;}
		if(tankFluid.getLocalizedName().equals("Water")){return 2437522;}
		if(tankFluid.getLocalizedName().equals("Lava")){return 16711680;}
		if(tankFluid.getLocalizedName().equals("Oil")){return 3158064;}
		if(tankFluid.getLocalizedName().equals("Fuel")){return 14602026;}
		return 0;
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