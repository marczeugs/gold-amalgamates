package marczeugs.goldamalgamates.mixin.common;

import marczeugs.goldamalgamates.mixinimpl.common.SmithingScreenHandlerMixinImpl;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    @Redirect(
        method = "updateResult()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/RecipeManager;getAllMatches(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/List;"
        )
    )
    public List<SmithingRecipe> addAmalgamateRecipes(RecipeManager instance, RecipeType<SmithingRecipe> type, Inventory inventory, World world) {
        return SmithingScreenHandlerMixinImpl.addAmalgamateRecipes(instance, type, inventory, world);
    }
}
