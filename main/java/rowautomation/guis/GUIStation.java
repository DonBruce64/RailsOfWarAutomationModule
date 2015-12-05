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
	private TileEntityStation ThisTileEntity;
	private int OpMode;
	private int ReverseSet;
	private int TickDelay;
	private int WhistleMode;
	private int LoadingOps;
	private int UnloadingOps;
	private float WhistleVolume;
	private float WhistlePitch;
	private long ScheduledTime;
	private String LocoLabel;

	private ResourceLocation Background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiButton LocoSettingsButton;
	private GuiButton OpModeButton;
	private GuiButton WhistleModeButton;
	private GuiButton WhistleVolumeUpButton;
	private GuiButton WhistleVolumeDownButton;
	private GuiButton WhistlePitchUpButton;
	private GuiButton WhistlePitchDownButton;
	private GuiTextField LocoLabelBox;
	private GuiTextField OpModeInfo;
	private GuiTextField TickDelayBox;
	private GuiTextField ScheduledTimeBox;
	private GuiTextField ReverseSetBox;
	private GuiTextField WhistleVolumeBox;
	private GuiTextField WhistlePitchBox;
	
	private GuiButton FreightSettingsButton;
	private GuiButton LoadingEntityButton;
	private GuiButton LoadingStockButton;
	private GuiButton LoadingDirectionButton;
	private GuiButton UnloadingEntityButton;
	private GuiButton UnloadingStockButton;
	private GuiButton UnloadingDirectionButton;
