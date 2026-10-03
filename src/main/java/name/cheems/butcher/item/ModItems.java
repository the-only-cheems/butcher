package name.cheems.butcher.item;

import name.cheems.butcher.Butcher;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item MYSTERY_MEAT = registerItem("mystery_meat", new Item(new Item.Settings()));
    public static final Item MYSTERY_MEAT_COOKED = registerItem("mystery_meat_cooked", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Butcher.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Butcher.LOGGER.info("Registering items for" + Butcher.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(MYSTERY_MEAT);
            fabricItemGroupEntries.add(MYSTERY_MEAT_COOKED);
        });
    }
}
