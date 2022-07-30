package marczeugs.goldamalgamates.mixin.client;

import marczeugs.goldamalgamates.mixinimpl.client.HeldItemRendererMixinImpl;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Redirect(
        method = "updateHeldItems()V",
        at = @At(
            value = "FIELD",
            opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;mainHand:Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack itemStackComparisonForBobbingAnimationShieldFix(HeldItemRenderer heldItemRenderer) {
        return HeldItemRendererMixinImpl.itemStackComparisonForBobbingAnimationShieldFix(heldItemRenderer);
    }
}
