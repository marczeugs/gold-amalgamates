package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.mixinimpl.common.EnchantmentHelperMixinImpl;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Redirect(
        method = "calculateRequiredExperienceLevel(Lnet/minecraft/util/math/random/Random;IILnet/minecraft/item/ItemStack;)I",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getEnchantability()I"
        )
    )
    private static int getEnchantabilityToCalculateRequiredExperienceLevel(Item item, Random random, int slotIndex, int bookshelfCount, ItemStack stack) {
        return EnchantmentHelperMixinImpl.getEnchantabilityForStack(stack);
    }

    @Redirect(
        method = "generateEnchantments(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;IZ)Ljava/util/List;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/Item;getEnchantability()I"
        )
    )
    private static int getEnchantabilityToGenerateEnchantments(Item item, Random random, ItemStack stack, int level, boolean treasureAllowed) {
        return EnchantmentHelperMixinImpl.getEnchantabilityForStack(stack);
    }
}
