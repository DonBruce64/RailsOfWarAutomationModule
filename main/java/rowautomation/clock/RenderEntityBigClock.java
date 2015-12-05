package rowautomation.clock;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderEntityBigClock extends Render{
	private static final ResourceLocation face = new ResourceLocation("rowam:textures/blocks/bigclock_face.png");
	private static final ResourceLocation hourHand = new ResourceLocation("rowam:textures/blocks/bigclock_hourhand.png");
	private static final ResourceLocation minuteHand = new ResourceLocation("rowam:textures/blocks/bigclock_minutehand.png");
	private static final ModelBigClock model = new ModelBigClock();

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch){
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(yaw, 0, 1, 0);
		bindTexture(face);
		model.renderBase();
		bindTexture(hourHand);
		model.renderHourHand((float) (2*Math.PI*entity.worldObj.getWorldTime()/12000F));
		bindTexture(minuteHand);
		model.renderMinuteHand((float) (2*Math.PI*((entity.worldObj.getWorldTime()%1000)/1000F) + Math.PI));
		GL11.glRotatef(-yaw, 0, 1, 0);
		GL11.glTranslated(-x, y, -z);
		GL11.glPopMatrix();
	}

	protected ResourceLocation getEntityTexture(Entity p_110775_1_){return null;}
}
