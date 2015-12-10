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
	private TileEntitySignal thisTileEntity;
	private int reverseMax;
	private int reverseMin;
	private int reverseSetMax;
	private int reverseSetMin;
	private String locoLabel;

	private static final ResourceLocation background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiTextField locoLabelBox;
	private GuiTextField reverseMaxBox;
	private GuiTextField reverseSetMaxBox;
	private GuiTextField reverseMinBox;
	private GuiTextField reverseSetMinBox;	

	public GUISignal(TileEntitySignal thisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.thisTileEntity=thisTileEntity;
		this.locoLabel=thisTileEntity.locoLabel;
		this.reverseMax=thisTileEntity.reverseMax;
		this.reverseSetMax=thisTileEntity.reverseSetMax;
		this.reverseMin=thisTileEntity.reverseMin;
		this.reverseSetMin=thisTileEntity.reverseSetMin;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	locoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	reverseMaxBox = new GuiTextField(fontRendererObj, x+90, y+50, 40, 15);
    	reverseSetMaxBox = new GuiTextField(fontRendererObj, x+90, y+70, 40, 15);
    	reverseMinBox = new GuiTextField(fontRendererObj, x+90, y+105, 40, 15);
    	reverseSetMinBox = new GuiTextField(fontRendererObj, x+90, y+125, 40, 15);	
    	reverseMaxBox.setMaxStringLength(3);
    	reverseSetMaxBox.setMaxStringLength(3);
    	reverseMinBox.setMaxStringLength(3);
    	reverseSetMinBox.setMaxStringLength(3);
    	locoLabelBox.setText(locoLabel);
    	reverseMaxBox.setText(String.valueOf(reverseMax));
    	reverseSetMaxBox.setText(String.valueOf(reverseSetMax));
    	reverseMinBox.setText(String.valueOf(reverseMin));
    	reverseSetMinBox.setText(String.valueOf(reverseSetMin));
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(background);
    	drawTexturedModalRect(x, y, 0, 0, 175, 165); 	
    	locoLabelBox.drawTextBox();
    	reverseMaxBox.drawTextBox();
    	reverseSetMaxBox.drawTextBox();
    	reverseMinBox.drawTextBox();
    	reverseSetMinBox.drawTextBox();
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
    	locoLabelBox.mouseClicked(x, y, p_73864_3_);
    	reverseMaxBox.mouseClicked(x, y, p_73864_3_);
    	reverseSetMaxBox.mouseClicked(x, y, p_73864_3_);
    	reverseMinBox.mouseClicked(x, y, p_73864_3_);
    	reverseSetMinBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int bytecode){
    	if(bytecode == 1){
        	this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();
    	}else if(locoLabelBox.isFocused()){
        	locoLabelBox.textboxKeyTyped(key, bytecode);
        }else if(isNumeric(key,bytecode)){
    		if(reverseMaxBox.isFocused()){
    			reverseMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseSetMaxBox.isFocused()){
    			reverseSetMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseMinBox.isFocused()){
    			reverseMinBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseSetMinBox.isFocused()){
    			reverseSetMinBox.textboxKeyTyped(key, bytecode);
    		}
        }else if(key=='e'){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    
    @Override
    public void onGuiClosed(){
    	if(reverseMaxBox.getText().equals("")){reverseMaxBox.setText(String.valueOf(this.reverseMax));}
    	if(reverseMinBox.getText().equals("")){reverseMinBox.setText(String.valueOf(this.reverseMin));}
    	if(reverseSetMaxBox.getText().equals("")){reverseSetMaxBox.setText(String.valueOf(this.reverseSetMax));}
    	if(reverseSetMinBox.getText().equals("")){reverseSetMinBox.setText(String.valueOf(this.reverseSetMin));}
    	PacketSignal ThisPacket=new PacketSignal(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord, Integer.parseInt(reverseMaxBox.getText()), Integer.parseInt(reverseMinBox.getText()), Integer.parseInt(reverseSetMaxBox.getText()), Integer.parseInt(reverseSetMinBox.getText()), locoLabelBox.getText());
    	ROWAM.ROWAMNet.sendToServer(ThisPacket);
		thisTileEntity.getWorldObj().markBlockForUpdate(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord);
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
