package Hirnk.src.world.block.distribution.fluid

import Hirnk.src.world.graph.Graph
import arc.Core
import arc.func.Prov
import arc.struct.IntSeq
import mindustry.Vars
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.ui.Bar
import mindustry.world.Block
import mindustry.world.meta.BlockGroup

open class FluidBlock(name: String) : Block(name) {
    var fluidCapacity = 500f
    var conductivity = 1f

    init {
        update = true
        buildType = Prov { FluidBuild() }

        group = BlockGroup.liquids
    }

    override fun setBars() {
        super.setBars()

        addBar<FluidBuild>("fluid-amount") {
            Bar(
                { Core.bundle.format("untitled-bar.fluid-amount", it.fluidAmount) },
                { Pal.lancerLaser },
                { 1f }
            )
        }
    }

    open inner class FluidBuild : Building(), IFluidBuild {
        override var graph: Graph<FluidGraph> = FluidGraph()
        override var graphInit: Boolean = false
        override val links: IntSeq = IntSeq()

        override var sn = 0f
        override var k = conductivity
        override var fluidAmount = 0f

        override fun drawSelect() {
            super.drawSelect()

            graph.all.forEach {
                Drawf.square(it.x, it.y, it.block().size * Vars.tilesize / 2.5f, 0f)
            }
        }

        override fun onProximityRemoved() {
            super.onProximityRemoved()

            graph.unlink(this)
        }

        override fun created() {
            super.created()
            graph.initNode(this)
        }
    }
}