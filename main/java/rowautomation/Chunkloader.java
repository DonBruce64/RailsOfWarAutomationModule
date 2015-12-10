package rowautomation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;


public class Chunkloader implements LoadingCallback{
	private Entity stock;
	private String stockName;
	private Ticket thisTicket;
	private Map<Entity, Ticket> stockTickets = Maps.newHashMap();
	
	public void ticketsLoaded(List<Ticket> tickets, World world){
		for(Ticket modTicket : tickets){
			if(modTicket.getModId().equals(ROWAM.MODID)){
				ForgeChunkManager.releaseTicket(modTicket);
			}
		}
	}
	
	public void setStockTicket(Entity stock, World world){
		Ticket ticket=ForgeChunkManager.requestTicket(ROWAM.instance, world, Type.ENTITY);
		ticket.bindEntity(stock);
		stockTickets.put(stock, ticket);
	}
	
	
	public void releaseStockTicket(Entity stock){
		ForgeChunkManager.releaseTicket(stockTickets.get(stock));
		stockTickets.remove(stock);
	}
	
	public void loadStockChunks(Entity stock){
		Ticket stockTicket=stockTickets.get(stock);
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX, stock.chunkCoordZ));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX, stock.chunkCoordZ+1));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX, stock.chunkCoordZ-1));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX+1, stock.chunkCoordZ));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX+1, stock.chunkCoordZ+1));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX+1, stock.chunkCoordZ-1));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX-1, stock.chunkCoordZ));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX-1, stock.chunkCoordZ+1));
		ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stock.chunkCoordX-1, stock.chunkCoordZ-1));
		stockTickets.put(stock, stockTicket);
	}
	
	@SubscribeEvent
	public void onWorldUnloadEvent(WorldEvent.Unload event){
		this.stockTickets.clear();
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		if(thisTicket==null){
			thisTicket=ForgeChunkManager.requestTicket(ROWAM.instance, event.world, Type.NORMAL);
		}
		for(Iterator<Entity> stockIterator=event.world.loadedEntityList.iterator(); stockIterator.hasNext();){
			stock=stockIterator.next();
			stockName=stock.getClass().getName();
			if(stockName.startsWith("net.row.stock.")){
				stock.fallDistance=0;
				if(!stockName.equals("net.row.stock.cart.CartNT")){
					if(stock.timeUntilPortal>0){
						stock.timeUntilPortal=Math.max(stock.timeUntilPortal-5, 0);
					}
				}	
				if(stock.riddenByEntity!=null){
					if(!(stock.riddenByEntity instanceof EntityPlayer)){
						stock.riddenByEntity.motionY=0;	
						if(stockName.contains("cart")){
							stock.riddenByEntity.yOffset=0.6F;
						}else if(stockName.contains("loco")){
							stock.riddenByEntity.yOffset=-0.45F;
						}
					}
				}
				if(!event.world.isRemote){
					if(stockTickets.containsKey(stock)){
						releaseStockTicket(stock);
					}
					setStockTicket(stock, event.world);
					loadStockChunks(stock);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event){
		if(event.player.ridingEntity != null && event.player.worldObj.isRemote){
			if(event.player.ridingEntity.getClass().getName().startsWith("net.row.stock.")){
				event.player.rotationYaw += (event.player.ridingEntity.rotationYaw-event.player.ridingEntity.prevRotationYaw)/2;
			}
		}
	}
}


