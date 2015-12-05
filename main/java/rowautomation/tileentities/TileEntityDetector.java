package rowautomation.tileentities;


public class TileEntityDetector extends TileEntityBase{
	public void updateEntity(){
		if(getNearbyStock("", DetectorRange)!=null){
			changeOpStatus(true);
		}else{
			changeOpStatus(false);
		}
	}	
}
