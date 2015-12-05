package rowautomation.network;

import io.netty.buffer.ByteBuf;
import rowautomation.tileentities.TileEntityStation;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
	
public class PacketStation implements IMessage{

	private int x;
	private int y;
	private int z;
	private int OpMode;
	private int ReverseSet;
	private int TickDelay;
	private int WhistleMode;
	private int LoadingOps;
	private int UnloadingOps;
	private float WhistleVolume;
	private float WhistlePitch;
	private long ScheduledTime;
	private String LocoLabel;

	public PacketStation() { }
	
	public PacketStation(int x, int y, int z, int OpMode, int ReverseSet, int TickDelay, int WhistleMode, int LoadingOps, int UnloadingOps, float WhistleVolume, float WhistlePitch, long ScheduledTime, String LocoLabel){
		this.x=x;
		this.y=y;
		this.z=z;
		this.OpMode=OpMode;
		this.ReverseSet=ReverseSet;
		this.TickDelay=TickDelay;
		this.WhistleMode=WhistleMode;
		this.LoadingOps=LoadingOps;
		this.UnloadingOps=UnloadingOps;
		this.WhistleVolume=WhistleVolume;
		this.WhistlePitch=WhistlePitch;
		this.ScheduledTime=ScheduledTime;
		this.LocoLabel=LocoLabel;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.x=buf.readInt();
		this.y=buf.readInt();
		this.z=buf.readInt();
		this.OpMode=buf.readInt();
		this.ReverseSet=buf.readInt();
		this.TickDelay=buf.readInt();
		this.WhistleMode=buf.readInt();
		this.LoadingOps=buf.readInt();
		this.UnloadingOps=buf.readInt();
		this.WhistleVolume=buf.readFloat();
		this.WhistlePitch=buf.readFloat();
		this.ScheduledTime=buf.readLong();
		this.LocoLabel=ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.OpMode);
		buf.writeInt(this.ReverseSet);
		buf.writeInt(this.TickDelay);
		buf.writeInt(this.WhistleMode);
		buf.writeInt(this.LoadingOps);
		buf.writeInt(this.UnloadingOps);
		buf.writeFloat(this.WhistleVolume);
		buf.writeFloat(this.WhistlePitch);
		buf.writeLong(this.ScheduledTime);
		ByteBufUtils.writeUTF8String(buf, this.LocoLabel);
	}

	public static class StationPacketHandler implements IMessageHandler<PacketStation, IMessage> {
		@Override
		public IMessage onMessage(PacketStation message, MessageContext ctx) {
			if(ctx.side==Side.SERVER){
				TileEntityStation ThisTileEntity = (TileEntityStation) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
				ThisTileEntity.OpMode=message.OpMode;
				ThisTileEntity.ReverseSet=message.ReverseSet;
				ThisTileEntity.TickDelay=message.TickDelay;
				ThisTileEntity.WhistleMode=message.WhistleMode;
				ThisTileEntity.LoadingOps=message.LoadingOps;
				ThisTileEntity.UnloadingOps=message.UnloadingOps;
				ThisTileEntity.WhistleVolume=message.WhistleVolume;
				ThisTileEntity.WhistlePitch=message.WhistlePitch;
				ThisTileEntity.ScheduledTime=message.ScheduledTime;
				ThisTileEntity.LocoLabel=message.LocoLabel;
				ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
			}
			return null;
		}
	}
}
