package ne.fnfal113.relicsofcthonia.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class RelicPlaceBreakListener implements Listener {

    // Due to synchronization bugs with block storage when spam clicking
    // it leads to bugs and unforeseen consequences. Picking up relics is
    // temporarily disabled until block storage rewrite
    /*@EventHandler
    public void onRelicClick(PlayerInteractEvent event){
        if(event.isCancelled()){
            return;
        }

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        if(event.getClickedBlock() == null){
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        Optional<SlimefunItem> relic = Optional.ofNullable(BlockStorage.check(clickedBlock));

        relic.ifPresent(item -> {
            if(item instanceof AbstractRelic){
                AbstractRelic abstractRelic = (AbstractRelic) item;
                String value = BlockStorage.getLocationInfo(clickedBlock.getLocation(), "relic_condition");

                if(!Objects.equals(BlockStorage.getLocationInfo(clickedBlock.getLocation(), "owner"), player.getUniqueId().toString())){
                    player.sendMessage(Utils.colorTranslator("&6你不是这件遗物的拥有者!"));

                    return;
                }

                ItemStack itemStack = abstractRelic.setRelicCondition(false, Integer.parseInt(value));

                clickedBlock.setType(Material.AIR);
                BlockStorage.clearBlockInfo(clickedBlock);

                if(player.getInventory().firstEmpty() == -1){
                    clickedBlock.getWorld().dropItemNaturally(clickedBlock.getLocation(), itemStack);

                    return;
                }

                player.getInventory().addItem(itemStack);

                event.setCancelled(true);
            }
        });

    }*/

    @EventHandler
    public void onRelicPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }

        ItemStack itemInHand = event.getItemInHand();

        Optional<SlimefunItem> relic = Optional.ofNullable(SlimefunItem.getByItem(itemInHand));

        relic.ifPresent(item -> {
            if(item instanceof AbstractRelic) {
                Utils.sendRelicMessage("You placed a relic, it will not drop anything once broken!", event.getPlayer());
            }
        });

    }

    @EventHandler
    public void onRelicBreak(BlockBreakEvent event) {
        if(event.isCancelled()){
            return;
        }

        Block blockBroken = event.getBlock();

        Optional<SlimefunItem> relic = Optional.ofNullable(BlockStorage.check(blockBroken));

        relic.ifPresent(item -> {
            if(item instanceof AbstractRelic) {
                event.setCancelled(true);

                BlockStorage.clearBlockInfo(blockBroken);

                blockBroken.setType(Material.AIR);
            }
        });

    }

}