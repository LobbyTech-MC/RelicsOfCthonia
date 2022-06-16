package ne.fnfal113.relicsofcthonia.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.config.ConfigManager;
import net.guizhanss.guizhanlib.minecraft.helper.MaterialHelper;
import net.guizhanss.guizhanlib.minecraft.helper.entity.EntityTypeHelper;
import net.guizhanss.guizhanlib.utils.StringUtil;
import net.guizhanss.relicsofcthonia.types.LoreType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {

    private static final Function<String, String> materialLoreHandler = (value) -> {
        SlimefunItem sfItem = SlimefunItem.getById(value);
        if (sfItem != null) {
            return sfItem.getItemName();
        } else {
            return MaterialHelper.getName(value);
        }
    };
    private static final Function<String, String> entityTypeLoreHandler = (value) ->
        EntityTypeHelper.getName(StringUtil.dehumanize(value));
    private static final Function<String, String> defaultLoreHandler = (value) ->
        value.replace("_", " ").toLowerCase();

    public static String colorTranslator(String strings){
        return ChatColor.translateAlternateColorCodes('&', strings);
    }

    public static void sendRelicMessage(String message, LivingEntity livingEntity){
        livingEntity.sendMessage(colorTranslator("&6[克苏尼亚遗物] > " + message));
    }

    // set or update the given string to replace with the given config section
    // and settings that returns an integer value as the new string.
    public static void setLoreByIntValue(@Nonnull ItemStack itemStack, String section, String settings, String stringToReplace, String color, String suffix){
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        for(int i = 0 ; i < lore.size(); i++){
            if(lore.get(i).contains(Utils.colorTranslator(color + stringToReplace))){
                int value = RelicsOfCthonia.getInstance().getConfigManager().getValueById(section, settings);
                if(value != 0) {
                    String line = lore.get(i).replace(Utils.colorTranslator(color + stringToReplace),
                            Utils.colorTranslator(color + value + suffix));

                    lore.set(i, line);
                }
            }
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public static void setLoreByFixedIntValue(@Nonnull ItemStack itemStack, int value, String stringToReplace, String color, String suffix){
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        for(int i = 0 ; i < lore.size(); i++){
            if(lore.get(i).contains(Utils.colorTranslator(color + stringToReplace))){
                if(value != 0) {
                    String line = lore.get(i).replace(Utils.colorTranslator(color + stringToReplace),
                            Utils.colorTranslator(color + value + suffix));

                    lore.set(i, line);
                }
            }
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    // set or update the given string to replace with the given config section
    // and settings that returns a double value as the new string.
    public static void setLoreByDoubleValue(@Nonnull ItemStack itemStack, String section, String settings, String stringToReplace, String color, String suffix){
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        for(int i = 0 ; i < lore.size(); i++){
            if(lore.get(i).contains(Utils.colorTranslator(color + stringToReplace))){
                double value = RelicsOfCthonia.getInstance().getConfigManager().getDoubleValueById(section, settings);
                if(value != 0.0){
                    String line = lore.get(i).replace(Utils.colorTranslator(color + stringToReplace),
                            Utils.colorTranslator(color + value + suffix));

                    lore.set(i, line);
                }
            }
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    // 对 lore 中显示的物品进行汉化
    public static void addLoreByStringList(@Nonnull ItemStack itemStack, String section, String settings, String stringToReplace, String color, String prefix, String suffix, LoreType loreType){
        Function<String, String> loreHandler;
        switch (loreType) {
            case MATERIAL:
                loreHandler = materialLoreHandler;
                break;
            case ENTITY_TYPE:
                loreHandler = entityTypeLoreHandler;
                break;
            default:
                loreHandler = defaultLoreHandler;
        }
        addLoreByStringList(itemStack, section, settings, stringToReplace, color, prefix, suffix, loreHandler);
    }

    // loops the provided list of string and appends them on the designated index
    // based on the string that will be replaced, but instead it is used as the index
    private static void addLoreByStringList(@Nonnull ItemStack itemStack, String section, String settings, String stringToReplace, String color, String prefix, String suffix, Function<String, String> handler){
        ConfigManager configManager = RelicsOfCthonia.getInstance().getConfigManager();
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        int j = 0;

        for(int i = 0 ; i < lore.size(); i++){
            if(lore.get(i).contains(Utils.colorTranslator(color + stringToReplace))){
                j = i;
                break;
            }
        }

        for (int x = 0; x <= RelicsOfCthonia.getInstance().getConfigManager().getStringListById(section, settings).size() - 1; x++) {
            String value = configManager.getStringListById(section, settings).get(x);

            if(!value.isEmpty()) {
                lore.add(j + 1, Utils.colorTranslator(color + prefix + handler.apply(value) + suffix));
            }
        }

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    public static void createDelayedTask(Consumer<BukkitTask> taskConsumer, long delay){
        Bukkit.getScheduler().runTaskLater(RelicsOfCthonia.getInstance(), taskConsumer, delay);
    }

    public static void createAsyncTask(Consumer<BukkitTask> taskConsumer){
        Bukkit.getScheduler().runTaskAsynchronously(RelicsOfCthonia.getInstance(), taskConsumer);
    }

    public static void createSyncTask(Consumer<BukkitTask> taskConsumer){
        Bukkit.getScheduler().runTask(RelicsOfCthonia.getInstance(), taskConsumer);
    }

    public static NamespacedKey createKey(String id){
        return new NamespacedKey(RelicsOfCthonia.getInstance(), id.toLowerCase());
    }


}
