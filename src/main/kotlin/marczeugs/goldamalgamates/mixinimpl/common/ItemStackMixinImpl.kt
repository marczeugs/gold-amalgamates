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
    fun isSuitableFor(instance: ItemStack, blockState: BlockState) =
        when (val item = instance.item) {
            is MiningToolItem -> {
                val miningLevel = item.material.miningLevel + (ToolItemMixinImpl.getAmalgamateSlots(instance)?.sumOf { it.miningLevelIncrease } ?: 0)

                when {
                    miningLevel < MiningLevels.DIAMOND && blockState.isIn(BlockTags.NEEDS_DIAMOND_TOOL) -> false
                    miningLevel < MiningLevels.IRON && blockState.isIn(BlockTags.NEEDS_IRON_TOOL) -> false
                    miningLevel < MiningLevels.STONE && blockState.isIn(BlockTags.NEEDS_STONE_TOOL) -> false
                    else -> blockState.isIn((item as MiningToolItemAccessor).effectiveBlocks)
                }
            }
            else -> item.isSuitableFor(blockState)
        }

    /*@JvmStatic
    fun updateItemStackStats(stack: ItemStack) {
        (stack.item as? ToolItem)?.let { item ->
            val amalgamateSlots = (item as ToolItemExtension).getAmalgamateSlots(stack) ?: listOf()

            @Suppress("CAST_NEVER_SUCCEEDS")
            (stack as ItemStackExtension).stackMaxDamage = (
                (item.material.durability + amalgamateSlots.sumOf { it.flatDurabilityIncrease }) *
                (amalgamateSlots.fold(1f) { acc, next -> acc * (1f + next.percentDurabilityIncrease) })
            ).roundToInt()

            /*val baseAttackDamage = when (item) {
                is MiningToolItem -> item.attackDamage
                is SwordItem -> item.attackDamage
                else -> unreachable()
            }

            val baseAttackSpeed = when (item) {
                is MiningToolItem -> (item as MiningToolItemExtension).attackSpeed
                is SwordItem -> (item as SwordItemExtension).attackSpeed
                else -> unreachable()
            }

            upsertToolItemStackAttributes(
                stack,
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                EntityAttributeModifier(
                    GoldAmalgamatesMod.AMALGAMATE_ATTACK_DAMAGE_MODIFIER_ID,
                    "Amalgamate modifier",
                    baseAttackDamage + amalgamateSlots.sumOf { it.attackDamageIncrease }.toDouble(),
                    EntityAttributeModifier.Operation.ADDITION
                )
            )

            upsertToolItemStackAttributes(
                stack,
                EntityAttributes.GENERIC_ATTACK_SPEED,
                EntityAttributeModifier(
                    GoldAmalgamatesMod.AMALGAMATE_ATTACK_SPEED_MODIFIER_ID,
                    "Amalgamate modifier",
                    baseAttackSpeed.toDouble(),
                    EntityAttributeModifier.Operation.ADDITION
                )
            )*/

            println("update amalgamates $amalgamateSlots maxdamage ${item.maxDamage}")
        }
    }*/

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
        println("absorbing ${stack.nbt}")
        extendedStack.currentShield -= absorbedPart
    }
}