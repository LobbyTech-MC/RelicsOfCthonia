package ne.fnfal113.relicsofcthonia.listeners;

import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MobKillListener implements Listener {

    @Getter
    private final Map<AbstractRelic, List<String>> whereToDropMobMap = RelicsOfCthonia.getInstance().getRelicsRegistry().getWhereToDropMobMap();

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        if(event.isCancelled()) {
            return;
        }

        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) {
            return;
        }

        if(event.getEntity().getWorld().getEnvironment() != World.Environment.NETHER) {
            return;
        }

        LivingEntity entity = event.getEntity();

        entity.setMetadata("relic_spawned_mob", new FixedMetadataValue(RelicsOfCthonia.getInstance(), "spawned_mob"));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if(livingEntity.getKiller() == null){
            return;
        }

        if(livingEntity.getWorld().getEnvironment() != World.Environment.NETHER){
            return;
        }

        if(livingEntity.hasMetadata("relic_spawned_mob")) {
            livingEntity.removeMetadata("relic_spawned_mob", RelicsOfCthonia.getInstance());

            return;
        }

        String entityType = livingEntity.getType().name().toLowerCase();
        World world = livingEntity.getWorld();
        AtomicInteger itemDroppedCounter = new AtomicInteger(0);
        ThreadLocalRandom currentRandomThread = ThreadLocalRandom.current();

        Utils.createAsyncTask(asyncTask -> {
            Iterator<Map.Entry<AbstractRelic, List<String>>> dropIterator = getWhereToDropMobMap().entrySet().iterator();
            
            while (dropIterator.hasNext()){
                Map.Entry<AbstractRelic, List<String>> pair = dropIterator.next();
                AbstractRelic abstractRelic = pair.getKey();

                if(abstractRelic.isDisabledIn(world) || abstractRelic.isDisabled()){
                    asyncTask.cancel();

                    continue;
                }

                // check if relic mob list contains killed mob type
                if(pair.getValue().contains(entityType)) {
                    // biased probability to lower the chance of repeated values from the current random thread which utilizes same seed
                    double randomOrigin = currentRandomThread.nextDouble(0.0, 60);
                    double randomNum = ThreadLocalRandom.current().nextDouble(randomOrigin, 100);

                    if(randomNum < abstractRelic.getDropChance()) {
                        ItemStack drop = abstractRelic.setRelicConditionAndGet(true, 0);
                        
                        Utils.createSyncTask(syncTask -> livingEntity.getWorld().dropItemNaturally(livingEntity.getLocation(), drop));

                        itemDroppedCounter.getAndIncrement();
                    }

                    // limit to max 2 drops per block only
                    if(itemDroppedCounter.get() == 2){
                        asyncTask.cancel();

                        return;
                    }

                    
                }

                if(!dropIterator.hasNext()) {
                    asyncTask.cancel();    
                }
            }
        });

    }

	private Map<AbstractRelic, List<String>> getWhereToDropMobMap() {
		// TODO Auto-generated method stub
		return whereToDropMobMap;
	}

}