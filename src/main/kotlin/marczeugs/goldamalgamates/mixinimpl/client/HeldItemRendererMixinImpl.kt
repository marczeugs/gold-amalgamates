package marczeugs.goldamalgamates.mixinimpl.client

import marczeugs.goldamalgamates.mixin.client.HeldItemRendererAccessor
import net.minecraft.client.render.item.HeldItemRenderer
import net.minecraft.item.ItemStack

object HeldItemRendererMixinImpl {
    @JvmStatic
    fun itemStackComparisonForBobbingAnimationShieldFix(heldItemRenderer: HeldItemRenderer): ItemStack? =
        if ((heldItemRenderer as HeldItemRendererAccessor).mainHand.nbt?.contains("ShieldRechargeTimer") == true) {
            heldItemRenderer.client.player!!.mainHandStack
        } else {
            heldItemRenderer.mainHand
        }
}