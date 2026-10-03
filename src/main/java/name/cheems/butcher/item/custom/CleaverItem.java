package name.cheems.butcher.item.custom;

import name.cheems.butcher.ModEntityTags;
import name.cheems.butcher.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CleaverItem extends Item{

    public CleaverItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);

        if (target.getType().isIn(ModEntityTags.CAN_BE_BUTCHERED)) {
            target.dropItem(ModItems.MYSTERY_MEAT, 1);
            target.dropItem(ModItems.MYSTERY_MEAT, 1);
            target.dropItem(ModItems.MYSTERY_MEAT, 1);
        }
    }

}
