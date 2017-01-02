package rowautomation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.event.world.WorldEvent;
import net.row.stock.core.RoWRollingStock;

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
					if(blockPos.length != 0){
						ForgeChunkManager.forceChunk(modTicket, world.getChunkFromBlockCoords(blockPos[0], blockPos[2]).getChunkCoordIntPair());//
						blockTickets.put(world.getBlock(blockPos[0], blockPos[1], blockPos[2]), modTicket);
					}else{
						ForgeChunkManager.releaseTicket(modTicket);
					}
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
		ForgeChunkManager.forceChunk(blockTicket, world.getChunkFromBlockCoords(x, z).getChunkCoordIntPair());
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
		for(int i=0; i<event.world.loadedEntityList.size(); ++i){
			stock = (Entity) event.world.loadedEntityList.get(i);
			if(stock instanceof RoWRollingStock){
				stockList.add(stock);				
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
			}
		}
	}
}


