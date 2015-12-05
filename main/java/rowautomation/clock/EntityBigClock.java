package rowautomation.clock;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.world.World;

public class EntityBigClock extends EntityHanging{
	
	public EntityBigClock(World world){
		super(world);
		this.setSize(1.0F, 1.0F);
	}
	
	public EntityBigClock(World world, int x, int y, int z, int side){
		super(world, x, y, z, side);
		this.setDirection(side);
	}
	
	public int getWidthPixels(){return 32;}
	public int getHeightPixels(){return 32;}
	public void onBroken(Entity p_110128_1_){}
}
