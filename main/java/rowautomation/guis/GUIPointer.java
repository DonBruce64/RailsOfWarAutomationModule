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
	private TileEntityPointer ThisTileEntity;
	private boolean Locked;
	private boolean Redstone;
	private boolean Switched;
	private boolean Spring;
	private String LocoLabel;

	private ResourceLocation Background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiButton LockedButton;
	private GuiButton RedstoneButton;
	private GuiButton SpringButton;
	private GuiButton SwitchedButton;
	private GuiTextField LocoLabelBox;

	public GUIPointer(TileEntityPointer ThisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.ThisTileEntity=ThisTileEntity;
    	this.Locked=ThisTileEntity.Locked;
    	this.Redstone=ThisTileEntity.Redstone;
    	this.Spring=ThisTileEntity.Spring;
    	this.Switched=ThisTileEntity.Switched;
    	this.LocoLabel=ThisTileEntity.LocoLabel;
	}
	
	@Override 
	public void initGui(){
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
    	LocoLabelBox = new GuiTextField(fontRendererObj, x+75, y+10, 85, 15);
    	SwitchedButton = new GuiButton(1, x+110, y+35, 50, 20, "");
    	LockedButton = new GuiButton(1, x+110, y+60, 50, 20, "");
    	RedstoneButton = new GuiButton(1, x+110, y+85, 50, 20, "");
    	SpringButton = new GuiButton(1, x+110, y+110, 50, 20, "");
    	buttonList.add(SwitchedButton);
    	buttonList.add(LockedButton);
    	buttonList.add(RedstoneButton);
    	buttonList.add(SpringButton);
    	LocoLabelBox.setText(LocoLabel);
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(Background);
    	if(Switched){
    		SwitchedButton.displayString="Siding";
        	if(Spring){
        		LockedButton.displayString="Siding";
        	}else if(Locked){
        		LockedButton.displayString="Main Line";
        	}else{
        		LockedButton.displayString="Ignored";
        	}
    	}else{
    		SwitchedButton.displayString="Main Line";
    		if(Spring){
    			LockedButton.displayString="Main Line";
        	}else if(Locked){
        		LockedButton.displayString="Siding";
        	}else{
        		LockedButton.displayString="Ignored";
        	}
    	}
    	if(Redstone){
    		SwitchedButton.enabled=false;
    		LockedButton.enabled=false;
    		LocoLabelBox.setEnabled(false);
    		RedstoneButton.displayString="Enabled";
    		SpringButton.displayString="Disabled";
    	}else if(Spring){
    		SwitchedButton.enabled=true;
    		LockedButton.enabled=false;
    		LocoLabelBox.setEnabled(false);
    		SpringButton.displayString="Enabled";
    		RedstoneButton.displayString="Disabled";
    	}else{
    		LockedButton.enabled=true;
    		SwitchedButton.enabled=true;
    		LocoLabelBox.setEnabled(true);
    		RedstoneButton.displayString="Disabled";
    		SpringButton.displayString="Disabled";
    	}
    	drawTexturedModalRect(x, y, 0, 0, 175, 165);
    	LocoLabelBox.drawTextBox();
    	LockedButton.drawButton(this.mc, mouseX, mouseY);
    	RedstoneButton.drawButton(this.mc, mouseX, mouseY);
    	SwitchedButton.drawButton(this.mc, mouseX, mouseY);
    	SpringButton.drawButton(this.mc, mouseX, mouseY);
    	fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Loco Destination:", x+10, y+40, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Other Locos:", x+10, y+65, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Redstone Control:", x+10, y+90, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Spring Control:", x+10, y+115, Color.WHITE.getRGB());
    }
    
    @Override
    protected void actionPerformed(GuiButton button){
    	if(button.equals(LockedButton)){
    		if(Locked){
    			Locked=false;
    		}else{
    			Locked=true;
    		}
    	}else if(button.equals(RedstoneButton)){
    		if(Redstone){
    			Redstone=false;
    		}else{
    			Redstone=true;
    			Spring=false;
    		}
    	}else if(button.equals(SwitchedButton)){
    		if(Switched){
    			Switched=false;
    		}else{
    			Switched=true;
    		}
    	}else if(button.equals(SpringButton)){
    		if(Spring){
    			Spring=false;
    		}else{
    			Spring=true;
    			Redstone=false;
    		}
    	}
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	LocoLabelBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int mod){
        if(mod == 1 || key=='e' && !LocoLabelBox.isFocused()){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        if(LocoLabelBox.isFocused()){
        	LocoLabelBox.textboxKeyTyped(key, mod);
        }
    }
    
    @Override
    public void onGuiClosed(){
    	PacketPointer ThisPacket=new PacketPointer(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord, Locked, Redstone, Spring, Switched, LocoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(ThisPacket);
		ThisTileEntity.getWorldObj().markBlockForUpdate(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord);
    }
    
	@Override
    public boolean doesGuiPauseGame(){return false;}
}
