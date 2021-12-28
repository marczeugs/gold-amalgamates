package marczeugs.goldamalgamates.mixin.client;

import marczeugs.goldamalgamates.mixinimpl.client.ClientPlayerInteractionManagerMixinImpl;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Redirect(
        method = "isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;areNbtEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean areNbtEqualWithShieldExceptions(ItemStack left, ItemStack right) {
        return ClientPlayerInteractionManagerMixinImpl.areNbtEqualWithShieldExceptions(left, right);
    }
}
