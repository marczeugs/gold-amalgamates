package marczeugs.goldamalgamates.mixinextensions

import marczeugs.goldamalgamates.Amalgamate
import net.minecraft.item.ItemStack

interface ToolItemExtension {
    fun getAmalgamateSlots(stack: ItemStack): List<Amalgamate>?
    fun setAmalgamateSlots(stack: ItemStack, amalgamates: List<Amalgamate>)
    fun getCurrentShield(stack: ItemStack): Int?
    fun setCurrentShield(stack: ItemStack, currentShield: Int)
    fun getShieldRechargeTimer(stack: ItemStack): Int?
    fun setShieldRechargeTimer(stack: ItemStack, shieldRechargeTimer: Int)
}