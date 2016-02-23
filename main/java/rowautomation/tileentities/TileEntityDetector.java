package rowautomation.tileentities;

import net.row.stock.core.RoWRollingStock;


public class TileEntityDetector extends TileEntityBase{
	public void updateEntity(){
		if(!getAllNearbyStock(RoWRollingStock.class, detectorRange).isEmpty()){
			changeOpStatus(true);
		}else{
			changeOpStatus(false);
		}
	}	
}
