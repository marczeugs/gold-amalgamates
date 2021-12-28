package marczeugs.goldamalgamates.mixin.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
class ItemMixin {
    @Redirect(
        method = {
            "getItemBarStep(Lnet/minecraft/item/ItemStack;)I",
            "getItemBarColor(Lnet/minecraft/item/ItemStack;)I"
        },
        require = 2,
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/Item;maxDamage:I"
        )
    )
    public int getStackMaxDamageForItemBarStep(Item item, ItemStack stack) {
        return stack.getMaxDamage();
    }
}