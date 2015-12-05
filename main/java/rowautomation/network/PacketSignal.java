package rowautomation.network;

import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntitySignal;
import rowautomation.tileentities.TileEntityStation;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
	
public class PacketSignal implements IMessage{

	private int x;
	private int y;
	private int z;
	private int ReverseMax;
	private int ReverseMin;
	private int ReverseSetMax;
	private int ReverseSetMin;
	private String LocoLabel;

	public PacketSignal() { }
	
	public PacketSignal(int x, int y, int z, int ReverseMax, int ReverseMin, int ReverseSetMax, int ReverseSetMin, String LocoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.ReverseMax=ReverseMax;
		this.ReverseMin=ReverseMin;
		this.ReverseSetMax=ReverseSetMax;
		this.ReverseSetMin=ReverseSetMin;
		this.LocoLabel=LocoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.ReverseMax=buf.readInt();
		this.ReverseMin=buf.readInt();
		this.ReverseSetMax=buf.readInt();
		this.ReverseSetMin=buf.readInt();
		this.LocoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.ReverseMax);
		buf.writeInt(this.ReverseMin);
		buf.writeInt(this.ReverseSetMax);
		buf.writeInt(this.ReverseSetMin);
		ByteBufUtils.writeUTF8String(buf, this.LocoLabel);
	}

	public static class SignalPacketHandler implements IMessageHandler<PacketSignal, IMessage> {
		@Override
		public IMessage onMessage(PacketSignal message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntitySignal ThisTileEntity = (TileEntitySignal) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				ThisTileEntity.ReverseMax=message.ReverseMax;
				ThisTileEntity.ReverseMin=message.ReverseMin;
				ThisTileEntity.ReverseSetMax=message.ReverseSetMax;
				ThisTileEntity.ReverseSetMin=message.ReverseSetMin;
				ThisTileEntity.LocoLabel=message.LocoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
