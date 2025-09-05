package ne.fnfal113.relicsofcthonia.relics.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.RelicsRegistry;
import ne.fnfal113.relicsofcthonia.config.ConfigManager;
import ne.fnfal113.relicsofcthonia.relics.implementation.NetherMaterials;
import ne.fnfal113.relicsofcthonia.relics.implementation.OffHandRightClickHandler;
import ne.fnfal113.relicsofcthonia.relics.implementation.Rarity;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import net.guizhanss.relicsofcthonia.types.LoreType;

public abstract class AbstractRelic extends SlimefunItem implements OffHandRightClickHandler {

    @Getter
    private final double dropChance;
    @Getter
    private final int defaultDropSize;
    @Getter
    private final int piglinRewardAmount;
    @Getter
    private final List<Material> netherMaterials = new ArrayList<>();
    @Getter
    private final ConfigManager configManager = RelicsOfCthonia.getInstance().getConfigManager();
    @Getter
    private final RelicsRegistry relicsRegistry = RelicsOfCthonia.getInstance().getRelicsRegistry();

    @ParametersAreNonnullByDefault
    public AbstractRelic(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                         double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, recipeType, recipe);

        initializeSettings(dropChance, piglinRewardAmount, defaultDropSize);
        this.dropChance = getConfigManager().getCustomConfig("relic-settings").getDouble(this.getId() + "." + "drop-chance");
        this.piglinRewardAmount = getConfigManager().getCustomConfig("relic-settings").getInt(this.getId() + "." + "piglin-reward-amount");
        this.defaultDropSize = defaultDropSize;
        updateRelicLore();
    }

    public void updateRelicLore(){
       // update this itemstack object lore for the drop chance
       Utils.setLoreByConfigValue(this.getItem(), this.getId(), "drop-chance", "%", "&e", "%", "relic-settings");
       // update this itemstack object lore on which materials do relic drop
       Utils.setLoreByConfigStringList(this.getItem(), this.getId(), "drops-on-material", "掉落于:", "&e", "‣ ", "",
           "relic-settings", LoreType.MATERIAL);
       // update this itemstack object lore on which mobs do relic drop
       Utils.setLoreByConfigStringList(this.getItem(), this.getId(), "drops-on-mob", "掉落于:", "&e", "‣ ", "", "relic" +
           "-settings", LoreType.ENTITY_TYPE);
       // update this itemstack object lore for the barter rewards
       Utils.setLoreByConfigStringList(this.getItem(), this.getId(), "piglin-barter-rewards", "猪灵交换物列表:", "&a",
           "‣ " + getPiglinRewardAmount() + " ", "", "relic-settings", LoreType.MATERIAL);
    }

    public int getPiglinRewardAmount() {
		// TODO Auto-generated method stub
		return piglinRewardAmount;
	}

	public void initializeSettings(double dropChance, int piglinRewardAmount, int defaultDropSize){
        initNetherMaterials();

        // configuration handling
        initSingleSectionSettings(dropChance, piglinRewardAmount);
        initDefaultWhereToDrop(defaultDropSize);
        initDefaultPiglinRewards(defaultDropSize);

        setRelicsRegistry();
    }

    public void setRelicsRegistry(){
        // load this relic config values to relicRegistry including newly added ones by an any admin
        try {
            FileConfiguration config = getConfigManager().getCustomConfig("relic-settings");

            // retrieve this relic's config material list then add to relic registry
            if(config.isConfigurationSection(this.getId()) && config.getConfigurationSection(this.getId()).contains("drops-on-material")) {
                List<Material> finalMaterialList = new ArrayList<>();

                for (String materials : config.getConfigurationSection(this.getId()).getStringList("drops-on-material")) {
                    finalMaterialList.add(Material.matchMaterial(materials));
                }

                getRelicsRegistry().getWhereToDropMaterialMap().put(this, finalMaterialList);
            }

            // retrieve this relic's config mob list then add to relic registry
            if(config.isConfigurationSection(this.getId()) && config.getConfigurationSection(this.getId()).contains("drops-on-mob")) {
                List<String> finalMobList = config.getConfigurationSection(this.getId()).getStringList("drops-on-mob");

                getRelicsRegistry().getWhereToDropMobMap().put(this, finalMobList);
            }

            // retrieve this relic's config rewards list then add to relic registry
            if(config.isConfigurationSection(this.getId()) && config.getConfigurationSection(this.getId()).contains("piglin-barter-rewards")) {
                List<String> finalRewardList = config.getConfigurationSection(this.getId()).getStringList("piglin-barter-rewards");

                getRelicsRegistry().getPiglinRewardList().put(this, finalRewardList);
            }
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred on adding data to relics registry! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    private RelicsRegistry getRelicsRegistry() {
		// TODO Auto-generated method stub
		return relicsRegistry;
	}

	private ConfigManager getConfigManager() {
		// TODO Auto-generated method stub
		return configManager;
	}

	public void initNetherMaterials(){
        getNetherMaterials().addAll(NetherMaterials.BASE_STONES_NETHER.getMaterial());
        getNetherMaterials().addAll(NetherMaterials.WART_BLOCKS.getMaterial());
        getNetherMaterials().addAll(NetherMaterials.GROW_MUSHROOM_BLOCKS.getMaterial());
        getNetherMaterials().addAll(NetherMaterials.SOUL_BLOCKS.getMaterial());
        getNetherMaterials().addAll(NetherMaterials.INFINIBURN_BLOCKS.getMaterial());
        getNetherMaterials().addAll(NetherMaterials.OTHER_BLOCKS.getMaterial());
    }

    private List<Material> getNetherMaterials() {
		// TODO Auto-generated method stub
		return netherMaterials;
	}

	public void initSingleSectionSettings(double dropChance, int piglinRewardAmount) {
        try {
            getConfigManager().initializeConfig(this.getId(), "drop-chance", dropChance, "relic-settings");
            getConfigManager().initializeConfig(this.getId(), "piglin-reward-amount", piglinRewardAmount, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default single section settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initDefaultWhereToDrop(int defaultDropSize) {
        try {
            JsonObject jsonObject = configManager.loadJson("nether_mobs");
            List<String> randomMobList = new ArrayList<>();
            List<String> randomMaterialList = new ArrayList<>();

            // retrieve json resource for nether mobs and
            // create a randomized mob list, size determined dy this object default drop size
            for (int i = 0; i < defaultDropSize; i++) {
                String mob = jsonObject
                                .getAsJsonPrimitive("nether_mob_" + ThreadLocalRandom.current().nextInt(1, 12))
                                    .getAsString();

                if(!randomMobList.contains(mob)) {
                    randomMobList.add(mob);
                }
            }

            // create a randomized material list, size determined dy this object default drop size
            for (int i = 0; i < defaultDropSize; i++) {
                String material = getNetherMaterials()
                                    .get(ThreadLocalRandom.current().nextInt(0, getNetherMaterials().size()))
                                        .toString();

                if(!randomMaterialList.contains(material)) {
                    randomMaterialList.add(material);
                }
            }

            getConfigManager().initializeConfig(this.getId(), "drops-on-mob", randomMobList, "relic-settings");
            getConfigManager().initializeConfig(this.getId(), "drops-on-material", randomMaterialList, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default drop settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initDefaultPiglinRewards(int defaultDropSize) {
        try {
            JsonObject jsonObject = configManager.loadJson("piglin_barter_list");
            List<String> randomRewardList = new ArrayList<>();

            // retrieve json resource for barter rewards and
            // create a randomized reward list, size determined dy this object default drop size
            for (int i = 1; i <= defaultDropSize; i++) {
                String reward = jsonObject
                                    .getAsJsonObject(getRarity().name())
                                        .get("drop-" + ThreadLocalRandom.current().nextInt(1,8))
                                            .getAsString();

                if(!randomRewardList.contains(reward)) {
                    randomRewardList.add(reward);
                }
            }

            getConfigManager().initializeConfig(this.getId(), "piglin-barter-rewards", randomRewardList, "relic-settings");
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default piglin rewards! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public ItemStack setRelicConditionAndGet(boolean isNaturallyDropped, int condition){
        ItemStack itemStack = this.getItem().clone();
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        int randomCondition = isNaturallyDropped ? ThreadLocalRandom.current().nextInt(1,100) : condition;

        pdc.set(Utils.createKey("relic_condition"), PersistentDataType.INTEGER, randomCondition);
        itemStack.setItemMeta(meta);

        Utils.setLoreByFixedIntValue(itemStack, randomCondition, "%", "&d", "%");

        return itemStack.clone();
    }

    public int getRelicCondition(@Nonnull ItemStack itemStack){
        return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(Utils.createKey("relic_condition"), PersistentDataType.INTEGER, 0);
    }

    public <T extends OffHandRightClickHandler> void callRelicHandler(Class<T> clazz, Consumer<T> callback, AbstractRelic abstractRelic){
        if(clazz.isInstance(abstractRelic)){
            callback.accept(clazz.cast(abstractRelic));
        }
    }

    public void consumeRelic(ItemStack itemStack){
        if(itemStack == null){
            return;
        }

        itemStack.setAmount(itemStack.getAmount() - 1);
    }

    /**
     *
     * @return the enum rarity of the relic
     */
    public abstract Rarity getRarity();

    @Override
    public boolean isDisenchantable(){
        return false;
    }

    @Override
    public boolean isEnchantable(){
        return false;
    }

	public double getDropChance() {
		// TODO Auto-generated method stub
		return dropChance;
	}

}