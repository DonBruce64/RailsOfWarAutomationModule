package rowautomation.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntitySignal;
	
public class PacketSignal implements IMessage{

	private int x;
	private int y;
	private int z;
	private String locoLabel;
	private int angle;
	private int reverseMax;
	private int reverseMin;
	private int reverseSet;
	private int regulatorMax;
	private int regulatorMin;
	private int regulatorSet;

	public PacketSignal() { }
	
	public PacketSignal(int x, int y, int z, String locoLabel, int angle, int reverseMax, int reverseMin, int reverseSet, int regulatorMax, int regulatorMin, int regulatorSet){
		this.x=x;
		this.y=y;
		this.z=z;
		this.locoLabel=locoLabel;
		this.angle=angle;
		this.reverseMax=reverseMax;
		this.reverseMin=reverseMin;
		this.reverseSet=reverseSet;
		this.regulatorMax=regulatorMax;
		this.regulatorMin=regulatorMin;
		this.regulatorSet=regulatorSet;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.locoLabel=ByteBufUtils.readUTF8String(buf);
		this.angle=buf.readInt();
		this.reverseMax=buf.readInt();
		this.reverseMin=buf.readInt();
		this.reverseSet=buf.readInt();
		this.regulatorMax=buf.readInt();
		this.regulatorMin=buf.readInt();
		this.regulatorSet=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		ByteBufUtils.writeUTF8String(buf, this.locoLabel);
		buf.writeInt(this.angle);
		buf.writeInt(this.reverseMax);
		buf.writeInt(this.reverseMin);
		buf.writeInt(this.reverseSet);
		buf.writeInt(this.regulatorMax);
		buf.writeInt(this.regulatorMin);
		buf.writeInt(this.regulatorSet);
	}

	public static class SignalPacketHandler implements IMessageHandler<PacketSignal, IMessage> {
		@Override
		public IMessage onMessage(PacketSignal message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntitySignal thisTileEntity = (TileEntitySignal) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				thisTileEntity.locoLabel=message.locoLabel;
				thisTileEntity.angle=message.angle;
				thisTileEntity.reverseMax=message.reverseMax;
				thisTileEntity.reverseMin=message.reverseMin;
				thisTileEntity.reverseSet=Math.max(Math.min(message.reverseSet, 100), -100);
				thisTileEntity.regulatorMax=message.regulatorMax;
				thisTileEntity.regulatorMin=message.regulatorMin;
				thisTileEntity.regulatorSet=Math.max(Math.min(message.regulatorSet, 100), 0);
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
