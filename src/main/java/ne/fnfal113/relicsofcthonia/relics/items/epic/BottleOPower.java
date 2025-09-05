package ne.fnfal113.relicsofcthonia.relics.items.epic;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.relicsofcthonia.relics.abstracts.AbstractRelic;
import ne.fnfal113.relicsofcthonia.relics.implementation.Rarity;
import ne.fnfal113.relicsofcthonia.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;

public class BottleOPower extends AbstractRelic {

    @ParametersAreNonnullByDefault
    public BottleOPower(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                      double dropChance, int piglinRewardAmount, int defaultDropSize) {
        super(itemGroup, item, recipeType, recipe, dropChance, piglinRewardAmount, defaultDropSize);
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public void onItemRightClick(PlayerInteractEvent event, Player player, ItemStack itemInOffhand) {
        consumeRelic(itemInOffhand);

        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 400, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 0));

        Utils.sendRelicMessage("&e力量之瓶给予你药水效果! 是时候和别人打一架了!", player);
    }

}
