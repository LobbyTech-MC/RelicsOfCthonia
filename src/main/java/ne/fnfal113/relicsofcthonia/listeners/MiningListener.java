package ne.fnfal113.relicsofcthonia.listeners;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MiningListener implements Listener {

    @Getter
    private final Map<AbstractRelic, List<Material>> whereToDropMaterialMap = RelicsOfCthonia.getInstance().getRelicsRegistry().getWhereToDropMaterialMap();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()){
            return;
        }

        if(event.getPlayer().getWorld().getEnvironment() != World.Environment.NETHER) {
            return;
        }

        Block block = event.getBlock();
        Material blockBrokeType = block.getType();

        // only naturally generated blocks are accepted to prevent place and break farming
        if(block.hasMetadata("placed_block")) {
            block.removeMetadata("placed_block", RelicsOfCthonia.getInstance());

            return;
        }

        World world = event.getPlayer().getWorld();
        AtomicInteger itemDroppedCounter = new AtomicInteger(0);
        ThreadLocalRandom currentRandomThread = ThreadLocalRandom.current();

        Utils.createAsyncTask(asyncTask -> {
           Iterator<Map.Entry<AbstractRelic, List<Material>>> dropIterator = getWhereToDropMaterialMap().entrySet().iterator(); 
           
            while (dropIterator.hasNext()) {
                Map.Entry<AbstractRelic, List<Material>> pair = dropIterator.next();
                AbstractRelic abstractRelic = pair.getKey();

                if(abstractRelic.isDisabledIn(world) || abstractRelic.isDisabled()) {
                    asyncTask.cancel();

                    continue;
                }

                if(pair.getValue().contains(blockBrokeType)) {
                    // biased probability to lower the chance of repeated values from the current random thread which utilizes same seed
                    double randomOrigin = currentRandomThread.nextDouble(0.0, 60);
                    double randomNum = currentRandomThread.nextDouble(randomOrigin, 100);
                    
                    if(randomNum < abstractRelic.getDropChance()) {
                        ItemStack drop = abstractRelic.setRelicConditionAndGet(true, 0);
                        
                        Utils.createSyncTask(syncTask -> block.getWorld().dropItemNaturally(block.getLocation(), drop));

                        itemDroppedCounter.getAndIncrement();
                    }

                    // limit to max 2 drops per block only
                    if(itemDroppedCounter.get() == 2) {
                        return;
                    }
                }

                if(!dropIterator.hasNext()) {
                    asyncTask.cancel();    
                }
            }
        });

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!event.isCancelled()) {
            handlePlacedBlock(event.getBlockPlaced());
        }
    }

    @EventHandler
    public void onBlockPlacerPlaced(BlockPlacerPlaceEvent event) {
        if(!event.isCancelled()) {
            handlePlacedBlock(event.getBlock());
        }
    }


    public void handlePlacedBlock(Block block) {
        /*
         * Prevent players from block place farming any relics
         * This will be detected above in the block break event
         */
        block.setMetadata("placed_block", new FixedMetadataValue(RelicsOfCthonia.getInstance(), "placed"));
    }

}
