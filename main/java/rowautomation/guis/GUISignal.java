package rowautomation.guis;

import java.awt.Color;

import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import rowautomation.ROWAM;
import rowautomation.network.PacketSignal;
import rowautomation.tileentities.TileEntitySignal;


public class GUISignal extends GuiScreen{
	private TileEntitySignal ThisTileEntity;
	private int ReverseMax;
	private int ReverseMin;
	private int ReverseSetMax;
	private int ReverseSetMin;
	private String LocoLabel;

	private ResourceLocation Background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiTextField LocoLabelBox;
	private GuiTextField ReverseMaxBox;
	private GuiTextField ReverseSetMaxBox;
	private GuiTextField ReverseMinBox;
	private GuiTextField ReverseSetMinBox;	

	public GUISignal(TileEntitySignal ThisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.ThisTileEntity=ThisTileEntity;
		this.LocoLabel=ThisTileEntity.LocoLabel;
		this.ReverseMax=ThisTileEntity.ReverseMax;
		this.ReverseSetMax=ThisTileEntity.ReverseSetMax;
		this.ReverseMin=ThisTileEntity.ReverseMin;
		this.ReverseSetMin=ThisTileEntity.ReverseSetMin;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	LocoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	ReverseMaxBox = new GuiTextField(fontRendererObj, x+90, y+50, 40, 15);
    	ReverseSetMaxBox = new GuiTextField(fontRendererObj, x+90, y+70, 40, 15);
    	ReverseMinBox = new GuiTextField(fontRendererObj, x+90, y+105, 40, 15);
    	ReverseSetMinBox = new GuiTextField(fontRendererObj, x+90, y+125, 40, 15);	
    	ReverseMaxBox.setMaxStringLength(3);
    	ReverseSetMaxBox.setMaxStringLength(3);
    	ReverseMinBox.setMaxStringLength(3);
    	ReverseSetMinBox.setMaxStringLength(3);
    	LocoLabelBox.setText(LocoLabel);
    	ReverseMaxBox.setText(String.valueOf(ReverseMax));
    	ReverseSetMaxBox.setText(String.valueOf(ReverseSetMax));
    	ReverseMinBox.setText(String.valueOf(ReverseMin));
    	ReverseSetMinBox.setText(String.valueOf(ReverseSetMin));
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(Background);
    	drawTexturedModalRect(x, y, 0, 0, 175, 165); 	
    	LocoLabelBox.drawTextBox();
    	ReverseMaxBox.drawTextBox();
    	ReverseSetMaxBox.drawTextBox();
    	ReverseMinBox.drawTextBox();
    	ReverseSetMinBox.drawTextBox();
    	fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Locos With", x+10, y+45, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Throttles Over:", x+10, y+55, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Will be set to:", x+10, y+75, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Locos With", x+10, y+100, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Throttles Under:", x+10, y+110, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Will be set to:", x+10, y+130, Color.WHITE.getRGB());
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	LocoLabelBox.mouseClicked(x, y, p_73864_3_);
    	ReverseMaxBox.mouseClicked(x, y, p_73864_3_);
    	ReverseSetMaxBox.mouseClicked(x, y, p_73864_3_);
    	ReverseMinBox.mouseClicked(x, y, p_73864_3_);
    	ReverseSetMinBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int bytecode){
    	if(bytecode == 1){
        	this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();
    	}else if(LocoLabelBox.isFocused()){
        	LocoLabelBox.textboxKeyTyped(key, bytecode);
        }else if(isNumeric(key,bytecode)){
    		if(ReverseMaxBox.isFocused()){
    			ReverseMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(ReverseSetMaxBox.isFocused()){
    			ReverseSetMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(ReverseMinBox.isFocused()){
    			ReverseMinBox.textboxKeyTyped(key, bytecode);
    		}else if(ReverseSetMinBox.isFocused()){
    			ReverseSetMinBox.textboxKeyTyped(key, bytecode);
    		}
        }else if(key=='e'){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    
    @Override
    public void onGuiClosed(){
    	if(ReverseMaxBox.getText().equals("")){ReverseMaxBox.setText(String.valueOf(this.ReverseMax));}
    	if(ReverseMinBox.getText().equals("")){ReverseMinBox.setText(String.valueOf(this.ReverseMin));}
    	if(ReverseSetMaxBox.getText().equals("")){ReverseSetMaxBox.setText(String.valueOf(this.ReverseSetMax));}
    	if(ReverseSetMinBox.getText().equals("")){ReverseSetMinBox.setText(String.valueOf(this.ReverseSetMin));}
    	PacketSignal ThisPacket=new PacketSignal(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord, Integer.parseInt(ReverseMaxBox.getText()), Integer.parseInt(ReverseMinBox.getText()), Integer.parseInt(ReverseSetMaxBox.getText()), Integer.parseInt(ReverseSetMinBox.getText()), LocoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(ThisPacket);
		ThisTileEntity.getWorldObj().markBlockForUpdate(ThisTileEntity.xCoord, ThisTileEntity.yCoord, ThisTileEntity.zCoord);
    }
    
	@Override
    public boolean doesGuiPauseGame(){return false;}
	
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
