package name.cheems.butcher.item;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.item.custom.CleaverItem;
import name.cheems.butcher.item.custom.RazorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item MYSTERY_MEAT = registerItem("mystery_meat", new Item(new Item.Settings()));
    public static final Item MYSTERY_MEAT_COOKED = registerItem("mystery_meat_cooked", new Item(new Item.Settings()));
    public static final Item RAZOR = registerItem("razor", new RazorItem(new Item.Settings().maxDamage(32)));
    public static final Item CLEAVER = registerItem("cleaver", new CleaverItem(new Item.Settings().maxDamage(50)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Butcher.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Butcher.LOGGER.info("Registering items for " + Butcher.MOD_ID);
    }
}
