package rowautomation.guis;

import java.awt.Color;

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
	private int reverseSet;
	private String locoLabel;

	private static final ResourceLocation background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiTextField locoLabelBox;
	private GuiTextField reverseMaxBox;
	private GuiTextField reverseMinBox;
	private GuiTextField reverseSetBox;	

	public GUISignal(TileEntitySignal thisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.thisTileEntity=thisTileEntity;
		this.locoLabel=thisTileEntity.locoLabel;
		this.reverseMax=thisTileEntity.reverseMax;
		this.reverseMin=thisTileEntity.reverseMin;
		this.reverseSet=thisTileEntity.reverseSet;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	locoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	reverseMaxBox = new GuiTextField(fontRendererObj, x+90, y+50, 40, 15);
    	reverseMinBox = new GuiTextField(fontRendererObj, x+90, y+70, 40, 15);
    	reverseSetBox = new GuiTextField(fontRendererObj, x+90, y+90, 40, 15);
    	reverseMaxBox.setMaxStringLength(3);
    	reverseMinBox.setMaxStringLength(3);
    	reverseSetBox.setMaxStringLength(4);
    	locoLabelBox.setText(locoLabel);
    	reverseMaxBox.setText(String.valueOf(reverseMax));
    	reverseMinBox.setText(String.valueOf(reverseMin));
    	reverseSetBox.setText(String.valueOf(reverseSet));
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
		this.mc.getTextureManager().bindTexture(background);
    	drawTexturedModalRect(x, y, 0, 0, 175, 165); 	
    	locoLabelBox.drawTextBox();
    	reverseMaxBox.drawTextBox();
    	reverseMinBox.drawTextBox();
    	reverseSetBox.drawTextBox();
    	fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Locos With", x+10, y+45, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Throttles Over:", x+10, y+55, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("And Under:", x+10, y+75, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Will be set to:", x+10, y+95, Color.WHITE.getRGB());
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	locoLabelBox.mouseClicked(x, y, p_73864_3_);
    	reverseMaxBox.mouseClicked(x, y, p_73864_3_);
    	reverseMinBox.mouseClicked(x, y, p_73864_3_);
    	reverseSetBox.mouseClicked(x, y, p_73864_3_);
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
    		}else if(reverseMinBox.isFocused()){
    			reverseMinBox.textboxKeyTyped(key, bytecode);
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
    	if(reverseMaxBox.getText().equals("")){reverseMaxBox.setText(String.valueOf(this.reverseMax));}
    	if(reverseMinBox.getText().equals("")){reverseMinBox.setText(String.valueOf(this.reverseMin));}
    	if(reverseSetBox.getText().equals("")){reverseSetBox.setText(String.valueOf(this.reverseSet));}
    	PacketSignal ThisPacket=new PacketSignal(thisTileEntity.xCoord, thisTileEntity.yCoord, thisTileEntity.zCoord, Integer.parseInt(reverseMaxBox.getText()), Integer.parseInt(reverseMinBox.getText()), Integer.parseInt(reverseSetBox.getText()), locoLabelBox.getText());
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
