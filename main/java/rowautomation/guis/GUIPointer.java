package rowautomation.guis;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import rowautomation.ROWAM;
import rowautomation.network.PacketPointer;
import rowautomation.tileentities.TileEntityPointer;


public class GUIPointer extends GuiScreen{
	private TileEntityPointer thisTileEntity;
	private boolean locked;
	private boolean redstone;
	private boolean switched;
	private boolean spring;
	private String locoLabel;

	private ResourceLocation background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiButton lockedButton;
	private GuiButton redstoneButton;
	private GuiButton springButton;
	private GuiButton switchedButton;
	private GuiTextField locoLabelBox;

	public GUIPointer(TileEntityPointer thisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.thisTileEntity=thisTileEntity;
    	this.locked=thisTileEntity.locked;
    	this.redstone=thisTileEntity.redstone;
    	this.spring=thisTileEntity.spring;
    	this.switched=thisTileEntity.switched;
    	this.locoLabel=thisTileEntity.locoLabel;
	}
	
	@Override 
	public void initGui(){
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
    	locoLabelBox = new GuiTextField(fontRendererObj, x+75, y+10, 85, 15);
    	switchedButton = new GuiButton(1, x+110, y+35, 50, 20, "");
    	lockedButton = new GuiButton(1, x+110, y+60, 50, 20, "");
    	redstoneButton = new GuiButton(1, x+110, y+85, 50, 20, "");
    	springButton = new GuiButton(1, x+110, y+110, 50, 20, "");
    	buttonList.add(switchedButton);
    	buttonList.add(lockedButton);
    	buttonList.add(redstoneButton);
    	buttonList.add(springButton);
    	locoLabelBox.setText(locoLabel);
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(background);
    	if(switched){
    		switchedButton.displayString="Siding";
        	if(spring){
        		lockedButton.displayString="Siding";
        	}else if(locked){
        		lockedButton.displayString="Main Line";
        	}else{
        		lockedButton.displayString="Ignored";
        	}
    	}else{
    		switchedButton.displayString="Main Line";
    		if(spring){
    			lockedButton.displayString="Main Line";
        	}else if(locked){
        		lockedButton.displayString="Siding";
        	}else{
        		lockedButton.displayString="Ignored";
        	}
    	}
    	if(redstone){
    		switchedButton.enabled=false;
    		lockedButton.enabled=false;
    		locoLabelBox.setEnabled(false);
    		redstoneButton.displayString="Enabled";
    		springButton.displayString="Disabled";
    	}else if(spring){
    		switchedButton.enabled=true;
    		lockedButton.enabled=false;
    		locoLabelBox.setEnabled(false);
    		springButton.displayString="Enabled";
    		redstoneButton.displayString="Disabled";
    	}else{
    		lockedButton.enabled=true;
    		switchedButton.enabled=true;
    		locoLabelBox.setEnabled(true);
    		redstoneButton.displayString="Disabled";
    		springButton.displayString="Disabled";
    	}
    	drawTexturedModalRect(x, y, 0, 0, 175, 165);
    	locoLabelBox.drawTextBox();
    	lockedButton.drawButton(this.mc, mouseX, mouseY);
    	redstoneButton.drawButton(this.mc, mouseX, mouseY);
    	switchedButton.drawButton(this.mc, mouseX, mouseY);
    	springButton.drawButton(this.mc, mouseX, mouseY);
    	fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Loco Destination:", x+10, y+40, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Other Locos:", x+10, y+65, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Redstone Control:", x+10, y+90, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Spring Control:", x+10, y+115, Color.WHITE.getRGB());
    }
    
    @Override
    protected void actionPerformed(GuiButton button){
    	if(button.equals(lockedButton)){
    		if(locked){
    			locked=false;
    		}else{
    			locked=true;
    		}
    	}else if(button.equals(redstoneButton)){
    		if(redstone){
    			redstone=false;
    		}else{
    			redstone=true;
    			spring=false;
    		}
    	}else if(button.equals(switchedButton)){
    		if(switched){
    			switched=false;
    		}else{
    			switched=true;
    		}
    	}else if(button.equals(springButton)){
    		if(spring){
    			spring=false;
    		}else{
    			spring=true;
    			redstone=false;
    		}
    	}
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	locoLabelBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int mod){
        if(mod == 1 || key=='e' && !locoLabelBox.isFocused()){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        if(locoLabelBox.isFocused()){
        	locoLabelBox.textboxKeyTyped(key, mod);
        }
    }
    
    @Override
    public void onGuiClosed(){
    	PacketPointer ThisPacket=new PacketPointer(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord, locked, redstone, spring, switched, locoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(ThisPacket);
		thisTileEntity.getWorldObj().markBlockForUpdate(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord);
    }
    
	@Override
    public boolean doesGuiPauseGame(){return false;}
}
