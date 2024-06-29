package Hirnk.src.world.block.transportation

import Hirnk.src.entities.special.GridPacket
import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import arc.util.Time
import mindustry.ctype.ContentType
import mindustry.ctype.UnlockableContent
import mindustry.graphics.Layer
import plumy.core.assets.TR

class GridPacketType(name: String) : UnlockableContent(name) {
    var speed = 1f
    var layer = Layer.blockOver

    lateinit var region: TR

    fun update(p: GridPacket) {
        val prog = p.progress.toInt()
        p.run {
            routine?.let {
                if (prog < it.size - 1) {
                    val current = it[prog]
                    val next = it[prog + 1]
                    val progress = progress % 1f

                    p.progress += speed * Time.delta / current.dst(next)
                    set(
                        Mathf.map(progress, current.x, next.x),
                        Mathf.map(progress, current.y, next.y)
                    )
                } else remove()
            }
        }
    }

    fun draw(p: GridPacket) {
        Draw.rect(region, p.x, p.y)
    }

    fun create(routine: Path, x: Float, y: Float) {
        val packet = GridPacket.create()
        packet.routine = routine
        packet.type = this
        packet.set(x, y)
        packet.add()
    }

    override fun load() {
        region = Core.atlas.find(name)
    }

    override fun getContentType(): ContentType = ContentType.bullet
}