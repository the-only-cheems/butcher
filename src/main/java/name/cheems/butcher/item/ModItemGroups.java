package name.cheems.butcher.item;

import name.cheems.butcher.Butcher;
import name.cheems.butcher.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup BUTCHER_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Butcher.MOD_ID, "butcher_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MYSTERY_MEAT))
                    .displayName(Text.translatable("itemgroup.butcher.butcher_items"))
                    .entries((displayContext, entries) -> {

                        entries.add(ModItems.MYSTERY_MEAT);
                        entries.add(ModItems.MYSTERY_MEAT_COOKED);
                        entries.add(ModBlocks.FLESH_BLOCK);

                        entries.add(ModItems.RAZOR);
                        entries.add(ModItems.CLEAVER);

                    }).build());


    public static void registerItemGroups() {
        Butcher.LOGGER.info("Registering Item group for " + Butcher.MOD_ID);
    }
}
