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
    val progresses = ArrayList<RecipeProgress>()

    fun bars(b: Block) {}
}

class RecipeProgress() {
    val consumers = ArrayList<Consume>()
    val outputItems = ArrayList<ItemStack>()
    val outputFluids = ArrayList<LiquidStack>()
    var craftTime = 60f
    var craftEffect = Fx.smeltsmoke
}

open class RecipeHandler(
    open val r: Recipe,
    open val building: Building
) {
    var progress = FloatArray(r.progresses.size) { 0f }

    fun craft(p: RecipeProgress) = building.run {
        p.outputItems.forEach { for (i in 0 until it.amount) offload(it.item) }
        p.consumers.forEach { it.trigger(this) }
        if (wasVisible) p.craftEffect.at(x, y)
    }

    fun update() = building.run {
        progress.forEachIndexed{ i, _ ->
            val p = r.progresses[i]
            progress[i] += getProgressIncrease(p.craftTime)
            if (p.outputFluids.isNotEmpty()) {
                val inc = getProgressIncrease(1f)
                p.outputFluids.forEach {
                    handleLiquid(this, it.liquid, it.amount * inc)
                }
            }
            if (progress[i] >= 1f) {
                progress[i] %= 1f
                craft(p)
            }
        }
    }

    fun efficiency(): Float {
        var e = 1f
        r.progresses.forEach { it.consumers.forEach { e *= it.efficiency(building) } }
        return e
    }

    fun efficiencyScale(): Float {
        var e = 1f
        r.progresses.forEach { it.consumers.forEach { e *= it.efficiency(building) } }
        return e
    }

    fun buildTable(table: Table) {
        r.progresses.forEach {
            it.consumers.forEach { it.build(building, table) }
        }
        table.image(Icon.right)
        r.progresses.forEach { p ->
            p.outputItems.forEach { table.add(ItemDisplay(it.item, it.amount, p.craftTime, false)) }
            p.outputFluids.forEach { table.add(LiquidDisplay(it.liquid, it.amount, true)) }
        }
    }
}