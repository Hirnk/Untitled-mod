package Hirnk.src.world.block.transportation

import Hirnk.src.content.UntitledPackets
import arc.func.Prov
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import mindustry.gen.Building
import mindustry.graphics.Layer
import plumy.dsl.castBuild
import plumy.dsl.config

class GridPort(name: String) : GridBlock(name) {
    var packet = UntitledPackets.basic
    var speed = 20f

    init {
        configurable = true
        drawArrow = false
        saveConfig = true
        destructible = true
        update = true

        buildType = Prov { GridPortBuild() }

        config<GridPortBuild, Int> {
            val target = it.castBuild<GridPortBuild>() ?: return@config

            destination = if (target != this && target != destination && target.graph == graph) target else null
        }
    }

    inner class GridPortBuild : GridBuild() {
        var destination: GridPortBuild? = null
        var reload = 0f

        fun getPath(): Path? {
            val d = destination ?: return null
            return graph.getPath(this, d)
        }

        override fun updateTile() {
            super.updateTile()

            val p = getPath() ?: return

            if (reload >= 1f) {
                reload %= 1f
                packet.create(p, x, y)
            } else {
                reload += getProgressIncrease(speed)
            }
        }

        override fun onConfigureBuildTapped(other: Building): Boolean {
            if (other != this) {
                configure(other.pos())
                return false
            }
            return true
        }

        override fun draw() {
            super.draw()

            val path = getPath()
            if (path != null) {
                Draw.z(Layer.blockOver)
                Draw.color(Color.red)
                Lines.beginLine()
                Lines.linePoint(this)
                path.forEach {
                    Lines.linePoint(it)
                }
                Lines.endLine()
                Draw.z()
            }

            drawPlaceText("${graph.entity.id}", tileX(), tileY(), true)
        }
    }
}