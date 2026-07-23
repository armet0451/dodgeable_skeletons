package com.armet.dodgeable_skeletons.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin {
    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void performRangedAttack(LivingEntity target, float distanceFactor, CallbackInfo ci){
        AbstractSkeleton skeleton = (AbstractSkeleton)(Object)this;

        ItemStack weapon = skeleton.getItemInHand(ProjectileUtil.getWeaponHoldingHand(skeleton, (item) -> item instanceof BowItem));
        ItemStack itemstack1 = skeleton.getProjectile(weapon);
        AbstractArrow abstractarrow = this.getArrow(itemstack1, distanceFactor, weapon);
        Item var7 = weapon.getItem();
        if (var7 instanceof ProjectileWeaponItem weaponItem) {
            abstractarrow = weaponItem.customArrow(abstractarrow, itemstack1, weapon);
        }


        abstractarrow.setPos(abstractarrow.getX(), abstractarrow.getY(), abstractarrow.getZ());

        double d0 = target.getX() - skeleton.getX();
        double d1 = target.getY() + target.getEyeHeight() - 0.2D - abstractarrow.getY();
        double d2 = target.getZ() - skeleton.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 0.6F, 12.0f);
        skeleton.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (skeleton.getRandom().nextFloat() * 0.4F + 0.8F));
        skeleton.level().addFreshEntity(abstractarrow);
        ci.cancel();
    }

    @Shadow
    protected abstract AbstractArrow getArrow(ItemStack ammo, float damageModifier, ItemStack weapon);

}
