package marczeugs.goldamalgamates

import marczeugs.goldamalgamates.mixinextensions.ToolItemExtension
import marczeugs.goldamalgamates.mixinimpl.common.SmithingScreenHandlerMixinImpl
import net.fabricmc.api.ModInitializer
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterials
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

class GoldAmalgamatesMod : ModInitializer {
    companion object {
        const val MOD_ID = "goldamalgamates"

        val LOGGER = LogManager.getLogger()

        @JvmStatic
        val amalgamateRecipes by lazy {
            Registry.ITEM
                .filterIsInstance<ToolItem>()
                .filter { it.material == ToolMaterials.GOLD }
                .flatMap { item -> Amalgamate.list.map { item to it } }
                .map { (item, amalgamate) ->
                    AmalgamateSmithingRecipe(
                        toolItem = item,
                        amalgamate = amalgamate
                    )
                }
        }

        class AmalgamateSmithingRecipe(
            val toolItem: ToolItem,
            val amalgamate: Amalgamate
        ) : SmithingRecipe(
            Identifier(MOD_ID, "amalgamate_${toolItem::class.java.simpleName.lowercase()}_${Registry.ITEM.getId(amalgamate.item).path}"),
            Ingredient.ofItems(toolItem),
            Ingredient.ofItems(amalgamate.item),
            ItemStack(toolItem)
        ) {
            override fun craft(inventory: Inventory): ItemStack = super.craft(inventory).also {
                SmithingScreenHandlerMixinImpl.addAmalgamateToStack(amalgamate, it)
            }

            override fun matches(inventory: Inventory, world: World) =
                super.matches(inventory, world) && inventory.getStack(0).let {
                    ((it.item as? ToolItemExtension)?.getAmalgamateSlots(it)?.size ?: 0) < Amalgamate.MAX_AMALGAMATE_SLOTS_PER_ITEM
                }
        }
    }

    override fun onInitialize() {
        LOGGER.info("[Gold Amalgamates] Mod loaded.")
    }
}