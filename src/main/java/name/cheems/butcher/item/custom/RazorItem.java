package name.cheems.butcher.item.custom;

import name.cheems.butcher.ModEntityTags;
import name.cheems.butcher.effect.ModEffects;
import name.cheems.butcher.item.ModItems;
import name.cheems.butcher.particle.ModParticles;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import net.minecraft.util.math.Vec3d;

public class RazorItem extends Item {
    public RazorItem(Settings settings) {
        super(settings);
    }


    private static final int MAX_USE_TIME = 40; // 2 seconds

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        if (world instanceof ServerWorld serverWorld) {

            Vec3d look = user.getRotationVec(1.0f);

            serverWorld.spawnParticles(
                    ModParticles.BLOOD_PARTICLE,
                    user.getX(),
                    user.getY() + 1,
                    user.getZ(),
                    10,
                    look.getX() / 2, look.getY(), look.getZ() / 2,
                    0.0
            );
        }

        return TypedActionResult.consume(user.getStackInHand(hand));
    }


    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // The player successfully completed the 40-tick use.
        if (!world.isClient && user instanceof PlayerEntity player) {
            player.damage(world.getDamageSources().generic(), 2.0F);

            stack.damage(1, player, EquipmentSlot.MAINHAND);

            ItemStack mysteryMeat = new ItemStack(ModItems.MYSTERY_MEAT);
            player.dropItem(mysteryMeat, true);
        }

        if (world.random.nextFloat() < 0.25f) {
            user.addStatusEffect(
                    new StatusEffectInstance(
                            ModEffects.BLEED,
                            120,
                            0,
                            false,
                            false,
                            true
                    )
            );
        }



        return stack;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);



        target.addStatusEffect(new StatusEffectInstance(ModEffects.BLEED,
                40,
                0,
                false,
                false,
                true));

        if (target.getType().isIn(ModEntityTags.CAN_BE_BUTCHERED)) {
            target.dropItem(ModItems.MYSTERY_MEAT, 1);
        }
    }

}
