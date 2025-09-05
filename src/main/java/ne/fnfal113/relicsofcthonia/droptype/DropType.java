package ne.fnfal113.relicsofcthonia.droptype;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.relicsofcthonia.RelicsOfCthonia;
import ne.fnfal113.relicsofcthonia.items.RelicItems;
import org.bukkit.NamespacedKey;

public class DropType {

    public static final RecipeType DROP_TYPE = new RecipeType(
            new NamespacedKey(RelicsOfCthonia.getInstance(), "relic_drop_type_generic"),
            RelicItems.RELIC_DROP_TYPE
    );
    
}
