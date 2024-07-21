package Hirnk.src.world.block.transportation

import Hirnk.src.entities.special.GridPacket
import Hirnk.src.world.block.transportation.EmptyNode.isConnected
import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import arc.util.Time
import mindustry.content.Fx
import mindustry.ctype.ContentType
import mindustry.ctype.UnlockableContent
import mindustry.entities.Effect
import mindustry.graphics.Layer
import mindustry.type.Item
import plumy.core.assets.TR

class GridPacketType(name: String) : UnlockableContent(name) {
    var speed = 1.5f
    var layer = Layer.blockOver
    var size = 2
    var derailFx = Fx.mineHuge
    var derailShake = 3f

    lateinit var region: TR

    fun update(p: GridPacket) {
        p.run {
            routine.let {
                val current = it[0]
                val next = it[1]

                p.progress += speed * Time.delta / current.dst(next)

                if (progress >= 1f) {
                    p.progress %= 1f
                    p.routine.removeFirst()

                    if (current is GridNode.GridNodeBuild) {
                        current.packets.remove(p)
                    }

                    if (routine.size <= 1) {
                        deposit(p)
                        remove()
                        return
                    }

                    if (next is GridNode.GridNodeBuild) {
                        next.packets.add(p)
                    }

                    val next1 = it[1]

                    if (!next.isConnected(next1)) return derail(p, next)
                }

                set(
                    Mathf.map(p.progress, current.x, next.x),
                    Mathf.map(p.progress, current.y, next.y)
                )
            }
        }
    }

    fun derail(packet: GridPacket, last: IGridNode) {
        last.let {
            if (it is GridNode.GridNodeBuild) {
                it.packets.remove(packet)
            } else return@let
        }
        derailFx.at(packet.x, packet.y)
        Effect.shake(derailShake, derailShake, packet.x, packet.y)
        packet.remove()
    }

    fun draw(p: GridPacket) {
        Draw.rect(region, p.x, p.y)
        if (p.item.amount != 0) Draw.rect(p.item.item.fullIcon, p.x, p.y)
    }

    //what to do when it reached its destination
    fun deposit(p: GridPacket) {
        val destination = p.routine.last()
        destination.items().add(p.item.item, p.item.amount)
    }

    fun create(routine: Path, x: Float, y: Float, item: Item, amount: Int) {
        val packet = GridPacket.create()
        packet.routine.addAll(routine)
        packet.type = this
        packet.set(x, y)
        packet.item.set(item, amount)
        packet.add()
    }

    override fun load() {
        region = Core.atlas.find(name)
    }

    override fun getContentType(): ContentType = ContentType.bullet

    companion object {
        fun checkValid(p: GridPacket) {
            p.routine
        }
    }
}