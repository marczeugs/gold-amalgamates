package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.mixinextensions.ItemStackExtension;
import marczeugs.goldamalgamates.mixinimpl.common.ItemStackMixinImpl;
import marczeugs.goldamalgamates.mixinimpl.common.ToolItemMixinImpl;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackExtension {
    @Override
    public int getCurrentShield() {
        return ToolItemMixinImpl.getCurrentShield((ItemStack) (Object) this);
    }

    @Override
    public void setCurrentShield(int currentShield) {
        ToolItemMixinImpl.setCurrentShield((ItemStack) (Object) this, currentShield);
    }

    @Override
    public int getShieldRechargeTimer() {
        return ToolItemMixinImpl.getShieldRechargeTimer((ItemStack) (Object) this);
    }

    @Override
    public void setShieldRechargeTimer(int shieldRegenTimer) {
        ToolItemMixinImpl.setShieldRechargeTimer((ItemStack) (Object) this, shieldRegenTimer);
    }

    @Override
    public int getStackMaxDamage() {
        return ItemStackMixinImpl.getStackMaxDamage((ItemStack) (Object) this);
    }

    @Inject(
        method = "isSuitableFor(Lnet/minecraft/block/BlockState;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Boolean isSuitableFor = ItemStackMixinImpl.isSuitableFor((ItemStack) (Object) this, state);

        if (isSuitableFor != null) {
            cir.setReturnValue(ItemStackMixinImpl.isSuitableFor((ItemStack) (Object) this, state));
        }
    }

    @Redirect(
        method = "isDamageable()Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getMaxDamage()I"
        )
    )
    public int getMaxDamageForIsDamageable(Item item) {
        return getStackMaxDamage();
    }

    @Redirect(
        method = "getMaxDamage()I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getMaxDamage()I"
        )
    )
    public int getMaxDamageForGetMaxDamage(Item item) {
        return getStackMaxDamage();
    }

    @Redirect(
        method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"
        )
    )
    public void absorbDamageWithShield(ItemStack stack, int damage, int amount) {
        ItemStackMixinImpl.absorbDamageWithShield(stack, amount);
    }
}
