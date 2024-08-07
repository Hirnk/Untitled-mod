package Hirnk.src.entities.special

import arc.graphics.g2d.Draw
import arc.math.Interp
import mindustry.graphics.Layer
import mindustry.type.Item

class ItemType {
    var drag = 0.2f
    var lifetime = 360f
    var phasingTime = 30f
    var speed = 4f
    var layer = Layer.blockOver + 0.01f
    var interp = Interp.pow10In

    fun update(i: ItemEntity) {
    }

    fun draw(i: ItemEntity) {
        Draw.z(layer)
        Draw.scl(i.fout(interp))
        Draw.rect(i.item.item.fullIcon, i.x, i.y)
        Draw.scl()
    }

    fun create(x: Float, y: Float, item: Item, amount: Int, angle: Float) {
        val i = ItemEntity.create()
        i.type = this
        i.drag = drag
        i.time = 0f
        i.lifetime = lifetime
        i.phasingTime = phasingTime
        i.set(x, y)
        i.item.set(item, amount)
        i.initVel(angle, speed)
        i.add()
    }
}