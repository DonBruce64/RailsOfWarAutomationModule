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
	public boolean Drain=true;
	private boolean FoundTank=false;
	private int TankCartAmount;
	private int TankCartFluidColor;
	private FluidStack TankCartFluid;
	private Entity TankCart;
	private NBTTagCompound TankCartNBT = new NBTTagCompound();
	private IFluidHandler tankContainer;
	private ForgeDirection TankDirection = null;

	@Override
	public void updateEntity(){
		if(this.worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord)>0){return;}
		FoundTank=false;
		changeOpStatus(false);
		for(int i=0;i<6;++i){
			TankDirection=ForgeDirection.getOrientation(i);
			TileEntity testTileEntity=this.worldObj.getTileEntity(this.xCoord+TankDirection.offsetX,this.yCoord+TankDirection.offsetY,this.zCoord+TankDirection.offsetZ);
			if(testTileEntity!=null){
				if(IFluidHandler.class.isAssignableFrom(testTileEntity.getClass())){
					tankContainer=(IFluidHandler) testTileEntity;
					FoundTank=true;
					break;
				}
			}
		}
		TankCart=getNearbyStock("cart.CartNT", Range);
		if(TankCart==null || !FoundTank){return;}
		TankCartNBT=this.getStockNBT(TankCart);  
		TankCartAmount=TankCartNBT.getInteger("PortalCooldown");
		TankCartFluidColor=TankCartNBT.getInteger("colour");
		TankCartFluid=getTankCartFluid(TankCartFluidColor, TankCartAmount);
		
		if(this.Drain){//drain tank cart
			if(TankCartAmount==0 || TankCartFluid==null){return;}
			if(tankContainer.canFill(TankDirection.getOpposite(), TankCartFluid.getFluid())){
				tankContainer.fill(TankDirection.getOpposite(), TankCartFluid, true);
				TankCartNBT.setInteger("PortalCooldown", TankCartAmount-TankCartFluid.amount);
			}else{
				return;
			}
		}else{//fill tank cart
			if(TankCartAmount>=96000){return;}
			if(TankCartFluid==null){//empty tank cart
				TankCartFluid=tankContainer.drain(TankDirection.getOpposite(), 1, false);
				if(TankCartFluid==null){return;}
				TankCartFluidColor=getFluidColor(TankCartFluid);
				if(TankCartFluidColor==0){return;}
				TankCartNBT.setInteger("colour",TankCartFluidColor);
			}
			if(tankContainer.canDrain(TankDirection.getOpposite(), TankCartFluid.getFluid())){
				TankCartFluid=tankContainer.drain(TankDirection.getOpposite(), 100, true);
				if(TankCartFluid==null){return;}
				TankCartNBT.setInteger("PortalCooldown", TankCartAmount+TankCartFluid.amount);
			}else{
				return;
			}
		}
		setStockNBT(TankCart, TankCartNBT);
		changeOpStatus(true);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagcompound){
		super.readFromNBT(tagcompound);
	    this.FinishedOperation=tagcompound.getBoolean("FinishedOperation");
	    this.Drain=tagcompound.getBoolean("Drain");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagcompound){
		super.writeToNBT(tagcompound);
		tagcompound.setBoolean("FinishedOperation", this.FinishedOperation);
		tagcompound.setBoolean("Drain", this.Drain);
	}
	
	public FluidStack getTankCartFluid(int TankCartColor, int TankCartAmount){
		if(TankCartColor==2437522){return new FluidStack(new Fluid("water"), Math.min(TankCartAmount,100));}
		if(TankCartColor==16711680){return new FluidStack(new Fluid("lava"), Math.min(TankCartAmount,100));}
		if(TankCartColor==3158064 && FluidRegistry.isFluidRegistered(new Fluid("oil"))){
			return new FluidStack(new Fluid("oil"), Math.min(TankCartAmount,100));
		}
		if(TankCartColor==14602026 && FluidRegistry.isFluidRegistered(new Fluid("fuel"))){
			return new FluidStack(new Fluid("fuel"), Math.min(TankCartAmount,100));
		}
		return null;
	}
	
	public int getFluidColor(FluidStack TankFluid){
		if(TankFluid==null){return 0;}
		if(TankFluid.getLocalizedName().equals("Water")){return 2437522;}
		if(TankFluid.getLocalizedName().equals("Lava")){return 16711680;}
		if(TankFluid.getLocalizedName().equals("Oil")){return 3158064;}
		if(TankFluid.getLocalizedName().equals("Fuel")){return 14602026;}
		return 0;
	}
}