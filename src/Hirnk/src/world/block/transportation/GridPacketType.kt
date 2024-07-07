package Hirnk.src.world.block.transportation

import Hirnk.src.entities.special.GridPacket
import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import arc.util.Time
import mindustry.content.Fx
import mindustry.ctype.ContentType
import mindustry.ctype.UnlockableContent
import mindustry.graphics.Layer
import mindustry.type.Item
import plumy.core.assets.TR

class GridPacketType(name: String) : UnlockableContent(name) {
    var speed = 1f
    var layer = Layer.blockOver
    var size = 2
    var derailFx = Fx.blockExplosionSmoke
    var derailShake = 1f

    lateinit var region: TR

    fun update(p: GridPacket) {
        p.run {
            routine?.let {
                if (node < it.size - 1) {

                    val current = it[node]
                    val next = it[node + 1]

                    p.progress += speed * Time.delta / current.dst(next)

                    if (progress >= 1f) {
                        onRailUpdate()
                        p.progress %= 1f
                        node += 1
                    }

                    set(
                        Mathf.map(p.progress, current.x, next.x),
                        Mathf.map(p.progress, current.y, next.y)
                    )
                } else {
                    deposit(p)
                    remove()
                }
            }
        }
    }

    fun onRailUpdate() {

    }

    fun draw(p: GridPacket) {
        Draw.rect(region, p.x, p.y)
        if (p.item.amount != 0) Draw.rect(p.item.item.fullIcon, p.x, p.y)
    }

    //what to do when it reached its destination
    fun deposit(p: GridPacket) {
        val destination = p.routine?.last() ?: return
        destination.items().add(p.item.item, p.item.amount)
    }

    fun create(routine: Path, x: Float, y: Float, item: Item, amount: Int) {
        val packet = GridPacket.create()
        packet.routine = routine
        packet.type = this
        packet.set(x, y)
        packet.item.set(item, amount)
        packet.add()
    }

    override fun load() {
        region = Core.atlas.find(name)
    }

    override fun getContentType(): ContentType = ContentType.bullet
}