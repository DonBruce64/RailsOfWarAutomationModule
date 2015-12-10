package rowautomation.tileentities;


public class TileEntityDetector extends TileEntityBase{
	public void updateEntity(){
		if(getNearbyStock("", detectorRange)!=null){
			changeOpStatus(true);
		}else{
			changeOpStatus(false);
		}
	}	
}
