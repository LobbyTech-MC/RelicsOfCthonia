package ne.fnfal113.relicsofcthonia.listeners;

import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class MobKillListener implements Listener {

    @Getter
    private final Map<AbstractRelic, List<String>> whereToDropMobMap = RelicsOfCthonia.getInstance().getRelicsRegistry().getWhereToDropMobMap();

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        if(event.isCancelled()){
            return;
        }

        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER){
            return;
        }

        if(event.getEntity().getWorld().getEnvironment() != World.Environment.NETHER){
            return;
        }

        LivingEntity entity = event.getEntity();

        entity.setMetadata("relic_spawned_mob", new FixedMetadataValue(RelicsOfCthonia.getInstance(), "spawned_mob"));
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        if(livingEntity.getKiller() == null){
            return;
        }

        if(livingEntity.getWorld().getEnvironment() != World.Environment.NETHER){
            return;
        }

        if(livingEntity.hasMetadata("relic_spawned_mob")){
            livingEntity.removeMetadata("relic_spawned_mob", RelicsOfCthonia.getInstance());

            return;
        }

        String entityType = livingEntity.getType().name().toLowerCase();
        World world = livingEntity.getWorld();
        ThreadLocalRandom currentRandomThread = ThreadLocalRandom.current();
        AtomicInteger i = new AtomicInteger(0);

        Utils.createAsyncTask(asyncTask -> {
            for (Map.Entry<AbstractRelic, List<String>> entry: getWhereToDropMobMap().entrySet()){
                AbstractRelic abstractRelic = entry.getKey();

                if(abstractRelic.isDisabledIn(world) || abstractRelic.isDisabled()){
                    continue;
                }

                if(entry.getValue().contains(entityType)){
                    double randomOrigin = currentRandomThread.nextDouble(0.0, 60);
                    double randomNum = currentRandomThread.nextDouble(randomOrigin, 100);

                    if(randomNum < abstractRelic.getDropChance()) {
                        ItemStack drop = abstractRelic.setRelicCondition(true, 0);
                        Utils.createSyncTask(syncTask -> livingEntity.getWorld().dropItemNaturally(livingEntity.getLocation(), drop));

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

	private Map<AbstractRelic, List<String>> getWhereToDropMobMap() {
		// TODO Auto-generated method stub
		return whereToDropMobMap;
	}

}