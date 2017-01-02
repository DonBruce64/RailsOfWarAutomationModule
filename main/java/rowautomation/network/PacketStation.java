package rowautomation.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntityStation;
	
public class PacketStation implements IMessage{

	private int x;
	private int y;
	private int z;
	private int opMode;
	private int reverseSet;
	private int regulatorSet;
	private int tickDelay;
	private int whistleMode;
	private int loadingOps;
	private int unloadingOps;
	private float whistleVolume;
	private float whistlePitch;
	private long scheduledTime;
	private String locoLabel;

	public PacketStation() { }
	public PacketStation(int x, int y, int z, int opMode, int reverseSet, int regulatorSet, int tickDelay, int whistleMode, int loadingOps, int unloadingOps, float whistleVolume, float whistlePitch, long scheduledTime, String locoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.opMode=opMode;
		this.reverseSet=reverseSet;
		this.regulatorSet=regulatorSet;
		this.tickDelay=tickDelay;
		this.whistleMode=whistleMode;
		this.loadingOps=loadingOps;
		this.unloadingOps=unloadingOps;
		this.whistleVolume=whistleVolume;
		this.whistlePitch=whistlePitch;
		this.scheduledTime=scheduledTime;
		this.locoLabel=locoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.opMode=buf.readInt();
		this.reverseSet=buf.readInt();
		this.regulatorSet=buf.readInt();
		this.tickDelay=buf.readInt();
		this.whistleMode=buf.readInt();
		this.loadingOps=buf.readInt();
		this.unloadingOps=buf.readInt();
		this.whistleVolume=buf.readFloat();
		this.whistlePitch=buf.readFloat();
		this.scheduledTime=buf.readLong();
		this.locoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.opMode);
		buf.writeInt(this.reverseSet);
		buf.writeInt(this.regulatorSet);
		buf.writeInt(this.tickDelay);
		buf.writeInt(this.whistleMode);
		buf.writeInt(this.loadingOps);
		buf.writeInt(this.unloadingOps);
		buf.writeFloat(this.whistleVolume);
		buf.writeFloat(this.whistlePitch);
		buf.writeLong(this.scheduledTime);
		ByteBufUtils.writeUTF8String(buf, this.locoLabel);
	}

	public static class StationPacketHandler implements IMessageHandler<PacketStation, IMessage> {
		@Override
		public IMessage onMessage(PacketStation message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntityStation thisTileEntity = (TileEntityStation) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				thisTileEntity.opMode=message.opMode;
				thisTileEntity.reverseSet=Math.max(Math.min(message.reverseSet, 100), -100);
				thisTileEntity.regulatorSet=Math.max(Math.min(message.regulatorSet, 100), 0);
				thisTileEntity.tickDelay=message.tickDelay;
				thisTileEntity.whistleMode=message.whistleMode;
				thisTileEntity.loadingOps=message.loadingOps;
				thisTileEntity.unloadingOps=message.unloadingOps;
				thisTileEntity.whistleVolume=message.whistleVolume;
				thisTileEntity.whistlePitch=message.whistlePitch;
				thisTileEntity.scheduledTime=message.scheduledTime;
				thisTileEntity.locoLabel=message.locoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
