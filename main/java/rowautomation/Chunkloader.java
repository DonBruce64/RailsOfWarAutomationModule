package rowautomation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
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
	private Map<Entity, Ticket> stockTickets = Maps.newHashMap();
	private static Map<Block, Ticket> blockTickets = Maps.newHashMap();
	public static List<Entity> stockList  = new ArrayList();
	
	public void ticketsLoaded(List<Ticket> tickets, World world){
		for(Ticket modTicket : tickets){
			if(modTicket.getModId().equals(ROWAM.MODID)){
				modTicket.setChunkListDepth(1);
				if(modTicket.getType().equals(Type.ENTITY)){
					stockTickets.put(modTicket.getEntity(), modTicket);
				}else{
					int[] blockPos = modTicket.getModData().getIntArray("blockPos");
					ForgeChunkManager.forceChunk(modTicket, new ChunkCoordIntPair((blockPos[0]-16)/16, (blockPos[2]-16)/16));
					blockTickets.put(world.getBlock(blockPos[0], blockPos[1], blockPos[2]), modTicket);
				}
			}
		}
	}
	
	private void setStockTicket(Entity stock, World world){
		Ticket ticket = ForgeChunkManager.requestTicket(ROWAM.instance, world, Type.ENTITY);
		ticket.setChunkListDepth(1);
		ticket.bindEntity(stock);
		stockTickets.put(stock, ticket);
	}
	
	public static void addBlockTicket(Block block, World world, int x, int y, int z){
		Ticket blockTicket = ForgeChunkManager.requestTicket(ROWAM.instance, world, Type.NORMAL);
		blockTicket.setChunkListDepth(1);
		ForgeChunkManager.forceChunk(blockTicket, new ChunkCoordIntPair((x-16)/16, (z-16)/16));
		blockTicket.getModData().setIntArray("blockPos", new int[]{x, y, z});		
		blockTickets.put(block, blockTicket);
	}
	
	public static void removeBlockTicket(Block block){
		if(blockTickets.containsKey(block)){
			ForgeChunkManager.releaseTicket(blockTickets.get(block));
			blockTickets.remove(block);
		}
	}
	
	@SubscribeEvent
	public void onWorldUnloadEvent(WorldEvent.Unload event){
		this.stockTickets.clear();
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		stockList.clear();
		Iterator<Entity> stockIterator = event.world.loadedEntityList.iterator();
		while(stockIterator.hasNext()){
			stock = stockIterator.next();
			stockName = stock.getClass().getName();
			if(stockName.startsWith("net.row.stock.")){
				stockList.add(stock);
				stock.fallDistance=0;
				
				if(!event.world.isRemote){
					if(!stockTickets.containsKey(stock)){
						setStockTicket(stock, event.world);
					}else if(stock.isDead){
						ForgeChunkManager.releaseTicket(stockTickets.get(stock));
						stockTickets.remove(stock);
					}else{
						Ticket stockTicket = stockTickets.get(stock);
						ForgeChunkManager.forceChunk(stockTicket, new ChunkCoordIntPair(stockTicket.getEntity().chunkCoordX, stockTicket.getEntity().chunkCoordZ));
						stockTickets.put(stock, stockTicket);
					}
				}
				
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


