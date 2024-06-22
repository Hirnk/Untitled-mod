package Hirnk.src.world.block.transportation

import Hirnk.src.world.block.transportation.IGridNode.Companion.linkedVertices
import arc.func.Prov
import arc.graphics.g2d.Lines
import arc.struct.IntSeq
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.world.Block
import plumy.dsl.castBuild
import plumy.dsl.config

class GridNode(name: String) : Block(name) {
    var range = 200f

    init {
        configurable = true
        canOverdrive = false
        drawArrow = false
        saveConfig = true
        destructible = true
        update = false

        buildType = Prov { GridNodeBuild() }

        config<GridNodeBuild, Int> {
            val target = it.castBuild<GridNodeBuild>() ?: return@config
            if (!linkValid(target)) {
                deselect()
                return@config
            }

            if (links.contains(it)) {
                disconnectTwoWay(target)
                reflow(target)
                return@config
            }
            link(target)
        }
    }

    fun GridNodeBuild.linkValid(other: IGridNode): Boolean {
        return other.dst(this) <= this@GridNode.range
    }

    inner class GridNodeBuild : Building(), IGridNode {
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

        override fun onConfigureBuildTapped(other: Building): Boolean {
            if (other != this) {
                configure(other.pos())
            }
            return true
        }

        override fun drawConfigure() {
            super.drawConfigure()
            Drawf.dashCircle(x, y, range, team.color)
        }

        override fun draw() {
            super.draw()

            linkedVertices.forEach { other ->
                Lines.dashLine(x, y, other.x, other.y, 3)
            }

            drawPlaceText("${graph.entity.id}", tileX(), tileY(), true)
        }
    }
}