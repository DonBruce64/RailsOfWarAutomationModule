package rowautomation.guis;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import rowautomation.ROWAM;
import rowautomation.network.PacketStation;
import rowautomation.tileentities.TileEntityStation;


public class GUIStation extends GuiScreen{
	private TileEntityStation thisTileEntity;
	private int opMode;
	private int reverseSet;
	private int tickDelay;
	private int whistleMode;
	private int loadingOps;
	private int unloadingOps;
	private float whistleVolume;
	private float whistlePitch;
	private long scheduledTime;
	private String locoLabel;

	private static final ResourceLocation background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiButton locoSettingsButton;
	private GuiButton opModeButton;
	private GuiButton whistleModeButton;
	private GuiButton whistleVolumeUpButton;
	private GuiButton whistleVolumeDownButton;
	private GuiButton whistlePitchUpButton;
	private GuiButton whistlePitchDownButton;
	private GuiTextField locoLabelBox;
	private GuiTextField opModeInfo;
	private GuiTextField tickDelayBox;
	private GuiTextField scheduledTimeBox;
	private GuiTextField reverseSetBox;
	private GuiTextField whistleVolumeBox;
	private GuiTextField whistlePitchBox;
	
	private GuiButton freightSettingsButton;
	private GuiButton loadingEntityButton;
	private GuiButton loadingStockButton;
	private GuiButton loadingDirectionButton;
	private GuiButton unloadingEntityButton;
	private GuiButton unloadingStockButton;
	private GuiButton unloadingDirectionButton;
;
	public GUIStation(TileEntityStation thisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.thisTileEntity=thisTileEntity;
		this.locoLabel=thisTileEntity.locoLabel;
		this.opMode=thisTileEntity.opMode;
		this.tickDelay=thisTileEntity.tickDelay;
		this.scheduledTime=thisTileEntity.scheduledTime;
		this.reverseSet=thisTileEntity.reverseSet;
		this.whistleMode=thisTileEntity.whistleMode;
		this.whistleVolume=thisTileEntity.whistleVolume;
		this.whistlePitch=thisTileEntity.whistlePitch;
		this.loadingOps=thisTileEntity.loadingOps;
		this.unloadingOps=thisTileEntity.unloadingOps;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	
    	locoSettingsButton = new GuiButton(1, x-5, y-20, 85, 20, "Loco Settings");
    	locoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	opModeButton = new GuiButton(1, x+80, y+30, 80, 20, "");
    	opModeInfo = new GuiTextField(fontRendererObj, x+5, y+60, 100, 15);
    	tickDelayBox = new GuiTextField(fontRendererObj, x+100, y+55, 60, 15);
    	scheduledTimeBox = new GuiTextField(fontRendererObj, x+100, y+55, 60, 15);
    	reverseSetBox = new GuiTextField(fontRendererObj, x+80, y+75, 80, 15);
    	whistleModeButton = new GuiButton(1, x+80, y+95, 80, 20, "");
    	whistleVolumeBox = new GuiTextField(fontRendererObj, x+55, y+120, 30, 20);
    	whistlePitchBox = new GuiTextField(fontRendererObj, x+120, y+120, 30, 20);
    	whistleVolumeUpButton = new GuiButton(1, x+25, y+135, 20, 20, "+");
    	whistleVolumeDownButton = new GuiButton(1, x+45, y+135, 20, 20, "-");
    	whistlePitchUpButton = new GuiButton(1, x+90, y+135, 20, 20, "+");
    	whistlePitchDownButton = new GuiButton(1, x+110, y+135, 20, 20, "-");
    	locoSettingsButton.enabled=false;
    	opModeInfo.setEnableBackgroundDrawing(false);
    	opModeInfo.setEnabled(false);
    	opModeInfo.setDisabledTextColour(Color.WHITE.getRGB());
    	tickDelayBox.setMaxStringLength(8);
    	scheduledTimeBox.setMaxStringLength(5);
    	reverseSetBox.setMaxStringLength(3);
    	whistleVolumeBox.setEnableBackgroundDrawing(false);
    	whistleVolumeBox.setEnabled(false);
    	whistleVolumeBox.setDisabledTextColour(Color.WHITE.getRGB());
    	whistlePitchBox.setEnableBackgroundDrawing(false);
    	whistlePitchBox.setEnabled(false);
    	whistlePitchBox.setDisabledTextColour(Color.WHITE.getRGB());
    	buttonList.add(locoSettingsButton);
    	buttonList.add(opModeButton);
    	buttonList.add(whistleModeButton);
    	buttonList.add(whistleVolumeUpButton);
    	buttonList.add(whistleVolumeDownButton);
    	buttonList.add(whistlePitchUpButton);
    	buttonList.add(whistlePitchDownButton);
    	locoLabelBox.setText(locoLabel);
    	tickDelayBox.setText(String.valueOf(tickDelay));
    	scheduledTimeBox.setText(String.valueOf(scheduledTime));
    	reverseSetBox.setText(String.valueOf(reverseSet));
    	
    	freightSettingsButton = new GuiButton(1, x+79, y-20, 91, 20, "Freight Settings");
    	loadingEntityButton = new GuiButton(1, x+5, y+20, 70, 20, "");
    	loadingStockButton = new GuiButton(1, x+5, y+60, 70, 20, "");
    	loadingDirectionButton = new GuiButton(1, x+5, y+100, 70, 20, "");
    	unloadingEntityButton = new GuiButton(1, x+90, y+20, 70, 20, "");
    	unloadingStockButton = new GuiButton(1, x+90, y+60, 70, 20, "");
    	unloadingDirectionButton = new GuiButton(1, x+90, y+100, 70, 20, "");
    	buttonList.add(freightSettingsButton);
    	buttonList.add(loadingEntityButton);
    	buttonList.add(loadingStockButton);
    	buttonList.add(loadingDirectionButton);
    	buttonList.add(unloadingEntityButton);
    	buttonList.add(unloadingStockButton);
    	buttonList.add(unloadingDirectionButton);
	}
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(background);
    	if(opMode==1){
    		opModeButton.displayString="Time Delay";
    		opModeInfo.setText("Delay (ticks):");
    	}else if(opMode==2){
    		opModeButton.displayString="Scheduled";
    		opModeInfo.setText("Departure time:");
    	}else{
    		opModeButton.displayString="Redstone";
    	}
    	if(whistleMode==1){
    		whistleModeButton.displayString="None";
    	}else if(whistleMode==2){
    		whistleModeButton.displayString="Arriving";
    	}else if(whistleMode==3){
    		whistleModeButton.displayString="Departing";
    	}else{
    		whistleModeButton.displayString="Arrive+Depart";
    	}
    	whistleVolumeBox.setText(String.valueOf(whistleVolume));
    	whistlePitchBox.setText(String.valueOf(whistlePitch));
    	
