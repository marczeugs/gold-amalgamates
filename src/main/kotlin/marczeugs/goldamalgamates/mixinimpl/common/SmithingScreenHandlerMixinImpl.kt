package marczeugs.goldamalgamates.mixinimpl.common

import marczeugs.goldamalgamates.Amalgamate
import marczeugs.goldamalgamates.GoldAmalgamatesMod
import marczeugs.goldamalgamates.mixinextensions.ToolItemExtension
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.world.World

object SmithingScreenHandlerMixinImpl {
    @JvmStatic
    fun addAmalgamateRecipes(recipeManager: RecipeManager, recipeType: RecipeType<SmithingRecipe>, inventory: Inventory, world: World) =
        recipeManager.getAllMatches(recipeType, inventory, world) + GoldAmalgamatesMod.amalgamateRecipes.filter { recipeType.match(it, world, inventory).isPresent }

    @JvmStatic
    fun addAmalgamateToStack(amalgamate: Amalgamate, stack: ItemStack) {
        (stack.item as? ToolItem)?.let { item ->
            val amalgamateSlots = ((item as ToolItemExtension).getAmalgamateSlots(stack) ?: listOf()).let {
                if (it.size < Amalgamate.MAX_AMALGAMATE_SLOTS_PER_ITEM) {
                    it + amalgamate
                } else {
                    it
                }
            }

            item.setAmalgamateSlots(stack, amalgamateSlots)
        }
    }

    /*private fun upsertToolItemStackAttributes(stack: ItemStack, attribute: EntityAttribute, modifier: EntityAttributeModifier) {
        println("upsert ${stack.orCreateNbt.contains("AttributeModifiers", net.fabricmc.fabric.api.util.NbtType.LIST)} ${stack.nbt!!.getList("AttributeModifiers", net.fabricmc.fabric.api.util.NbtType.COMPOUND)}")

        if (!stack.orCreateNbt.contains("AttributeModifiers", net.fabricmc.fabric.api.util.NbtType.LIST)) {
            stack.nbt!!.put("AttributeModifiers", NbtList())
        }

        val existingModifiers = stack.nbt!!.getList("AttributeModifiers", net.fabricmc.fabric.api.util.NbtType.COMPOUND)

        existingModifiers.find { EntityAttributeModifier.fromNbt(it as? NbtCompound)?.id == modifier.id }?.let {
            (it as NbtCompound).copyFrom(modifier.toNbt())
        } ?: run {
            existingModifiers.add(
                modifier.toNbt().apply {
                    putString("AttributeName", Registry.ATTRIBUTE.getId(attribute).toString())
                    putString("Slot", EquipmentSlot.MAINHAND.name.lowercase())
                }
            )
        }
    }*/
}