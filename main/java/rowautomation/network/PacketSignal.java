package rowautomation.network;

import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntitySignal;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
	
public class PacketSignal implements IMessage{

	private int x;
	private int y;
	private int z;
	private int reverseMax;
	private int reverseMin;
	private int reverseSetMax;
	private int reverseSetMin;
	private String locoLabel;

	public PacketSignal() { }
	
	public PacketSignal(int x, int y, int z, int reverseMax, int reverseMin, int reverseSetMax, int reverseSetMin, String locoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.reverseMax=reverseMax;
		this.reverseMin=reverseMin;
		this.reverseSetMax=reverseSetMax;
		this.reverseSetMin=reverseSetMin;
		this.locoLabel=locoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.reverseMax=buf.readInt();
		this.reverseMin=buf.readInt();
		this.reverseSetMax=buf.readInt();
		this.reverseSetMin=buf.readInt();
		this.locoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.reverseMax);
		buf.writeInt(this.reverseMin);
		buf.writeInt(this.reverseSetMax);
		buf.writeInt(this.reverseSetMin);
		ByteBufUtils.writeUTF8String(buf, this.locoLabel);
	}

	public static class SignalPacketHandler implements IMessageHandler<PacketSignal, IMessage> {
		@Override
		public IMessage onMessage(PacketSignal message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntitySignal thisTileEntity = (TileEntitySignal) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				thisTileEntity.reverseMax=message.reverseMax;
				thisTileEntity.reverseMin=message.reverseMin;
				thisTileEntity.reverseSetMax=Math.max(message.reverseSetMax, 100);
				thisTileEntity.reverseSetMin=Math.min(message.reverseSetMin, -100);
				thisTileEntity.locoLabel=message.locoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
