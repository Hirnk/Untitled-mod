package Hirnk.src.world.block.crafter.recipe

import arc.scene.ui.layout.Table
import mindustry.content.Fx
import mindustry.gen.Building
import mindustry.gen.Icon
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.ui.ItemDisplay
import mindustry.ui.LiquidDisplay
import mindustry.world.Block
import mindustry.world.consumers.Consume

open class Recipe() {
    val consumers = ArrayList<Consume>()
    val outputItems = ArrayList<ItemStack>()
    val outputFluids = ArrayList<LiquidStack>()
    var craftTime = 60f
    var craftEffect = Fx.smeltsmoke
    var name = ""
    var optional = false
    var boost = false

    fun bars(b: Block) {}
}

class RecipeHandler(val r: Recipe,val building: Building) {
    var progress = 0f

    fun craft() = building.run {
        r.outputItems.forEach { for (i in 0 until it.amount) offload(it.item) }
        r.consumers.forEach { it.trigger(this) }
        progress %= 1f
        if (wasVisible) r.craftEffect.at(x, y)
    }

    fun update() = building.run {
        progress += getProgressIncrease(r.craftTime)

        if (r.outputFluids.isNotEmpty()) {
            val inc = getProgressIncrease(1f)
            r.outputFluids.forEach {
                handleLiquid(this, it.liquid, it.amount * inc)
            }
        }

        if (progress >= 1f) craft()
    }

    fun efficiency(): Float {
        var e = 1f
        r.consumers.forEach { e *= it.efficiency(building) }
        return e
    }

    fun efficiencyScale(): Float {
        var e = 1f
        r.consumers.forEach { e += (it.efficiencyMultiplier(building) - 1f) }
        return e
    }

    fun buildTable(table: Table) {
        r.consumers.forEach { it.build(building, table) }
        table.image(Icon.right)
        r.outputItems.forEach { table.add(ItemDisplay(it.item, it.amount, r.craftTime, false)) }
        r.outputFluids.forEach { table.add(LiquidDisplay(it.liquid, it.amount, true)) }
    }
}