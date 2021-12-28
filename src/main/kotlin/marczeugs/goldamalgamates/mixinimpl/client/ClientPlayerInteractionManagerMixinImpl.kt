package marczeugs.goldamalgamates.mixinimpl.client

import net.minecraft.item.ItemStack

object ClientPlayerInteractionManagerMixinImpl {
    @JvmStatic
    fun areNbtEqualWithShieldExceptions(left: ItemStack, right: ItemStack) =
        ItemStack.areNbtEqual(
            left.copy().apply { nbt?.remove("ShieldRechargeTimer") },
            right.copy().apply { nbt?.remove("ShieldRechargeTimer") }
        )
}