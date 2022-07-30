package marczeugs.goldamalgamates.mixinimpl.common

import marczeugs.goldamalgamates.Amalgamate
import marczeugs.goldamalgamates.GoldAmalgamatesMod
import marczeugs.goldamalgamates.mixinextensions.ItemStackExtension
import marczeugs.goldamalgamates.mixinextensions.ToolItemExtension
import net.fabricmc.fabric.api.util.NbtType
import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import kotlin.math.floor
import kotlin.math.roundToInt

object ToolItemMixinImpl {
    private const val AMALGAMATES_KEY = "Amalgamates"
    private const val CURRENT_SHIELD_KEY = "CurrentShield"
    private const val SHIELD_RECHARGE_TIMER_KEY = "ShieldRechargeTimer"

    @JvmStatic
    fun getAmalgamateSlots(stack: ItemStack) = stack.nbt
        ?.takeIf { it.contains(AMALGAMATES_KEY, NbtType.LIST) }
        ?.getList(AMALGAMATES_KEY, NbtType.STRING)
        ?.filterIsInstance<NbtString>()
        ?.mapNotNull { element -> Amalgamate.list.find { it.serializedName == element.asString() } }

    @JvmStatic
    fun setAmalgamateSlots(stack: ItemStack, amalgamates: List<Amalgamate>) {
        stack.orCreateNbt.put(
            AMALGAMATES_KEY,
            NbtList().apply {
                amalgamates.forEach {
                    add(NbtString.of(it.serializedName))
                }
            }
        )
    }

    @JvmStatic
    fun getCurrentShield(stack: ItemStack) = stack.nbt
        ?.takeIf { it.contains(CURRENT_SHIELD_KEY, NbtType.INT) }
        ?.getInt(CURRENT_SHIELD_KEY)
        ?: 0

    @JvmStatic
    fun setCurrentShield(stack: ItemStack, currentShield: Int) {
        stack.orCreateNbt.put(
            CURRENT_SHIELD_KEY,
            NbtInt.of(currentShield)
        )
    }

    @JvmStatic
    fun getShieldRechargeTimer(stack: ItemStack) = stack.nbt
        ?.takeIf { it.contains(SHIELD_RECHARGE_TIMER_KEY, NbtType.INT) }
        ?.getInt(SHIELD_RECHARGE_TIMER_KEY)
        ?: 0

    @JvmStatic
    fun setShieldRechargeTimer(stack: ItemStack, shieldRechargeTimer: Int) {
        stack.orCreateNbt.put(
            SHIELD_RECHARGE_TIMER_KEY,
            NbtInt.of(shieldRechargeTimer)
        )
    }

