package Hirnk.src.entities.special

import Hirnk.src.content.UntitledMisc
import Hirnk.src.world.block.transportation.grid.EmptyNode.isConnected
import Hirnk.src.world.block.transportation.grid.GridNode
import Hirnk.src.world.block.transportation.grid.Path
import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import arc.util.Time
import mindustry.content.Fx
import mindustry.ctype.ContentType
import mindustry.ctype.UnlockableContent
import mindustry.entities.Effect
import mindustry.gen.Sounds
import mindustry.graphics.Layer
import mindustry.type.Item
import plumy.core.assets.TR

class GridPacketType(name: String) : UnlockableContent(name) {
    var speed = 1.5f
    var layer = Layer.blockOver + 0.02f
    var size = 2
    var derailFx = Fx.mineHuge
    var derailSFx = Sounds.boom
    var derailShake = 3.5f
    var itemType = UntitledMisc.basicItem

    lateinit var region: TR
    lateinit var outlineRegion: TR

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
                    val next1 = it[1]


                    if (next is GridNode.GridNodeBuild && next.isConnected(next1)) {
                        next.packets.add(p)
                    } else {
                        derail(p)
                        return
                    }
                }

                set(
                    Mathf.map(p.progress, current.x, next.x),
                    Mathf.map(p.progress, current.y, next.y)
                )
            }
        }
    }

    fun derail(packet: GridPacket) {
        itemType.create(packet.x, packet.y, packet.item.item, packet.item.amount, Mathf.random(360f))

        derailFx.at(packet.x, packet.y)
        derailSFx.at(packet.x, packet.y)
        Effect.shake(derailShake, derailShake, packet.x, packet.y)
        packet.remove()
    }

    fun draw(p: GridPacket) {
        Draw.z(Layer.blockOver - 0.02f)
        Draw.rect(outlineRegion, p.x, p.y)
        Draw.z(layer)
        Draw.rect(region, p.x, p.y)
        Draw.rect(p.item.item.fullIcon, p.x, p.y)
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

    fun checkValid(p: GridPacket): Boolean {
        if (p.routine.size <= 1) return true

        val current = p.routine[0]
        val next = p.routine[1]
        if (!current.isConnected(next)) {
            p.type.derail(p)
            return false
        } else return true
    }

    override fun load() {
        region = Core.atlas.find(name)
        outlineRegion = Core.atlas.find("$name-outline")
    }

    override fun getContentType(): ContentType = ContentType.bullet
}