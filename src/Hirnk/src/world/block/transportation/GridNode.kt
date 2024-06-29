package Hirnk.src.world.block.transportation

import Hirnk.src.world.block.transportation.IGridNode.Companion.linked2
import arc.Core
import arc.func.Prov
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import plumy.core.assets.TR
import plumy.dsl.castBuild
import plumy.dsl.config

class GridNode(name: String) : GridBlock(name) {
    var range = 200f
    var stroke = 5f
    var connections = 3

    lateinit var bridgeRegion: TR
    lateinit var topRegion: TR

    init {
        configurable = true
        saveConfig = true
        drawArrow = false
        canOverdrive = false
        update = false
        clipSize

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

    override fun load() {
        super.load()
        bridgeRegion = Core.atlas.find("$name-bridge")
        topRegion = Core.atlas.find("$name-top")
    }

    fun GridBuild.linkValid(other: IGridNode): Boolean {
        return links.size < connections && dst(other) <= this@GridNode.range
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

            Draw.z(Layer.blockOver - 0.01f)
            Lines.stroke(stroke)

            linked2.forEach { other ->
                if(other is GridNodeBuild && other.id() >= id) return@forEach //prevent overlapping
                val a = angleTo(other)

                if(a >= 45f && a < 225f) Lines.line(bridgeRegion, other.x, other.y, x, y, false)
                else Lines.line(bridgeRegion, x, y, other.x, other.y, false)
            }

            Draw.z(Layer.blockOver)
            Draw.rect(topRegion, x, y)
            drawPlaceText("${graph.entity.id}", tileX(), tileY(), true)
        }
    }
}