package Hirnk.src.world.block.crafter

import arc.Core
import arc.func.Prov
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.gen.Tex
import mindustry.ui.Styles
import mindustry.world.Block
import mindustry.world.meta.BlockFlag
import plumy.dsl.config
import Hirnk.src.world.block.crafter.recipe.*

class MultiCrafter(name: String) : Block(name) {
    val recipes = ArrayList<Recipe>()

    init {
        update = true
        solid = true
        ambientSound = Sounds.machine
        sync = true
        ambientSoundVolume = 0.03f
        flags = EnumSet.of(BlockFlag.factory)
        drawArrow = false
        configurable = true
        saveConfig = true
        buildType = Prov { MultiCrafterBuild() }

        config<MultiCrafterBuild, Int> {
            if (currentRecipe == it) currentRecipe = -1
            else currentRecipe = it
        }
    }

    override fun init() {
        super.init()
        recipes.forEach { it.progresses.forEach { it.consumers.forEach { it.apply(this) } }}
    }

    override fun setBars() {
        super.setBars()
        recipes.forEach { it.bars(this) }
    }

    inner class MultiCrafterBuild : Building() {
        val handlers = Array(recipes.size) { RecipeHandler(recipes[it], this) }
        var currentRecipe = -1 //selected recipe, -1 for no recipe selected

        fun active() = currentRecipe != -1

        override fun updateTile() {
            if (!active()) return

            efficiency(handlers[currentRecipe].efficiency())
            handlers[currentRecipe].update()
        }

        override fun efficiencyScale(): Float {
            return if (active()) handlers[currentRecipe].efficiencyScale() else 0f
        }

        override fun config() = currentRecipe

        override fun buildConfiguration(table: Table) {
            table.table(Tex.button) {
                it.defaults().grow().margin(5f)
                it.add(Core.bundle.get("untitled-select-recipe")).row()
                it.table { t ->
                    handlers.forEachIndexed { i, j ->
                        t.left().button ({
                            b -> j.buildTable(b)
                        }, Styles.underlineb, { configure(i) })
                            .update { it.isChecked = currentRecipe == i }
                            .growX().fillY().pad(4f).marginTop(5f).marginBottom(5f).row()
                    }
                }.expandX()
            }
        }
    }
}

