package hirnk.src.content

import mindustry.content.Items
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.blocks.liquid.LiquidRouter
import hirnk.src.world.block.LiquidCell

object UntiltedBlocks {
    lateinit var copperTank: Block
    lateinit var copperTankBig: Block

    fun load() {
        copperTank = LiquidCell("copper-tank-2").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 550
            size = 2
            liquidCapacity = 500f
            squareSprite = false
        }

        copperTankBig = LiquidRouter("copper-tank-4").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45*4, Items.lead, 20*4))
            health = 3500
            size = 4
            liquidCapacity = 2200f
            squareSprite = false
        }
    }
}