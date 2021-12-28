package marczeugs.goldamalgamates.mixinimpl.common

import net.minecraft.item.ItemStack

object EnchantmentHelperMixinImpl {
    @JvmStatic
    fun getEnchantabilityForStack(stack: ItemStack) =
        stack.item.enchantability + (ToolItemMixinImpl.getAmalgamateSlots(stack)?.sumOf { it.enchantabilityIncrease } ?: 0)
}
