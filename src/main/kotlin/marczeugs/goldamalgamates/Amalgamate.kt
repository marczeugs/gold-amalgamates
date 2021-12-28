package marczeugs.goldamalgamates

import net.minecraft.item.Item
import net.minecraft.item.Items
import java.awt.Color

sealed class Amalgamate(val item: Item, val serializedName: String, val color: Color) {
    companion object {
        const val MAX_AMALGAMATE_SLOTS_PER_ITEM = 5

        val list by lazy {
            listOf(
                Copper,
                Iron,
                Lapis,
                Emerald,
                Obsidian,
                Diamond,
                Netherite
            )
        }
    }

    open val flatDurabilityIncrease = 0
    open val percentDurabilityIncrease = 0.0
    open val miningLevelIncrease = 0
    open val enchantabilityIncrease = 0
    //open val attackDamageIncrease = 0


    object Copper : Amalgamate(Items.COPPER_INGOT, "copper", Color(0xbf5935)) {
        const val SHIELD_PERCENT_PER_SLOT = 0.05
        const val SHIELD_RECHARGE_PER_TICK_PER_SLOT = 1
        const val BASE_SHIELD_RECHARGE_TIMEOUT_TICKS = 440
        const val SHIELD_RECHARGE_TIMEOUT_TICKS_DIFF_PER_SLOT = -40
    }

    object Iron : Amalgamate(Items.IRON_INGOT, "iron", Color(0xc5c5c5)) {
        override val flatDurabilityIncrease = 125
    }

    object Lapis : Amalgamate(Items.LAPIS_LAZULI, "lapis", Color.BLUE) {
        override val enchantabilityIncrease = 10
    }

    object Emerald : Amalgamate(Items.EMERALD, "emerald", Color.GREEN) {
        const val TICKS_PER_REPAIR = 100
    }

    object Obsidian : Amalgamate(Items.OBSIDIAN, "obsidian", Color.BLACK) {
        override val flatDurabilityIncrease = 250
        override val percentDurabilityIncrease = 0.1
    }

    object Diamond : Amalgamate(Items.DIAMOND, "diamond", Color.CYAN) {
        override val miningLevelIncrease = 2
        //override val attackDamageIncrease = 2
    }

    object Netherite : Amalgamate(Items.NETHERITE_INGOT, "netherite", Color(0x412128)) {
        override val percentDurabilityIncrease = 1.0
    }
}