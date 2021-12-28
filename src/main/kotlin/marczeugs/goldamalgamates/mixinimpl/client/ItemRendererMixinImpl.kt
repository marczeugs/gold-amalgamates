package marczeugs.goldamalgamates.mixinimpl.client

import com.mojang.blaze3d.systems.RenderSystem
import marczeugs.goldamalgamates.Amalgamate
import marczeugs.goldamalgamates.mixin.client.ItemRendererAccessor
import marczeugs.goldamalgamates.mixinextensions.ItemStackExtension
import marczeugs.goldamalgamates.mixinimpl.common.ToolItemMixinImpl
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterials
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

object ItemRendererMixinImpl {
    @JvmStatic
    fun isItemBarVisibleForStack(stack: ItemStack): Boolean {
        val copperAmalgamateSlots = ToolItemMixinImpl.getAmalgamateSlots(stack)?.count { it == Amalgamate.Copper } ?: 0
        return stack.isItemBarVisible || stack.item.let { it is ToolItem && it.material == ToolMaterials.GOLD && copperAmalgamateSlots > 0 }
    }

    @JvmStatic
    fun renderAmalgamateIndicator(itemRenderer: ItemRenderer, stack: ItemStack, x: Int, y: Int) {
        if (stack.item.let { it is ToolItem && it.material == ToolMaterials.GOLD }) {
            val amalgamateSlots = ToolItemMixinImpl.getAmalgamateSlots(stack)

            if ((amalgamateSlots?.size ?: 0) > 0) {
                RenderSystem.disableDepthTest()
                RenderSystem.enableBlend()
                RenderSystem.enableTexture()

                val immediate = Tessellator.getInstance().buffer

                (itemRenderer as ItemRendererAccessor).invokeRenderGuiQuad(immediate, x, y, 1, 4, 0, 0, 0, 96)

                amalgamateSlots!!.fold(0) { xOffset, amalgamate ->
                    (itemRenderer as ItemRendererAccessor).invokeRenderGuiQuad(immediate, x + xOffset + 1, y, 3, 4, 0, 0, 0, 96)
                    (itemRenderer as ItemRendererAccessor).invokeRenderGuiQuad(immediate, x + xOffset + 1, y + 1, 2, 2, amalgamate.color.red, amalgamate.color.green, amalgamate.color.blue, 255)

                    xOffset + 3
                }
            }
        }
    }

    @JvmStatic
    fun enableBlendAndTexture() {
        RenderSystem.enableBlend()
        RenderSystem.enableTexture()
    }

    @JvmStatic
    fun renderShieldBar(itemRenderer: ItemRenderer, stack: ItemStack, x: Int, y: Int, immediate: BufferBuilder) {
        if (stack.item.let { it is ToolItem && it.material == ToolMaterials.GOLD }) {
            val amalgamateSlots = ToolItemMixinImpl.getAmalgamateSlots(stack)

            if ((amalgamateSlots?.size ?: 0) > 0) {
                @Suppress("CAST_NEVER_SUCCEEDS")
                val extendedStack = stack as ItemStackExtension
                val copperAmalgamateSlots = amalgamateSlots!!.count { it == Amalgamate.Copper }

                if (copperAmalgamateSlots > 0) {
                    val shieldBarWidth = ceil(extendedStack.currentShield * 13.0 / floor(copperAmalgamateSlots * Amalgamate.Copper.SHIELD_PERCENT_PER_SLOT * stack.maxDamage)).roundToInt().coerceAtMost(13)
                    (itemRenderer as ItemRendererAccessor).invokeRenderGuiQuad(immediate, x + 2, y + 13, shieldBarWidth, 2, 64, 160, 255, 192)
                }
            }
        }
    }
}