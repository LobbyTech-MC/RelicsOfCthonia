package ne.fnfal113.relicsofcthonia.relics.abstracts;

import java.io.IOException;
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
    @Getter
    private final List<String> piglinRewardList = new ArrayList<>();

    @ParametersAreNonnullByDefault
    public AbstractRelic(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                         double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, recipeType, recipe);

        initializeSettings(dropChance, piglinRewardAmount, defaultDropSize);
        this.dropChance = configManager.getDoubleValueById(this.getId(), "drop-chance");
        this.piglinRewardAmount = configManager.getValueById(this.getId(), "piglin-reward-amount");
        this.defaultDropSize = defaultDropSize;
        updateRelicLore();
    }

    public void updateRelicLore(){
       Utils.setLoreByDoubleValue(this.getItem(), this.getId(), "drop-chance", "%", "&e", "%");
       Utils.addLoreByStringList(this.getItem(), this.getId(), "drops-on-material", "掉落于:", "&e", "‣ ", "", LoreType.MATERIAL);
       Utils.addLoreByStringList(this.getItem(), this.getId(), "drops-on-mob", "掉落于:", "&e", "‣ ", "", LoreType.ENTITY_TYPE);
       Utils.addLoreByStringList(this.getItem(), this.getId(), "piglin-barter-rewards", "猪灵交换物列表:", "&a", "‣ " + getPiglinRewardAmount() + "x ", "", LoreType.MATERIAL);
    }

    public int getPiglinRewardAmount() {
		// TODO Auto-generated method stub
		return piglinRewardAmount;
	}

	public void initializeSettings(double dropChance, int piglinRewardAmount, int defaultDropSize){
        initNetherMaterials();
        initSingleSectionSettings(dropChance, piglinRewardAmount);
        initDefaultWhereToDrop(defaultDropSize);
        initDefaultPiglinRewards(defaultDropSize);

        setRelicsRegistry();
    }

    public void setRelicsRegistry(){
        List<Material> finalMaterialList = new ArrayList<>();

        // we load the relic settings file to retrieve values
        // including newly added ones by an any admin
        try {
            FileConfiguration config = getConfigManager().getCustomConfig();

            if(config.isConfigurationSection(this.getId()) && config.getConfigurationSection(this.getId()).contains("drops-on-material")) {
                for (String materials : config.getConfigurationSection(this.getId()).getStringList("drops-on-material")) {
                    finalMaterialList.add(Material.matchMaterial(materials));
                }
                getRelicsRegistry().getWhereToDropMaterialMap().put(this, finalMaterialList);
            }

            if(config.isConfigurationSection(this.getId())) {
                List<String> finalMobList = new ArrayList<>(config.getConfigurationSection(this.getId()).getStringList("drops-on-mob"));
                getRelicsRegistry().getWhereToDropMobMap().put(this, finalMobList);
            }

            if(config.isConfigurationSection(this.getId()) && config.getConfigurationSection(this.getId()).contains("piglin-barter-rewards")) {
                for (String reward : config.getConfigurationSection(this.getId()).getStringList("piglin-barter-rewards")) {
                    getPiglinRewardList().add(reward);
                }
            }
        } catch (IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon setting relics registry! Please report on github issue tracker!");
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

	public void initDefaultPiglinRewards(int defaultDropSize) {
        List<String> rewardList = new ArrayList<>();
        List<String> randomRewardList = new ArrayList<>();

        try {
            JsonObject jsonObject = (JsonObject) configManager.loadJson("piglin_barter_list");

            for (int i = 1; i <= 7; i++) {
                rewardList.add(jsonObject.getAsJsonObject(getRarity().name()).get("drop-" + i).getAsString());
            }

            for (int i = 0; i < defaultDropSize; i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(rewardList.size());

                if(!randomRewardList.contains(rewardList.get(randomIndex))) {
                    randomRewardList.add(rewardList.get(randomIndex));
                }
            }

            getConfigManager().setConfigStringListValues(this.getId(), "piglin-barter-rewards", randomRewardList, "relic-settings", false);
        } catch (IOException | IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default piglin rewards! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initDefaultWhereToDrop(int defaultDropSize) {
        List<String> mobList = new ArrayList<>();
        List<String> randomMobList = new ArrayList<>();
        List<String> randomMaterialList = new ArrayList<>();

        try {
            JsonObject jsonObject = (JsonObject) configManager.loadJson("nether_mobs");

            for (int i = 1; i <= 11; i++) {
                mobList.add(jsonObject.getAsJsonPrimitive("nether_mob_" + i).getAsString());
            }

            for (int i = 0; i < defaultDropSize; i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(mobList.size());

                if(!randomMobList.contains(mobList.get(randomIndex))) {
                    randomMobList.add(mobList.get(randomIndex));
                }
            }

            for (int i = 0; i < defaultDropSize; i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(getNetherMaterials().size());

                if(!randomMaterialList.contains(getNetherMaterials().get(randomIndex).toString())) {
                    randomMaterialList.add(getNetherMaterials().get(randomIndex).toString());
                }
            }

            getConfigManager().setConfigStringListValues(this.getId(), "drops-on-mob", randomMobList, "relic-settings", false);
            getConfigManager().setConfigStringListValues(this.getId(), "drops-on-material", randomMaterialList, "relic-settings", false);
        } catch (IOException | IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default drop settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public void initSingleSectionSettings(double dropChance, int piglinRewardAmount) {
        try {
            getConfigManager().setConfigDoubleValues(this.getId(), "drop-chance", dropChance, "relic-settings", false);
            getConfigManager().setConfigIntegerValues(this.getId(), "piglin-reward-amount", piglinRewardAmount, "relic-settings", false);
        } catch (IOException | IllegalArgumentException | NullPointerException e){
            RelicsOfCthonia.getInstance().getLogger().info("An error has occurred upon initializing default single section settings! Please report on github issue tracker!");
            e.printStackTrace();
        }
    }

    public ItemStack setRelicCondition(boolean isNaturallyDropped, int condition){
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

	public List<String> getPiglinRewardList() {
		// TODO Auto-generated method stub
		return piglinRewardList;
	}

}