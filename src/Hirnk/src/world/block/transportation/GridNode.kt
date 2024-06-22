package Hirnk.src.world.block.transportation

import arc.func.Prov
import arc.graphics.g2d.Lines
import mindustry.gen.Building
import mindustry.graphics.Drawf
import plumy.dsl.castBuild
import plumy.dsl.config

class GridNode(name: String) : GridBlock(name) {
    var range = 200f

    init {
        configurable = true
        saveConfig = true
        drawArrow = false
        canOverdrive = false
        update = false

        buildType = Prov { GridNodeBuild() }

        config<GridBuild, Int> {
            val target = it.castBuild<GridBuild>() ?: return@config
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

    fun GridBuild.linkValid(other: IGridNode): Boolean {
        return other.dst(this) <= this@GridNode.range
    }

    inner class GridNodeBuild : GridBuild() {
        override fun onConfigureBuildTapped(other: Building): Boolean {
            if (other != this) {
                configure(other.pos())
                return false
            }
            return true
        }

        override fun drawConfigure() {
            super.drawConfigure()
            Drawf.dashCircle(x, y, range, team.color)
        }

        override fun draw() {
            super.draw()

            Lines.stroke(2.5f)

            linkedVertices.forEach { other ->
                Lines.line(x, y, other.x, other.y)
            }

            drawPlaceText("${graph.entity.id}", tileX(), tileY(), true)
        }
    }
}