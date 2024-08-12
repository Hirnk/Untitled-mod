package Hirnk.src.world.block.transportation.grid

import Hirnk.src.content.UntitledMisc
import arc.Core
import arc.func.Prov
import arc.graphics.g2d.Draw
import mindustry.gen.Building
import mindustry.graphics.Layer
import mindustry.type.Item
import plumy.core.assets.TR
import plumy.dsl.castBuild
import plumy.dsl.config

class GridPort(name: String) : GridBlock(name) {
    var packet = UntitledMisc.basicPacket
    var speed = 20f

    lateinit var topRegion: TR

    init {
        configurable = true
        drawArrow = false
        saveConfig = true
        destructible = true
        update = true
        hasItems = true
        acceptsItems = true

        buildType = Prov { GridPortBuild() }

        config<GridPortBuild, Int> {
            val target = it.castBuild<GridPortBuild>() ?: return@config

            destination = if (target != this && target != destination && target.graph == graph) target else null
        }
    }

    override fun load() {
        super.load()
        topRegion = Core.atlas.find("$name-top")
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

            dump()

            val p = getPath() ?: return
            if (!items.any()) return

            if (reload >= 1f) {
                reload %= 1f
                val i = items.first()
                val amt = items.get(i).coerceAtMost(packet.size)
                items.remove(i, amt)
                packet.create(p, x, y, i, amt)
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

        override fun acceptItem(source: Building, item: Item): Boolean {
            return items.get(item) < getMaximumAccepted(item)
        }

        override fun draw() {
            super.draw()
            Draw.z(Layer.blockOver + 0.02f)
            Draw.rect(topRegion, x, y)
        }
    }
}