    	if(loadingOps%10>=5){loadingEntityButton.displayString="Chicken";}
    	else if(loadingOps%10>=4){loadingEntityButton.displayString="Pig";}
    	else if(loadingOps%10>=3){loadingEntityButton.displayString="Sheep";}
    	else if(loadingOps%10>=2){loadingEntityButton.displayString="Cow";}
    	else if(loadingOps%10>=1){loadingEntityButton.displayString="Horse";}
    	else if(loadingOps%10>=0){loadingEntityButton.displayString="Villager";}
    	if(loadingOps%100>=30){loadingStockButton.displayString="Locomotive";}
    	else if(loadingOps%100>=20){loadingStockButton.displayString="Carriage";}
    	else if(loadingOps%100>=10){loadingStockButton.displayString="Boxcar";}
    	else if(loadingOps%100>=0){loadingStockButton.displayString="Any";}
    	if(loadingOps%1000>=200){loadingDirectionButton.displayString="Left";}
    	else if(loadingOps%1000>=100){loadingDirectionButton.displayString="Right";}
    	else if(loadingOps%1000>=0){loadingDirectionButton.displayString="Any";}
    	
    	if(unloadingOps%10>=5){unloadingEntityButton.displayString="Chicken";}
    	else if(unloadingOps%10>=4){unloadingEntityButton.displayString="Pig";}
    	else if(unloadingOps%10>=3){unloadingEntityButton.displayString="Sheep";}
    	else if(unloadingOps%10>=2){unloadingEntityButton.displayString="Cow";}
    	else if(unloadingOps%10>=1){unloadingEntityButton.displayString="Horse";}
    	else if(unloadingOps%10>=0){unloadingEntityButton.displayString="Villager";}
    	if(unloadingOps%100>=30){unloadingStockButton.displayString="Locomotive";}
    	else if(unloadingOps%100>=20){unloadingStockButton.displayString="Carriage";}
    	else if(unloadingOps%100>=10){unloadingStockButton.displayString="Boxcar";}
    	else if(unloadingOps%100>=0){unloadingStockButton.displayString="Any";}
    	if(unloadingOps%1000>=200){unloadingDirectionButton.displayString="Left";}
    	else if(unloadingOps%1000>=100){unloadingDirectionButton.displayString="Right";}
    	
