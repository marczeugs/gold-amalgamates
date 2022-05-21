package marczeugs.goldamalgamates.mixinimpl.common

import marczeugs.goldamalgamates.mixin.common.MiningToolItemAccessor
import marczeugs.goldamalgamates.mixinextensions.ItemStackExtension
import marczeugs.goldamalgamates.mixinextensions.ToolItemExtension
import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolItem
import net.minecraft.tag.BlockTags
import kotlin.math.roundToInt

object ItemStackMixinImpl {
    @JvmStatic
    fun isSuitableFor(stack: ItemStack, blockState: BlockState) =
        when (val item = stack.item) {
            is MiningToolItem -> {
                val miningLevel = item.material.miningLevel + (ToolItemMixinImpl.getAmalgamateSlots(stack)?.sumOf { it.miningLevelIncrease } ?: 0)

                when {
                    miningLevel < MiningLevels.DIAMOND && blockState.isIn(BlockTags.NEEDS_DIAMOND_TOOL) -> false
                    miningLevel < MiningLevels.IRON && blockState.isIn(BlockTags.NEEDS_IRON_TOOL) -> false
                    miningLevel < MiningLevels.STONE && blockState.isIn(BlockTags.NEEDS_STONE_TOOL) -> false
                    else -> blockState.isIn((item as MiningToolItemAccessor).effectiveBlocks)
                }
            }
            else -> null
        }

    @JvmStatic
    fun getStackMaxDamage(stack: ItemStack) = (stack.item as? ToolItem)?.let { item ->
        val amalgamateSlots = (item as ToolItemExtension).getAmalgamateSlots(stack) ?: listOf()

        (
            (item.material.durability + amalgamateSlots.sumOf { it.flatDurabilityIncrease }) *
            (amalgamateSlots.fold(1.0) { acc, next -> acc * (1.0 + next.percentDurabilityIncrease) })
        ).roundToInt()
    } ?: stack.item.maxDamage

    @JvmStatic
    fun absorbDamageWithShield(stack: ItemStack, amount: Int) {
        @Suppress("CAST_NEVER_SUCCEEDS")
        val extendedStack = stack as ItemStackExtension

        val absorbedPart = if (extendedStack.currentShield >= amount) {
            amount
        } else {
            extendedStack.currentShield
        }

        if (absorbedPart > 0) {
            extendedStack.shieldRechargeTimer = -1
        }

        stack.damage = stack.damage + (amount - absorbedPart)
        extendedStack.currentShield -= absorbedPart
    }
}