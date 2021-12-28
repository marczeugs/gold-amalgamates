package marczeugs.goldamalgamates.mixin.client;

import marczeugs.goldamalgamates.mixinimpl.client.ItemRendererMixinImpl;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Redirect(
        method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
        )
    )
    private boolean isItemBarVisibleForStack(ItemStack stack) {
        return ItemRendererMixinImpl.isItemBarVisibleForStack(stack);
    }

    @Inject(
        method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isItemBarVisible()Z"
        )
    )
    private void renderAmalgamateIndicator(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        ItemRendererMixinImpl.renderAmalgamateIndicator((ItemRenderer) (Object) this, stack, x, y);
    }

    @Inject(
        method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiQuad(Lnet/minecraft/client/render/BufferBuilder;IIIIIIII)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void enableBlend(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        ItemRendererMixinImpl.enableBlendAndTexture();
    }

    @Inject(
        method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V",
            ordinal = 0,
            remap = false
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void renderShieldBar(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci, Tessellator string, BufferBuilder immediate, int i, int j) {
        ItemRendererMixinImpl.renderShieldBar((ItemRenderer) (Object) this, stack, x, y, immediate);
    }
}