    	drawTexturedModalRect(x, y, 0, 0, 175, 165);
		freightSettingsButton.drawButton(mc, mouseX, mouseY);
		locoSettingsButton.drawButton(mc, mouseX, mouseY);
		setPage(!locoSettingsButton.enabled);
    	if(!locoSettingsButton.enabled){
			locoLabelBox.drawTextBox();
			reverseSetBox.drawTextBox();
			if(opMode==1){
				opModeInfo.drawTextBox();
				tickDelayBox.drawTextBox();
				tickDelayBox.setVisible(true);
			}else if(opMode==2){
				opModeInfo.drawTextBox();
				scheduledTimeBox.drawTextBox();
				tickDelayBox.setVisible(false);
			}
			opModeButton.drawButton(mc, mouseX, mouseY);
			whistleModeButton.drawButton(mc, mouseX, mouseY);
			fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Station Mode:", x+10, y+35, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Throttle Set:", x+10, y+80, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Whistle Mode:", x+10, y+100, Color.WHITE.getRGB());
			if(whistleMode!=1){
				whistleVolumeBox.drawTextBox();
				whistlePitchBox.drawTextBox();
				whistleVolumeUpButton.drawButton(mc, mouseX, mouseY);
				whistleVolumeDownButton.drawButton(mc, mouseX, mouseY);
				whistlePitchUpButton.drawButton(mc, mouseX, mouseY);
				whistlePitchDownButton.drawButton(mc, mouseX, mouseY);
				fontRendererObj.drawStringWithShadow("Volume:", x+20, y+120, Color.WHITE.getRGB());
				fontRendererObj.drawStringWithShadow("Pitch:", x+90, y+120, Color.WHITE.getRGB());
			}
    	}else{
        	loadingEntityButton.drawButton(mc, mouseX, mouseY);
        	loadingDirectionButton.drawButton(mc, mouseX, mouseY);
        	loadingStockButton.drawButton(mc, mouseX, mouseY);
        	unloadingEntityButton.drawButton(mc, mouseX, mouseY);
        	unloadingDirectionButton.drawButton(mc, mouseX, mouseY);
        	unloadingStockButton.drawButton(mc, mouseX, mouseY);
			fontRendererObj.drawStringWithShadow("Load Entity:", x+10, y+10, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Into Stock:", x+10, y+50, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("From Side:", x+10, y+90, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Unload Entity:", x+95, y+10, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("From Stock:", x+95, y+50, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("To Side:", x+95, y+90, Color.WHITE.getRGB());
    	}

    }
    
    @Override
    protected void actionPerformed(GuiButton button){
    	if(button.equals(locoSettingsButton)){
    		locoSettingsButton.enabled=false;
    		freightSettingsButton.enabled=true;
    	}else if(button.equals(freightSettingsButton)){
    		freightSettingsButton.enabled=false;
    		locoSettingsButton.enabled=true;
    	}else 
    	if(button.equals(opModeButton)){
    		++opMode;
    		if(opMode>3){opMode=1;}
    	}else if(button.equals(whistleModeButton)){
    		++whistleMode;
    		if(whistleMode>4){whistleMode=1;}
    	}else if(button.equals(whistleVolumeUpButton)){
    		if(whistleVolume<10.0F){whistleVolume=whistleVolume+0.5F;}
    	}else if(button.equals(whistleVolumeDownButton)){
    		if(whistleVolume>0.5F){whistleVolume=whistleVolume-0.5F;}
    	}else if(button.equals(whistlePitchUpButton)){
    		if(whistlePitch<1.5F){whistlePitch=(whistlePitch*10+1.0F)/10;}
    	}else if(button.equals(whistlePitchDownButton)){
    		if(whistlePitch>0.5F){whistlePitch=(whistlePitch*10-1.0F)/10;}
    	}else if(button.equals(loadingEntityButton)){
    		if(loadingOps%10>=5){
        		loadingOps=loadingOps-5;
    		}else{
        		loadingOps=loadingOps+1;
    		}
    	}else if(button.equals(loadingStockButton)){
    		if(loadingOps%100>=30){
    			loadingOps=loadingOps-30;
    		}else{
    			loadingOps=loadingOps+10;
    		}
    	}else if(button.equals(loadingDirectionButton)){
    		if(loadingOps%1000>=200){
    			loadingOps=loadingOps-200;
    		}else{
    			loadingOps=loadingOps+100;
    		}
    	}else if(button.equals(unloadingEntityButton)){
    		if(unloadingOps%10>=5){
        		unloadingOps=unloadingOps-5;
    		}else{
        		unloadingOps=unloadingOps+1;
    		}
    	}else if(button.equals(unloadingStockButton)){
    		if(unloadingOps%100>=30){
    			unloadingOps=unloadingOps-30;
    		}else{
    			unloadingOps=unloadingOps+10;
    		}
    	}else if(button.equals(unloadingDirectionButton)){
    		if(unloadingOps%1000>=200){
        		unloadingOps=unloadingOps-100;
    		}else{
        		unloadingOps=unloadingOps+100;
    		}
    	}
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	locoLabelBox.mouseClicked(x, y, p_73864_3_);
    	tickDelayBox.mouseClicked(x, y, p_73864_3_);
    	scheduledTimeBox.mouseClicked(x, y, p_73864_3_);
    	reverseSetBox.mouseClicked(x, y, p_73864_3_);
    	whistleVolumeBox.mouseClicked(x, y, p_73864_3_);
    	whistlePitchBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int bytecode){
    	if(bytecode == 1){
        	this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();
    	}else if(locoLabelBox.isFocused()){
        	locoLabelBox.textboxKeyTyped(key, bytecode);
        }else if(isNumeric(key,bytecode)){
        	if(tickDelayBox.isFocused() && tickDelayBox.getVisible()){
        		tickDelayBox.textboxKeyTyped(key, bytecode);
        	}else if(scheduledTimeBox.isFocused()){
        		scheduledTimeBox.textboxKeyTyped(key, bytecode);
        	}else if(reverseSetBox.isFocused()){
        		reverseSetBox.textboxKeyTyped(key, bytecode);
        	}
    	}else if(key=='e'){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    
    @Override
    public void onGuiClosed(){
    	if(reverseSetBox.getText().equals("")){reverseSetBox.setText(String.valueOf(this.reverseSet));}
    	if(tickDelayBox.getText().equals("")){tickDelayBox.setText(String.valueOf(this.tickDelay));}
    	if(scheduledTimeBox.getText().equals("")){scheduledTimeBox.setText(String.valueOf(this.scheduledTime));}
    	PacketStation thisPacket = new PacketStation(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord, opMode, Integer.parseInt(reverseSetBox.getText()), Integer.parseInt(tickDelayBox.getText()), whistleMode, loadingOps, unloadingOps, whistleVolume, whistlePitch, Long.parseLong(scheduledTimeBox.getText()), locoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(thisPacket);
		thisTileEntity.getWorldObj().markBlockForUpdate(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord);
    }
    
	@Override
    public boolean doesGuiPauseGame(){return false;}
	
	public void setPage(boolean locoPage){
		opModeButton.visible=locoPage;
		whistleModeButton.visible=locoPage;
		whistleVolumeUpButton.visible=locoPage;
		whistleVolumeDownButton.visible=locoPage;
		whistlePitchUpButton.visible=locoPage;
		whistlePitchDownButton.visible=locoPage;
		locoLabelBox.setVisible(locoPage);
		opModeInfo.setVisible(locoPage);
		tickDelayBox.setVisible(locoPage);
		scheduledTimeBox.setVisible(locoPage);
		reverseSetBox.setVisible(locoPage);
		whistleVolumeBox.setVisible(locoPage);
		whistlePitchBox.setVisible(locoPage);
		
		loadingEntityButton.visible=!locoPage;
		loadingStockButton.visible=!locoPage;
		loadingDirectionButton.visible=!locoPage;
		unloadingEntityButton.visible=!locoPage;
		unloadingStockButton.visible=!locoPage;
		unloadingDirectionButton.visible=!locoPage;
	}
	
	public static boolean isNumeric(char key, int Bytecode){
    	try{
    		Integer.valueOf(String.valueOf(key));
    	}catch(NumberFormatException nfe){
    		if(Bytecode!=12
    	    &&Bytecode!=14
    		&&Bytecode!=199
    		&&Bytecode!=207
    		&&Bytecode!=211
    		&&Bytecode!=203
    		&&Bytecode!=205){
    			return false;
    		}
    	}
    	return true;
	}
}
