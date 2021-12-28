package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.Amalgamate;
import marczeugs.goldamalgamates.mixinextensions.ToolItemExtension;
import marczeugs.goldamalgamates.mixinimpl.common.ToolItemMixinImpl;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ToolItem.class)
public class ToolItemMixin extends Item implements ToolItemExtension {
    private ToolItemMixin(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public List<Amalgamate> getAmalgamateSlots(@NotNull ItemStack stack) {
        return ToolItemMixinImpl.getAmalgamateSlots(stack);
    }

    @Override
    public void setAmalgamateSlots(@NotNull ItemStack stack, @NotNull List<? extends Amalgamate> amalgamateSlots) {
        ToolItemMixinImpl.setAmalgamateSlots(stack, amalgamateSlots);
    }

    @Override
    public Integer getCurrentShield(@NotNull ItemStack stack) {
        return ToolItemMixinImpl.getCurrentShield(stack);
    }

    @Override
    public void setCurrentShield(@NotNull ItemStack stack, int currentShield) {
        ToolItemMixinImpl.setCurrentShield(stack, currentShield);
    }

    @Override
    public Integer getShieldRechargeTimer(@NotNull ItemStack stack) {
        return ToolItemMixinImpl.getShieldRechargeTimer(stack);
    }

    @Override
    public void setShieldRechargeTimer(@NotNull ItemStack stack, int shieldRechargeTimer) {
        ToolItemMixinImpl.setShieldRechargeTimer(stack, shieldRechargeTimer);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, @Nullable World world, @NotNull List<Text> tooltip, @NotNull TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        ToolItemMixinImpl.appendTooltip(stack, tooltip);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        ToolItemMixinImpl.inventoryTick(stack, world);
    }
}
