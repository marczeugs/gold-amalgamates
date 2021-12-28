package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.mixinextensions.SwordItemExtension;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwordItem.class)
public class SwordItemMixin implements SwordItemExtension {
    private float attackSpeed;

    @Override
    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/item/ToolMaterial;IFLnet/minecraft/item/Item$Settings;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMultimap;builder()Lcom/google/common/collect/ImmutableMultimap$Builder;",
            ordinal = 0,
            remap = false
        )
    )
    public void setAttackSpeed(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item.Settings settings, CallbackInfo ci) {
        this.attackSpeed = attackSpeed;
    }
}
