package name.cheems.butcher.item;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.item.custom.BleedItem;
import name.cheems.butcher.item.custom.CorruptedFoodItem;
import name.cheems.butcher.item.custom.RazorItem;
import name.cheems.butcher.item.custom.SyringeItem;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item MYSTERY_MEAT = registerItem("mystery_meat", new CorruptedFoodItem(new Item.Settings().food(ModFoodComponents.MYSTERY_MEAT), 5));
    public static final Item MYSTERY_MEAT_COOKED = registerItem("mystery_meat_cooked", new CorruptedFoodItem(new Item.Settings().food(ModFoodComponents.MYSTERY_MEAT_COOKED), 1));
    public static final Item RAZOR = registerItem("razor", new RazorItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f))));
    public static final Item CLEAVER = registerItem("cleaver", new BleedItem(new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.IRON, 6, -3.1f))));
    public static final Item BONE_SAW = registerItem("bone_saw", new BleedItem(new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 5, -3.0f))));
    public static final Item STEAK_KNIFE = registerItem("steak_knife", new BleedItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))));
    public static final Item SKINNING_KNIFE = registerItem("skinning_knife", new BleedItem(new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.IRON, 2, -2.4F))));
    public static final Item MEAT_INJECTOR = registerItem("meat_injector", new SyringeItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Butcher.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Butcher.LOGGER.info("Registering items for " + Butcher.MOD_ID);
    }
}
