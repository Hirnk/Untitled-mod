package Hirnk.src.content

import arc.func.Prov
import mindustry.gen.UnitEntity
import mindustry.graphics.Layer
import mindustry.type.UnitType
import mindustry.type.Weapon

object UntitledUnitTypes {
    lateinit var prospector: UnitType

    fun load() {
        prospector = UnitType("prospector").apply {
            flying = true
            drag = 0.03f
            accel = 0.07f
            speed = 1.8f
            health = 680f
            armor = 4f
            hitSize = 25f
            isEnemy = false
            engineSize = 3.2f
            engineOffset = 40f / 4f
            itemCapacity = 80
            trailLength = 12
            trailScl = 0.75f
            lowAltitude = true
            engineLayer = Layer.flyingUnit - 0.01f

            setEnginesMirror(
                UnitType.UnitEngine(32f / 4f, -32f / 4f, 2.4f, 280f)
            )

            constructor = Prov { UnitEntity.create() }

            weapons.add(
                Weapon("untitled-prospector-beam").apply {
                    x = 0f
                    mirror = false
                }
            )
        }
    }
}