;
	public GUIStation(TileEntityStation ThisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.ThisTileEntity=ThisTileEntity;
		this.LocoLabel=ThisTileEntity.LocoLabel;
		this.OpMode=ThisTileEntity.OpMode;
		this.TickDelay=ThisTileEntity.TickDelay;
		this.ScheduledTime=ThisTileEntity.ScheduledTime;
		this.ReverseSet=ThisTileEntity.ReverseSet;
		this.WhistleMode=ThisTileEntity.WhistleMode;
		this.WhistleVolume=ThisTileEntity.WhistleVolume;
		this.WhistlePitch=ThisTileEntity.WhistlePitch;
		this.LoadingOps=ThisTileEntity.LoadingOps;
		this.UnloadingOps=ThisTileEntity.UnloadingOps;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	
    	LocoSettingsButton = new GuiButton(1, x-5, y-20, 85, 20, "Loco Settings");
    	LocoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	OpModeButton = new GuiButton(1, x+80, y+30, 80, 20, "");
    	OpModeInfo = new GuiTextField(fontRendererObj, x+5, y+60, 100, 15);
    	TickDelayBox = new GuiTextField(fontRendererObj, x+100, y+55, 60, 15);
    	ScheduledTimeBox = new GuiTextField(fontRendererObj, x+100, y+55, 60, 15);
    	ReverseSetBox = new GuiTextField(fontRendererObj, x+80, y+75, 80, 15);
    	WhistleModeButton = new GuiButton(1, x+80, y+95, 80, 20, "");
    	WhistleVolumeBox = new GuiTextField(fontRendererObj, x+55, y+120, 30, 20);
    	WhistlePitchBox = new GuiTextField(fontRendererObj, x+120, y+120, 30, 20);
    	WhistleVolumeUpButton = new GuiButton(1, x+25, y+135, 20, 20, "+");
    	WhistleVolumeDownButton = new GuiButton(1, x+45, y+135, 20, 20, "-");
    	WhistlePitchUpButton = new GuiButton(1, x+90, y+135, 20, 20, "+");
    	WhistlePitchDownButton = new GuiButton(1, x+110, y+135, 20, 20, "-");
    	LocoSettingsButton.enabled=false;
    	OpModeInfo.setEnableBackgroundDrawing(false);
    	OpModeInfo.setEnabled(false);
    	OpModeInfo.setDisabledTextColour(Color.WHITE.getRGB());
    	TickDelayBox.setMaxStringLength(8);
    	ScheduledTimeBox.setMaxStringLength(5);
    	ReverseSetBox.setMaxStringLength(3);
    	WhistleVolumeBox.setEnableBackgroundDrawing(false);
    	WhistleVolumeBox.setEnabled(false);
    	WhistleVolumeBox.setDisabledTextColour(Color.WHITE.getRGB());
    	WhistlePitchBox.setEnableBackgroundDrawing(false);
    	WhistlePitchBox.setEnabled(false);
    	WhistlePitchBox.setDisabledTextColour(Color.WHITE.getRGB());
    	buttonList.add(LocoSettingsButton);
    	buttonList.add(OpModeButton);
    	buttonList.add(WhistleModeButton);
    	buttonList.add(WhistleVolumeUpButton);
    	buttonList.add(WhistleVolumeDownButton);
    	buttonList.add(WhistlePitchUpButton);
    	buttonList.add(WhistlePitchDownButton);
    	LocoLabelBox.setText(LocoLabel);
    	TickDelayBox.setText(String.valueOf(TickDelay));
    	ScheduledTimeBox.setText(String.valueOf(ScheduledTime));
    	ReverseSetBox.setText(String.valueOf(ReverseSet));
    	
    	FreightSettingsButton = new GuiButton(1, x+79, y-20, 91, 20, "Freight Settings");
    	LoadingEntityButton = new GuiButton(1, x+5, y+20, 70, 20, "");
    	LoadingStockButton = new GuiButton(1, x+5, y+60, 70, 20, "");
    	LoadingDirectionButton = new GuiButton(1, x+5, y+100, 70, 20, "");
    	UnloadingEntityButton = new GuiButton(1, x+90, y+20, 70, 20, "");
    	UnloadingStockButton = new GuiButton(1, x+90, y+60, 70, 20, "");
    	UnloadingDirectionButton = new GuiButton(1, x+90, y+100, 70, 20, "");
    	buttonList.add(FreightSettingsButton);
    	buttonList.add(LoadingEntityButton);
    	buttonList.add(LoadingStockButton);
    	buttonList.add(LoadingDirectionButton);
    	buttonList.add(UnloadingEntityButton);
    	buttonList.add(UnloadingStockButton);
    	buttonList.add(UnloadingDirectionButton);
	}
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(Background);
    	if(OpMode==1){
    		OpModeButton.displayString="Time Delay";
    		OpModeInfo.setText("Delay (ticks):");
    	}else if(OpMode==2){
    		OpModeButton.displayString="Scheduled";
    		OpModeInfo.setText("Departure time:");
    	}else{
    		OpModeButton.displayString="Redstone";
    	}
    	if(WhistleMode==1){
    		WhistleModeButton.displayString="None";
    	}else if(WhistleMode==2){
    		WhistleModeButton.displayString="Arriving";
    	}else if(WhistleMode==3){
    		WhistleModeButton.displayString="Departing";
    	}else{
    		WhistleModeButton.displayString="Arrive+Depart";
    	}
    	WhistleVolumeBox.setText(String.valueOf(WhistleVolume));
    	WhistlePitchBox.setText(String.valueOf(WhistlePitch));
    	
    	if(LoadingOps%10>=5){LoadingEntityButton.displayString="Chicken";}
    	else if(LoadingOps%10>=4){LoadingEntityButton.displayString="Pig";}
    	else if(LoadingOps%10>=3){LoadingEntityButton.displayString="Sheep";}
    	else if(LoadingOps%10>=2){LoadingEntityButton.displayString="Cow";}
    	else if(LoadingOps%10>=1){LoadingEntityButton.displayString="Horse";}
    	else if(LoadingOps%10>=0){LoadingEntityButton.displayString="Villager";}
    	if(LoadingOps%100>=30){LoadingStockButton.displayString="Locomotive";}
    	else if(LoadingOps%100>=20){LoadingStockButton.displayString="Carriage";}
    	else if(LoadingOps%100>=10){LoadingStockButton.displayString="Boxcar";}
    	else if(LoadingOps%100>=0){LoadingStockButton.displayString="Any";}
    	if(LoadingOps%1000>=200){LoadingDirectionButton.displayString="Left";}
    	else if(LoadingOps%1000>=100){LoadingDirectionButton.displayString="Right";}
    	else if(LoadingOps%1000>=0){LoadingDirectionButton.displayString="Any";}
    	
    	if(UnloadingOps%10>=5){UnloadingEntityButton.displayString="Chicken";}
    	else if(UnloadingOps%10>=4){UnloadingEntityButton.displayString="Pig";}
    	else if(UnloadingOps%10>=3){UnloadingEntityButton.displayString="Sheep";}
    	else if(UnloadingOps%10>=2){UnloadingEntityButton.displayString="Cow";}
    	else if(UnloadingOps%10>=1){UnloadingEntityButton.displayString="Horse";}
    	else if(UnloadingOps%10>=0){UnloadingEntityButton.displayString="Villager";}
    	if(UnloadingOps%100>=30){UnloadingStockButton.displayString="Locomotive";}
    	else if(UnloadingOps%100>=20){UnloadingStockButton.displayString="Carriage";}
    	else if(UnloadingOps%100>=10){UnloadingStockButton.displayString="Boxcar";}
    	else if(UnloadingOps%100>=0){UnloadingStockButton.displayString="Any";}
    	if(UnloadingOps%1000>=200){UnloadingDirectionButton.displayString="Left";}
    	else if(UnloadingOps%1000>=100){UnloadingDirectionButton.displayString="Right";}
    	
    	drawTexturedModalRect(x, y, 0, 0, 175, 165);
		FreightSettingsButton.drawButton(mc, mouseX, mouseY);
		LocoSettingsButton.drawButton(mc, mouseX, mouseY);
		setPage(!LocoSettingsButton.enabled);
    	if(!LocoSettingsButton.enabled){
			LocoLabelBox.drawTextBox();
			ReverseSetBox.drawTextBox();
			if(OpMode==1){
				OpModeInfo.drawTextBox();
				TickDelayBox.drawTextBox();
				TickDelayBox.setVisible(true);
			}else if(OpMode==2){
				OpModeInfo.drawTextBox();
				ScheduledTimeBox.drawTextBox();
				TickDelayBox.setVisible(false);
			}
			OpModeButton.drawButton(mc, mouseX, mouseY);
			WhistleModeButton.drawButton(mc, mouseX, mouseY);
			fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Station Mode:", x+10, y+35, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Throttle Set:", x+10, y+80, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Whistle Mode:", x+10, y+100, Color.WHITE.getRGB());
			if(WhistleMode!=1){
				WhistleVolumeBox.drawTextBox();
				WhistlePitchBox.drawTextBox();
				WhistleVolumeUpButton.drawButton(mc, mouseX, mouseY);
				WhistleVolumeDownButton.drawButton(mc, mouseX, mouseY);
				WhistlePitchUpButton.drawButton(mc, mouseX, mouseY);
				WhistlePitchDownButton.drawButton(mc, mouseX, mouseY);
				fontRendererObj.drawStringWithShadow("Volume:", x+20, y+120, Color.WHITE.getRGB());
				fontRendererObj.drawStringWithShadow("Pitch:", x+90, y+120, Color.WHITE.getRGB());
			}
    	}else{
        	LoadingEntityButton.drawButton(mc, mouseX, mouseY);
        	LoadingDirectionButton.drawButton(mc, mouseX, mouseY);
        	LoadingStockButton.drawButton(mc, mouseX, mouseY);
        	UnloadingEntityButton.drawButton(mc, mouseX, mouseY);
        	UnloadingDirectionButton.drawButton(mc, mouseX, mouseY);
        	UnloadingStockButton.drawButton(mc, mouseX, mouseY);
			fontRendererObj.drawStringWithShadow("Load Entity:", x+10, y+10, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Into Stock:", x+10, y+50, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("From Side:", x+10, y+90, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("Unload Entity:", x+95, y+10, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("From Stock:", x+95, y+50, Color.WHITE.getRGB());
			fontRendererObj.drawStringWithShadow("To Side:", x+95, y+90, Color.WHITE.getRGB());
    	}

    }
    
    @Override
    protected void actionPerformed(GuiButton Button){
    	if(Button.equals(LocoSettingsButton)){
    		LocoSettingsButton.enabled=false;
    		FreightSettingsButton.enabled=true;
    	}else if(Button.equals(FreightSettingsButton)){
    		FreightSettingsButton.enabled=false;
    		LocoSettingsButton.enabled=true;
    	}else 
    	if(Button.equals(OpModeButton)){
    		++OpMode;
    		if(OpMode>3){OpMode=1;}
    	}else if(Button.equals(WhistleModeButton)){
    		++WhistleMode;
    		if(WhistleMode>4){WhistleMode=1;}
    	}else if(Button.equals(WhistleVolumeUpButton)){
    		if(WhistleVolume<10.0F){WhistleVolume=WhistleVolume+0.5F;}
    	}else if(Button.equals(WhistleVolumeDownButton)){
    		if(WhistleVolume>0.5F){WhistleVolume=WhistleVolume-0.5F;}
    	}else if(Button.equals(WhistlePitchUpButton)){
    		if(WhistlePitch<1.5F){WhistlePitch=(WhistlePitch*10+1.0F)/10;}
    	}else if(Button.equals(WhistlePitchDownButton)){
    		if(WhistlePitch>0.5F){WhistlePitch=(WhistlePitch*10-1.0F)/10;}
    	}else if(Button.equals(LoadingEntityButton)){
    		if(LoadingOps%10>=5){
        		LoadingOps=LoadingOps-5;
    		}else{
        		LoadingOps=LoadingOps+1;
    		}
    	}else if(Button.equals(LoadingStockButton)){
    		if(LoadingOps%100>=30){
    			LoadingOps=LoadingOps-30;
    		}else{
    			LoadingOps=LoadingOps+10;
    		}
    	}else if(Button.equals(LoadingDirectionButton)){
    		if(LoadingOps%1000>=200){
    			LoadingOps=LoadingOps-200;
    		}else{
    			LoadingOps=LoadingOps+100;
    		}
    	}else if(Button.equals(UnloadingEntityButton)){
    		if(UnloadingOps%10>=5){
        		UnloadingOps=UnloadingOps-5;
    		}else{
        		UnloadingOps=UnloadingOps+1;
    		}
    	}else if(Button.equals(UnloadingStockButton)){
    		if(UnloadingOps%100>=30){
    			UnloadingOps=UnloadingOps-30;
    		}else{
    			UnloadingOps=UnloadingOps+10;
    		}
    	}else if(Button.equals(UnloadingDirectionButton)){
    		if(UnloadingOps%1000>=200){
        		UnloadingOps=UnloadingOps-100;
    		}else{
        		UnloadingOps=UnloadingOps+100;
    		}
    	}
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	LocoLabelBox.mouseClicked(x, y, p_73864_3_);
    	TickDelayBox.mouseClicked(x, y, p_73864_3_);
    	ScheduledTimeBox.mouseClicked(x, y, p_73864_3_);
    	ReverseSetBox.mouseClicked(x, y, p_73864_3_);
    	WhistleVolumeBox.mouseClicked(x, y, p_73864_3_);
    	WhistlePitchBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int bytecode){
    	if(bytecode == 1){
        	this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();
    	}else if(LocoLabelBox.isFocused()){
        	LocoLabelBox.textboxKeyTyped(key, bytecode);
        }else if(isNumeric(key,bytecode)){
        	if(TickDelayBox.isFocused() && TickDelayBox.getVisible()){
        		TickDelayBox.textboxKeyTyped(key, bytecode);
        	}else if(ScheduledTimeBox.isFocused()){
        		ScheduledTimeBox.textboxKeyTyped(key, bytecode);
        	}else if(ReverseSetBox.isFocused()){
        		ReverseSetBox.textboxKeyTyped(key, bytecode);
        	}
    	}else if(key=='e'){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    
    @Override
    public void onGuiClosed(){
    	if(ReverseSetBox.getText().equals("")){ReverseSetBox.setText(String.valueOf(this.ReverseSet));}
    	if(TickDelayBox.getText().equals("")){TickDelayBox.setText(String.valueOf(this.TickDelay));}
    	if(ScheduledTimeBox.getText().equals("")){ScheduledTimeBox.setText(String.valueOf(this.ScheduledTime));}
    	PacketStation ThisPacket=new PacketStation(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord, OpMode, Integer.parseInt(ReverseSetBox.getText()), Integer.parseInt(TickDelayBox.getText()), WhistleMode, LoadingOps, UnloadingOps, WhistleVolume, WhistlePitch, Long.parseLong(ScheduledTimeBox.getText()), LocoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(ThisPacket);
		ThisTileEntity.getWorldObj().markBlockForUpdate(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord);
    }
    
	@Override
    public boolean doesGuiPauseGame(){return false;}
	
	public void setPage(boolean locoPage){
		OpModeButton.visible=locoPage;
		WhistleModeButton.visible=locoPage;
		WhistleVolumeUpButton.visible=locoPage;
		WhistleVolumeDownButton.visible=locoPage;
		WhistlePitchUpButton.visible=locoPage;
		WhistlePitchDownButton.visible=locoPage;
		LocoLabelBox.setVisible(locoPage);
		OpModeInfo.setVisible(locoPage);
		TickDelayBox.setVisible(locoPage);
		ScheduledTimeBox.setVisible(locoPage);
		ReverseSetBox.setVisible(locoPage);
		WhistleVolumeBox.setVisible(locoPage);
		WhistlePitchBox.setVisible(locoPage);
		
		LoadingEntityButton.visible=!locoPage;
		LoadingStockButton.visible=!locoPage;
		LoadingDirectionButton.visible=!locoPage;
		UnloadingEntityButton.visible=!locoPage;
		UnloadingStockButton.visible=!locoPage;
		UnloadingDirectionButton.visible=!locoPage;
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
