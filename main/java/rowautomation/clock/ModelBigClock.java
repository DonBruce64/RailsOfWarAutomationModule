package rowautomation.clock;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBigClock extends ModelBase{
    public ModelRenderer base;
    public ModelRenderer hourHand;
    public ModelRenderer minuteHand;
    private final int textureWidth = 32;
    private final int textureHeight = 32;
    private final float scale=0.0625F;
  
    
    public ModelBigClock(){
    	base = new ModelRenderer(this, 0, 0);
    	base.setTextureSize(textureWidth, textureHeight);
    	base.setRotationPoint(0, 0, 0.99F);
    	base.addBox(-16, -16, 0, 32, 32, 0);

    	hourHand = new ModelRenderer(this, 0, 0);
    	hourHand.setTextureSize(textureWidth, textureHeight);
    	hourHand.setRotationPoint(0, 0, 0.98F);
    	hourHand.addBox(-16, -16, 0, 32, 32, 0);
    	
    	minuteHand = new ModelRenderer(this, 0, 0);
    	minuteHand.setTextureSize(textureWidth, textureHeight);
    	minuteHand.setRotationPoint(0, 0, 0.97F);
    	minuteHand.addBox(-16, -16, 0, 32, 32, 0);
    }    
    
    public void renderBase(){
    	base.render(scale);
    }
    
    public void renderHourHand(float handAngle){
    	hourHand.rotateAngleZ = handAngle;
    	hourHand.render(scale);
    }
    
    public void renderMinuteHand(float handAngle){
    	minuteHand.rotateAngleZ = handAngle;
    	minuteHand.render(scale);
    }
}