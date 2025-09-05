package ne.fnfal113.relicsofcthonia.relics.implementation;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import ne.fnfal113.relicsofcthonia.droptype.DropType;
import ne.fnfal113.relicsofcthonia.items.RelicItems;
import ne.fnfal113.relicsofcthonia.relics.items.common.CthonianPearl;
import ne.fnfal113.relicsofcthonia.relics.items.common.FishingSack;
import ne.fnfal113.relicsofcthonia.relics.items.common.HealingPotion;
import ne.fnfal113.relicsofcthonia.relics.items.common.LeatherHelmet;
import ne.fnfal113.relicsofcthonia.relics.items.common.Mushroom;
import ne.fnfal113.relicsofcthonia.relics.items.common.PettyMarbleBlock;
import ne.fnfal113.relicsofcthonia.relics.items.common.RingOfSuffering;
import ne.fnfal113.relicsofcthonia.relics.items.epic.BottleOPower;
import ne.fnfal113.relicsofcthonia.relics.items.epic.FervorHelmet;
import ne.fnfal113.relicsofcthonia.relics.items.epic.FlawlessAquaGem;
import ne.fnfal113.relicsofcthonia.relics.items.epic.LuminousPearl;
import ne.fnfal113.relicsofcthonia.relics.items.epic.ThunderInABottle;
import ne.fnfal113.relicsofcthonia.relics.items.epic.ValiantTalisman;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.EyeOfSauron;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.GlazingPearl;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.MagmaGauntlet;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.MysteriousHolocron;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.SapphireRing;
import ne.fnfal113.relicsofcthonia.relics.items.legendary.SkullOfPrometheus;
import ne.fnfal113.relicsofcthonia.relics.items.rare.BlazeAshes;
import ne.fnfal113.relicsofcthonia.relics.items.rare.CeruleanGem;
import ne.fnfal113.relicsofcthonia.relics.items.rare.CrossedSwords;
import ne.fnfal113.relicsofcthonia.relics.items.rare.GoldenJar;
import ne.fnfal113.relicsofcthonia.relics.items.rare.HornOfTaurus;
import ne.fnfal113.relicsofcthonia.relics.items.rare.MarriageRing;
import ne.fnfal113.relicsofcthonia.relics.items.rare.TerracottaPot;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.AgedWine;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.BlueGlowstone;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.CthonianToken;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.GrayBerry;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.PinkSalt;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.SkullHat;
import ne.fnfal113.relicsofcthonia.relics.items.uncommon.TanzaniteBlock;
import org.bukkit.inventory.ItemStack;

public class RegisterRelics {

    public static void registerRelics(SlimefunAddon instance){

        // Common relics
        new CthonianPearl(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_CTHONIAN_PEARL, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.57, 3, 3)
                .register(instance);

        new FishingSack(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_FISHING_SACK, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.28, 3, 3)
                .register(instance);

        new HealingPotion(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_HEALING_POTION, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.32, 3, 3)
                .register(instance);

        new LeatherHelmet(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_LEATHER_HELMET, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.24, 3, 3)
                .register(instance);

        new Mushroom(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_MUSHROOM, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.18, 3, 3)
                .register(instance);

        new PettyMarbleBlock(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_PETTY_MARBLE_BLOCK, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.53, 3, 3)
                .register(instance);

        new RingOfSuffering(RelicItems.COMMON_RELIC, RelicItems.COMMON_RELIC_RING_OF_SUFFERING, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 12.54, 3, 3)
                .register(instance);


        // Uncommon Relics
        new AgedWine(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_AGED_WINE, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.32, 2, 3)
                .register(instance);

        new BlueGlowstone(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_BLUE_GLOWSTONE, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null},9.02, 2, 3)
                .register(instance);

        new CthonianToken(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_CTHONIAN_TOKEN, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.01, 2, 3)
                .register(instance);

        new GrayBerry(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_GRAY_BERRY, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.38, 2, 3)
                .register(instance);

        new PinkSalt(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_PINK_SALT, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.15, 2, 3)
                .register(instance);

        new SkullHat(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_SKULL_HAT, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.62, 2, 3)
                .register(instance);

        new TanzaniteBlock(RelicItems.UNCOMMON_RELIC, RelicItems.UNCOMMON_RELIC_TANZANITE_BLOCK, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 9.32, 2, 3)
                .register(instance);

        // Rare Relics
        new BlazeAshes(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_BLAZE_ASHES, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.14, 1, 2)
                .register(instance);

        new CeruleanGem(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_CERULEAN_GEM, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.05, 1, 2)
                .register(instance);

        new CrossedSwords(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_CROSSED_SWORDS, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.54, 1, 2)
                .register(instance);

        new GoldenJar(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_GOLDEN_JAR, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.32, 1, 2)
                .register(instance);

        new HornOfTaurus(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_HORN_OF_TAURUS, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.54, 1, 2)
                .register(instance);

        new MarriageRing(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_MARRIAGE_RING, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.12, 1, 3)
                .register(instance);

        new TerracottaPot(RelicItems.RARE_RELIC, RelicItems.RARE_RELIC_TERRACOTTA_POT, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 7.91, 1, 3)
                .register(instance);


        // Epic Relics
        new BottleOPower(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_BOTTLE_O_POWER, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.42, 1, 2)
                .register(instance);

        new FlawlessAquaGem(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_FLAWLESS_AQUA_GEM, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.94, 1, 2)
                .register(instance);

        new LuminousPearl(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_LUMINOUS_PEARL, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.67, 1, 2)
                .register(instance);

        new ValiantTalisman(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_VALIANT_TALISMAN, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.32, 1, 2)
                .register(instance);

        new FervorHelmet(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_FERVOR_HELMET, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.13, 1, 2)
                .register(instance);

        new ThunderInABottle(RelicItems.EPIC_RELIC, RelicItems.EPIC_RELIC_THUNDER_IN_A_BOTTLE, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 4.34, 1, 2)
                .register(instance);


        // Legendary Relics
        new EyeOfSauron(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_EYE_OF_SAURON, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.12, 2, 2)
                .register(instance);

        new MysteriousHolocron(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_MYSTERIOUS_HOLOCRON, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.24, 2, 2)
                .register(instance);

        new SkullOfPrometheus(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_SKULL_OF_PROMETHEUS, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.06, 2, 2)
                .register(instance);

        new MagmaGauntlet(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_MAGMA_GAUNTLET, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.42, 2, 2)
                .register(instance);

        new GlazingPearl(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_GLAZING_PEARL, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.35, 2, 2)
                .register(instance);

        new SapphireRing(RelicItems.LEGENDARY_RELIC, RelicItems.LEGENDARY_RELIC_SAPPHIRE_RING, DropType.DROP_TYPE, new ItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null}, 1.02, 2, 2)
                .register(instance);

    }

}