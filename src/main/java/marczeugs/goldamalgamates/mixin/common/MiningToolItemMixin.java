package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.mixinextensions.MiningToolItemExtension;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin implements MiningToolItemExtension {
    private float attackSpeed;

    @Override
    public float getAttackSpeed() {
        return attackSpeed;
    }

    @Inject(
        method = "<init>(FFLnet/minecraft/item/ToolMaterial;Lnet/minecraft/tag/TagKey;Lnet/minecraft/item/Item$Settings;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMultimap;builder()Lcom/google/common/collect/ImmutableMultimap$Builder;",
            ordinal = 0,
            remap = false
        )
    )
    public void setAttackSpeed(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Item.Settings settings, CallbackInfo c) {
        this.attackSpeed = attackSpeed;
    }
}
