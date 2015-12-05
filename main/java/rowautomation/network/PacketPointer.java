package rowautomation.network;

import rowautomation.tileentities.TileEntityPointer;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
	
public class PacketPointer implements IMessage{

	private int x;
	private int y;
	private int z;
	private boolean Locked;
	private boolean Redstone;
	private boolean Spring;
	private boolean Switched;
	private String LocoLabel;

	public PacketPointer() { }
	
	public PacketPointer(int x, int y, int z, boolean Locked, boolean Redstone, boolean Spring, boolean Switched, String LocoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.Locked=Locked;
		this.Redstone=Redstone;
		this.Spring=Spring;
		this.Switched=Switched;
		this.LocoLabel=LocoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.Locked=buf.readBoolean();
		this.Redstone=buf.readBoolean();
		this.Spring=buf.readBoolean();
		this.Switched=buf.readBoolean();
		this.LocoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeBoolean(this.Locked);
		buf.writeBoolean(this.Redstone);
		buf.writeBoolean(this.Spring);
		buf.writeBoolean(this.Switched);
		ByteBufUtils.writeUTF8String(buf, this.LocoLabel);
	}

	public static class PointerPacketHandler implements IMessageHandler<PacketPointer, IMessage> {
		@Override
		public IMessage onMessage(PacketPointer message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntityPointer ThisTileEntity = (TileEntityPointer) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				ThisTileEntity.Locked=message.Locked;
				ThisTileEntity.Redstone=message.Redstone;
				ThisTileEntity.Spring=message.Spring;
				ThisTileEntity.Switched=message.Switched;
				ThisTileEntity.LocoLabel=message.LocoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
