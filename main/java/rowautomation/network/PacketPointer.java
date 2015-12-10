package rowautomation.network;

import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntityPointer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
	
public class PacketPointer implements IMessage{

	private int x;
	private int y;
	private int z;
	private boolean locked;
	private boolean redstone;
	private boolean spring;
	private boolean switched;
	private String locoLabel;

	public PacketPointer() { }
	
	public PacketPointer(int x, int y, int z, boolean locked, boolean redstone, boolean spring, boolean switched, String locoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.locked=locked;
		this.redstone=redstone;
		this.spring=spring;
		this.switched=switched;
		this.locoLabel=locoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.locked=buf.readBoolean();
		this.redstone=buf.readBoolean();
		this.spring=buf.readBoolean();
		this.switched=buf.readBoolean();
		this.locoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeBoolean(this.locked);
		buf.writeBoolean(this.redstone);
		buf.writeBoolean(this.spring);
		buf.writeBoolean(this.switched);
		ByteBufUtils.writeUTF8String(buf, this.locoLabel);
	}

	public static class PointerPacketHandler implements IMessageHandler<PacketPointer, IMessage> {
		@Override
		public IMessage onMessage(PacketPointer message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntityPointer thisTileEntity = (TileEntityPointer) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				thisTileEntity.locked=message.locked;
				thisTileEntity.redstone=message.redstone;
				thisTileEntity.spring=message.spring;
				thisTileEntity.switched=message.switched;
				thisTileEntity.locoLabel=message.locoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