    @JvmStatic
    fun appendTooltip(stack: ItemStack, tooltip: MutableList<Text>) {
        (stack.item as ToolItemExtension).getAmalgamateSlots(stack)?.let { slots ->
            if (slots.isNotEmpty()) {
                tooltip.add(
                    Text.translatable("${GoldAmalgamatesMod.MOD_ID}.tooltip.list").apply {
                        slots.take(2).forEachIndexed { index, amalgamate ->
                            append(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.${amalgamate.serializedName}.name"))

                            if (index < slots.size - 1) {
                                append(Text.of(", "))
                            }
                        }
                    }.formatted(Formatting.GRAY)
                )

                if (slots.size > 2) {
                    tooltip.add(
                        Text.empty().apply {
                            slots.withIndex().drop(2).forEach { (index, amalgamate) ->
                                append(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.${amalgamate.serializedName}.name"))

                                if (index < slots.size - 1) {
                                    append(Text.of(", "))
                                }
                            }
                        }.formatted(Formatting.GRAY)
                    )
                }

                slots.sumOf { it.miningLevelIncrease }.takeIf { it > 0 }?.let {
                    val miningLevelText = when ((stack.item as ToolItem).material.miningLevel + it) {
                        MiningLevels.HAND -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.hand")
                        MiningLevels.WOOD -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.wood")
                        MiningLevels.STONE -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.stone")
                        MiningLevels.IRON -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.iron")
                        MiningLevels.DIAMOND -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.diamond")
                        else -> Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.netherite")
                    }

                    tooltip.add(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.mininglevel.description").append(miningLevelText).formatted(Formatting.DARK_GRAY))
                }

                (slots.sumOf { it.flatDurabilityIncrease } to slots.fold(1.0) { acc, next -> acc * (1.0 + next.percentDurabilityIncrease) })
                    .takeIf { (flat, percent) -> flat > 0 || percent > 1f }
                    ?.let { (flat, percent) ->
                        tooltip.add(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.durabilityincrease.description", flat, ((percent - 1f) * 100).roundToInt()).formatted(Formatting.DARK_GRAY))
                    }

                slots.sumOf { it.enchantabilityIncrease }.takeIf { it > 0 }?.let {
                    tooltip.add(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.enchantabilityincrease.description", (stack.item as ToolItem).material.enchantability + it, it).formatted(Formatting.DARK_GRAY))
                }

                slots.count { it == Amalgamate.Emerald }.takeIf { it > 0 }?.let {
                    tooltip.add(Text.translatable("${GoldAmalgamatesMod.MOD_ID}.autorepair.description", it, Amalgamate.Emerald.TICKS_PER_REPAIR / 20).formatted(Formatting.DARK_GRAY))
                }

                slots.count { it == Amalgamate.Copper }.takeIf { it > 0 }?.let {
                    tooltip.add(
                        Text.translatable(
                            "${GoldAmalgamatesMod.MOD_ID}.shield.description.line1",
                            (Amalgamate.Copper.SHIELD_PERCENT_PER_SLOT * it * 100).roundToInt()
                        ).formatted(Formatting.DARK_GRAY)
                    )

                    tooltip.add(
                        Text.translatable(
                            "${GoldAmalgamatesMod.MOD_ID}.shield.description.line2",
                            (Amalgamate.Copper.BASE_SHIELD_RECHARGE_TIMEOUT_TICKS + Amalgamate.Copper.SHIELD_RECHARGE_TIMEOUT_TICKS_DIFF_PER_SLOT * it) / 20
                        ).formatted(Formatting.DARK_GRAY)
                    )

                    @Suppress("CAST_NEVER_SUCCEEDS")
                    tooltip.add(
                        Text.translatable(
                            "${GoldAmalgamatesMod.MOD_ID}.shield.currentvalue",
                            (stack as ItemStackExtension).currentShield,
                            floor(it * Amalgamate.Copper.SHIELD_PERCENT_PER_SLOT * stack.maxDamage).roundToInt()
                        ).formatted(Formatting.DARK_GRAY)
                    )
                }

                /*slots.sumOf { it.attackDamageIncrease }.takeIf { it > 0 }?.let {
                    tooltip.add(TranslatableText("${GoldAmalgamatesMod.MOD_ID}.attackdamageincrease.description", it).formatted(Formatting.GRAY))
                }*/

                if (stack.hasEnchantments()) {
                    tooltip.add(Text.empty())
                }
            }
        }
    }

    @JvmStatic
    fun inventoryTick(stack: ItemStack, world: World) {
        if (world.isClient) {
            return
        }

        val amalgamateSlots = getAmalgamateSlots(stack)

        if (amalgamateSlots?.getOrNull((world.time % Amalgamate.Emerald.TICKS_PER_REPAIR).toInt()) == Amalgamate.Emerald && stack.damage > 0) {
            stack.damage--
        }

        @Suppress("CAST_NEVER_SUCCEEDS")
        val extendedStack = stack as ItemStackExtension

        val copperAmalgamateSlots = amalgamateSlots?.count { it == Amalgamate.Copper } ?: 0

        if (extendedStack.shieldRechargeTimer == -1 && copperAmalgamateSlots > 0) {
            extendedStack.shieldRechargeTimer = Amalgamate.Copper.BASE_SHIELD_RECHARGE_TIMEOUT_TICKS - copperAmalgamateSlots * Amalgamate.Copper.SHIELD_RECHARGE_TIMEOUT_TICKS_DIFF_PER_SLOT
        }

        if (extendedStack.shieldRechargeTimer == 0) {
            val maximumShieldValue = floor(copperAmalgamateSlots * Amalgamate.Copper.SHIELD_PERCENT_PER_SLOT * stack.maxDamage).roundToInt()

            if (extendedStack.currentShield < maximumShieldValue) {
                extendedStack.currentShield = (extendedStack.currentShield + copperAmalgamateSlots * Amalgamate.Copper.SHIELD_RECHARGE_PER_TICK_PER_SLOT).coerceAtMost(maximumShieldValue)
            }
        } else if (extendedStack.shieldRechargeTimer > 0 && world.time % 20 == 0L) {
            extendedStack.shieldRechargeTimer = (extendedStack.shieldRechargeTimer - 20).coerceAtLeast(0)
        }
    }
}