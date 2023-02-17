package ne.fnfal113.relicsofcthonia.listeners;

import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MiningListener implements Listener {

    @Getter
    private final Map<AbstractRelic, List<Material>> whereToDropMaterialMap = RelicsOfCthonia.getInstance().getRelicsRegistry().getWhereToDropMaterialMap();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }

        if(event.getPlayer().getWorld().getEnvironment() != World.Environment.NETHER){
            return;
        }

        Block block = event.getBlock();
        Material blockBrokeType = block.getType();

        // only naturally generated blocks are accepted to prevent place and break farming
        if(block.hasMetadata("placed_block")){
            block.removeMetadata("placed_block", RelicsOfCthonia.getInstance());

            return;
        }

        World world = event.getPlayer().getWorld();
        ThreadLocalRandom currentRandomThread = ThreadLocalRandom.current();
        AtomicInteger i = new AtomicInteger(0);

        Utils.createAsyncTask(asyncTask -> {
           for (Map.Entry<AbstractRelic, List<Material>> entry: getWhereToDropMaterialMap().entrySet()){
               AbstractRelic abstractRelic = entry.getKey();

               if(abstractRelic.isDisabledIn(world) || abstractRelic.isDisabled()){
                   continue;
               }

               if(entry.getValue().contains(blockBrokeType)){
                   // randomize twice since current thread random is using same seed for every block break in this loop
                   double randomOrigin = currentRandomThread.nextDouble(0.0, 60);
                   double randomNum = currentRandomThread.nextDouble(randomOrigin, 100);

                   if(randomNum < abstractRelic.getDropChance()) {
                       ItemStack drop = abstractRelic.setRelicCondition(true, 0);
                       Utils.createSyncTask(syncTask -> block.getWorld().dropItemNaturally(block.getLocation(), drop));

                       i.getAndIncrement();
                   }

                   // limit to max 2 drops per block only
                   if(i.get() == 2){
                       return;
                   }
               }
           }
        });

    }

    private Map<AbstractRelic, List<Material>> getWhereToDropMaterialMap() {
		// TODO Auto-generated method stub
		return whereToDropMaterialMap;
	}

	@EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }

        /*
         * Prevent players from block place farming any relics
         * This will be detected above in the block break event
         */
        event.getBlockPlaced().setMetadata("placed_block", new FixedMetadataValue(RelicsOfCthonia.getInstance(), "placed"));
    }

}
