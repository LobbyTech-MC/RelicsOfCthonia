package ne.fnfal113.relicsofcthonia.miscellaneous.abstracts;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.relics.implementation.Rarity;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AbstractRelicVoider extends UnplaceableBlock {

    private static final CustomItemStack DECREMENT_CONDITION = new CustomItemStack(Material.RED_STAINED_GLASS_PANE,
            "&c减少阈值",
            "&7点击减少阈值"
    );

    private static final CustomItemStack INCREMENT_CONDITION = new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,
            "&c增加阈值",
            "&7点击增加阈值"
    );

    @Getter
    private final boolean notifEnabled = RelicsOfCthonia.getInstance().getConfig().getBoolean("enable-relic-voider-notif", true);

    @Getter
    private final Rarity rarity;
    @Getter
    final NamespacedKey conditionKey;

    public AbstractRelicVoider(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Rarity rarity) {
        super(itemGroup, item, recipeType, recipe);

        this.rarity = rarity;
        this.conditionKey = new NamespacedKey(RelicsOfCthonia.getInstance(), "condition_quota");
    }

    @SuppressWarnings("ConstantConditions")
    public void onInventoryClick(InventoryClickEvent event, ItemStack itemStack) {
        ItemStack currentClickedItem = event.getCurrentItem();

        if (currentClickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
            setConditionQuota(itemStack, event.getInventory(), -1);
        } else if (currentClickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            setConditionQuota(itemStack, event.getInventory(), 1);
        }
    }

    public void setConditionQuota(ItemStack itemStack, Inventory inventory, int integer){
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int conditionQuota = pdc.getOrDefault(getConditionKey(), PersistentDataType.INTEGER, 0);
        int finalCondition = conditionQuota + integer;

        if(finalCondition < 0 || finalCondition > 100){
            return;
        }

        pdc.set(getConditionKey(), PersistentDataType.INTEGER, finalCondition);
        itemStack.setItemMeta(meta);

        inventory.setItem(4, new CustomItemStack(Material.PURPLE_STAINED_GLASS,
                "&f清空任何完整度低于 &6&l" + finalCondition + "%",
                "&f的" + getRarity().getRarityName() + "遗物"
        ));
    }

    private NamespacedKey getConditionKey() {
		// TODO Auto-generated method stub
		return conditionKey;
	}

	public void onClick(ItemStack itemStack, Player player){
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int conditionQuota = pdc.getOrDefault(getConditionKey(), PersistentDataType.INTEGER, 0);

        Inventory inventory = Bukkit.createInventory(null, 9, Utils.colorTranslator(meta.getDisplayName()));

        for(int i = 0; i < inventory.getSize(); i++){
            if(i == 0){
                inventory.setItem(i, DECREMENT_CONDITION);
            } else if(i == 8){
                inventory.setItem(i, INCREMENT_CONDITION);
            } else if(i == 4) {
                inventory.setItem(i, new CustomItemStack(Material.PURPLE_STAINED_GLASS,
                    "&f清空任何完整度低于 &6&l" + conditionQuota + "%",
                    "&f的" + getRarity().getRarityName() + "遗物"
                ));
            } else {
                inventory.setItem(i, ChestMenuUtils.getBackground());
            }
        }

        player.openInventory(inventory);
    }

    public void onRelicPickup(EntityPickupItemEvent event, ItemStack voider, Item pickedUpRelic, int pickedUpRelicCondition){
        ItemMeta meta = voider.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        int conditionQuota = pdc.getOrDefault(getConditionKey(), PersistentDataType.INTEGER, 0);

        if(pickedUpRelicCondition <= conditionQuota){
            if(isNotifEnabled()) {
                Utils.sendRelicMessage("&6遗物清除器已清除 " + "&r" + pickedUpRelic.getItemStack().getItemMeta().getDisplayName(), event.getEntity());
            }

            pickedUpRelic.remove();

            event.setCancelled(true);
        }
    }

	private boolean isNotifEnabled() {
		// TODO Auto-generated method stub
		return notifEnabled;
	}

	public Rarity getRarity() {
		// TODO Auto-generated method stub
		return rarity;
	}

}
