package Hirnk.src.world.block.transportation.grid

import arc.func.Prov
import arc.struct.IntSeq
import mindustry.gen.Building
import mindustry.world.Block

open class GridBlock(name: String) : Block(name) {

    init {
        destructible = true
        update = false
        buildType = Prov { GridBuild() }
    }

    open inner class GridBuild : Building(), IGridNode {
        override var graph = GridGraph()
        override var graphInit = false
        override val links = IntSeq()

        override fun created() {
            super.created()
            graph.initNode(this)
        }

        override fun onProximityRemoved() {
            super.onProximityRemoved()
            removeFromGraph()
        }
    }
}