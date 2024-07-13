package Hirnk.src.world.block.transportation

import Hirnk.src.entities.special.GridPacket
import arc.func.Prov
import arc.struct.IntMap
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
        override val packets = IntMap<GridPacket>()

        override fun created() {
            super.created()
            graph.initNode(this)
        }

        override fun onProximityRemoved() {
            super.onProximityRemoved()
            removeFromGraph()
        }

        override fun draw() {
            super.draw()

            var text = ""
            packets.forEach {
                text += "${it.value.id}\n"
            }
            drawPlaceText("${graph.entity.id}\n$text", tileX(), tileY(), true)
        }
    }
}