package Hirnk.src.world.block.distribution.grid

import Hirnk.src.entities.special.GridPacket
import Hirnk.src.util.graphic.UntitledPal
import Hirnk.src.world.block.distribution.grid.IGridNode.Companion.linked2
import arc.Core
import arc.func.Prov
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.ui.Bar
import plumy.core.assets.TR
import plumy.dsl.castBuild
import plumy.dsl.config

class GridNode(name: String) : GridBlock(name) {
    var range = 200f
    var stroke = 8f
    var connections = 7

    lateinit var bridgeRegion: TR
    lateinit var bridgeOutlineRegion: TR
    lateinit var topRegion: TR
    lateinit var outlineRegion: TR

    init {
        configurable = true
        saveConfig = true
        drawArrow = false
        canOverdrive = false
        update = false

        buildType = Prov { GridNodeBuild() }

        config<GridBuild, Int> {
            val target = it.castBuild<GridBuild>() ?: return@config

            if (links.contains(it)) {
                disconnectTwoWay(target)
                reflow(target)
                return@config
            }
            if (!linkValid(target)) {
                deselect()
                return@config
            }
            link(target)
        }
    }

    override fun init() {
        super.init()
        clipSize = range
    }

    override fun setBars() {
        super.setBars()
        addBar<GridNodeBuild>("connections") {
            Bar(
                { Core.bundle.format("untitled-bar.connections", it.links.size, connections) },
                { UntitledPal.grid },
                { it.links.size.toFloat() / connections }
            ).blink(Color.white)
        }
    }

    override fun load() {
        super.load()
        bridgeRegion = Core.atlas.find("$name-bridge")
        bridgeOutlineRegion = Core.atlas.find("$name-bridge-outline")
        topRegion = Core.atlas.find("$name-top")
        outlineRegion = Core.atlas.find("$name-outline")
    }

    fun GridBuild.linkValid(other: IGridNode): Boolean {
        return links.size < connections && dst(other) <= this@GridNode.range
    }

    inner class GridNodeBuild : GridBuild() {
        var packets = ArrayList<GridPacket>()

        override fun onDisconnect(other: IGridNode) {
            packets.retainAll { it.type.checkValid(it) }
            if (other is GridNodeBuild) other.packets.retainAll { it.type.checkValid(it) }
        }

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

            Lines.stroke(stroke)
            Draw.rect(outlineRegion, x, y)

            linked2.forEach { other ->
                if(other is GridNodeBuild && other.id() >= id) return@forEach //prevent overlapping
                Draw.z(Layer.blockOver - 0.02f)
                Lines.line(bridgeOutlineRegion, other.x, other.y, x, y, false)
            }

            linked2.forEach { other ->
                if(other is GridNodeBuild && other.id() >= id) return@forEach //prevent overlapping
                val a = angleTo(other)
                Draw.z(Layer.blockOver - 0.01f)

                if(a < 45f || a >= 225f) Lines.line(bridgeRegion, x, y, other.x, other.y,false)
                else Lines.line(bridgeRegion, other.x, other.y, x, y, false)
            }

            Draw.yscl = 1f

            Draw.z(Layer.blockOver)

            Draw.rect(topRegion, x, y)

            var text = ""
            packets.forEach {
                text += "${it.id}\n"
            }
            drawPlaceText("${graph.entity.id}\n$text", tileX(), tileY(), true)
        }
    }
}