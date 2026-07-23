package com.armet.dodgeable_skeletons.mixin;

import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Inject(method = "getDefaultGravity", at = @At("HEAD"), cancellable = true)
    private void betaGravity(CallbackInfoReturnable<Double> cir) {
        AbstractArrow arrow = (AbstractArrow)(Object)this;
        cir.setReturnValue(0.03);

    }
}
