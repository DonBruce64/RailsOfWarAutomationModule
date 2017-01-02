package rowautomation.guis;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import rowautomation.ROWAM;
import rowautomation.network.PacketSignal;
import rowautomation.tileentities.TileEntitySignal;


public class GUISignal extends GuiScreen{
	private TileEntitySignal thisTileEntity;
	private int reverseMax;
	private int reverseMin;
	private int reverseSet;
	private int regulatorMax;
	private int regulatorMin;
	private int regulatorSet;
	private int angle;
	private String locoLabel;

	private static final ResourceLocation background = new ResourceLocation("rowam", "textures/gui/gui.png");	
	private GuiTextField locoLabelBox;
	private GuiTextField angleBox;
	private GuiTextField reverseMaxBox;
	private GuiTextField reverseMinBox;
	private GuiTextField reverseSetBox;
	private GuiTextField regulatorMaxBox;
	private GuiTextField regulatorMinBox;
	private GuiTextField regulatorSetBox;

	public GUISignal(TileEntitySignal thisTileEntity){
		Keyboard.enableRepeatEvents(true);
		this.allowUserInput=true;
		this.thisTileEntity=thisTileEntity;
		this.locoLabel=thisTileEntity.locoLabel;
		this.angle=thisTileEntity.angle;
		this.reverseMax=thisTileEntity.reverseMax;
		this.reverseMin=thisTileEntity.reverseMin;
		this.reverseSet=thisTileEntity.reverseSet;
		this.regulatorMax=thisTileEntity.regulatorMax;
		this.regulatorMin=thisTileEntity.regulatorMin;
		this.regulatorSet=thisTileEntity.regulatorSet;
	}

	@Override 
	public void initGui(){
    	int x=(this.width - 165)/2;
    	int y=(this.height - 165)/2;
    	int line = 0;
    	locoLabelBox = new GuiTextField(fontRendererObj, x+80, y+10, 80, 15);
    	angleBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 30, 15);
    	reverseMaxBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);
    	reverseMinBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);
    	reverseSetBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);
    	regulatorMaxBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);
    	regulatorMinBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);
    	regulatorSetBox = new GuiTextField(fontRendererObj, x+120, y+40+17*(line++), 40, 15);    	
    	
    	angleBox.setMaxStringLength(3);
    	reverseMaxBox.setMaxStringLength(3);
    	reverseMinBox.setMaxStringLength(3);
    	reverseSetBox.setMaxStringLength(4);
    	regulatorMaxBox.setMaxStringLength(3);
    	regulatorMinBox.setMaxStringLength(3);
    	regulatorSetBox.setMaxStringLength(3);
    	
    	locoLabelBox.setText(locoLabel);
    	angleBox.setText(String.valueOf(angle));
    	reverseMaxBox.setText(String.valueOf(reverseMax));
    	reverseMinBox.setText(String.valueOf(reverseMin));
    	reverseSetBox.setText(String.valueOf(reverseSet));
    	regulatorMaxBox.setText(String.valueOf(regulatorMax));
    	regulatorMinBox.setText(String.valueOf(regulatorMin));
    	regulatorSetBox.setText(String.valueOf(regulatorSet));
	}
	
	
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
    	int x=(this.width - 175)/2;
    	int y=(this.height - 165)/2;
    	int line = 0;
		this.mc.getTextureManager().bindTexture(background);
    	drawTexturedModalRect(x, y, 0, 0, 175, 165); 	
    	locoLabelBox.drawTextBox();
    	
    	angleBox.drawTextBox();
    	reverseMaxBox.drawTextBox();
    	reverseMinBox.drawTextBox();
    	reverseSetBox.drawTextBox();
    	regulatorMaxBox.drawTextBox();
    	regulatorMinBox.drawTextBox();
    	regulatorSetBox.drawTextBox();
    	
    	fontRendererObj.drawStringWithShadow("Loco Label:", x+10, y+15, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Locos:", x+10, y+35, Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Heading at angle:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("With Reverse over:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("And Reverse under:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Will be set to:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("With Regulator over:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("And Regulator under:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    	fontRendererObj.drawStringWithShadow("Will be set to:", x+10, y+45+17*(line++), Color.WHITE.getRGB());
    }
    
    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_){
    	super.mouseClicked(x, y, p_73864_3_);
    	locoLabelBox.mouseClicked(x, y, p_73864_3_);
    	angleBox.mouseClicked(x, y, p_73864_3_);
    	reverseMaxBox.mouseClicked(x, y, p_73864_3_);
    	reverseMinBox.mouseClicked(x, y, p_73864_3_);
    	reverseSetBox.mouseClicked(x, y, p_73864_3_);
    	regulatorMaxBox.mouseClicked(x, y, p_73864_3_);
    	regulatorMinBox.mouseClicked(x, y, p_73864_3_);
    	regulatorSetBox.mouseClicked(x, y, p_73864_3_);
    }
    
    @Override
    protected void keyTyped(char key, int bytecode){
    	if(bytecode == 1){
        	this.mc.displayGuiScreen((GuiScreen)null);
        	this.mc.setIngameFocus();
    	}else if(locoLabelBox.isFocused()){
        	locoLabelBox.textboxKeyTyped(key, bytecode);
        }else if(isNumeric(key,bytecode)){
        	if(angleBox.isFocused()){
    			angleBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseMaxBox.isFocused()){
    			reverseMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseMinBox.isFocused()){
    			reverseMinBox.textboxKeyTyped(key, bytecode);
    		}else if(reverseSetBox.isFocused()){
    			reverseSetBox.textboxKeyTyped(key, bytecode);
    		}else if(regulatorMaxBox.isFocused()){
    			regulatorMaxBox.textboxKeyTyped(key, bytecode);
    		}else if(regulatorMinBox.isFocused()){
    			regulatorMinBox.textboxKeyTyped(key, bytecode);
    		}else if(regulatorSetBox.isFocused()){
    			regulatorSetBox.textboxKeyTyped(key, bytecode);
    		}
        }else if(key=='e'){
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
    
    @Override
    public void onGuiClosed(){
    	if(angleBox.getText().equals("")){angleBox.setText(String.valueOf(this.angle));}
    	if(reverseMaxBox.getText().equals("")){reverseMaxBox.setText(String.valueOf(this.reverseMax));}
    	if(reverseMinBox.getText().equals("")){reverseMinBox.setText(String.valueOf(this.reverseMin));}
    	if(reverseSetBox.getText().equals("")){reverseSetBox.setText(String.valueOf(this.reverseSet));}
    	if(regulatorMaxBox.getText().equals("")){regulatorMaxBox.setText(String.valueOf(this.regulatorMax));}
    	if(regulatorMinBox.getText().equals("")){regulatorMinBox.setText(String.valueOf(this.regulatorMin));}
    	if(regulatorSetBox.getText().equals("")){regulatorSetBox.setText(String.valueOf(this.regulatorSet));}
    	PacketSignal ThisPacket=new PacketSignal(
    			thisTileEntity.xCoord, 
    			thisTileEntity.yCoord, 
    			thisTileEntity.zCoord,
    			locoLabelBox.getText(),
    			Integer.parseInt(angleBox.getText()),
    			Integer.parseInt(reverseMaxBox.getText()), 
    			Integer.parseInt(reverseMinBox.getText()), 
    			Integer.parseInt(reverseSetBox.getText()), 
    			Integer.parseInt(regulatorMaxBox.getText()), 
    			Integer.parseInt(regulatorMinBox.getText()), 
    			Integer.parseInt(regulatorSetBox.getText()) 
    	);
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
