package Hirnk.src.content

import Hirnk.src.bullet.MiningBulletType
import arc.func.Prov
import mindustry.content.Fx
import mindustry.gen.UnitEntity
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon

object UntitledUnitTypes {
    lateinit var prospector: UnitType

    fun load() {
        prospector = UnitType("prospector").apply {
            flying = true
            drag = 0.03f
            accel = 0.08f
            speed = 1.2f
            health = 280f
            isEnemy = false
            constructor = Prov { UnitEntity.create() }

            weapons.add(Weapon().apply {
                reload = 30f
                bullet = MiningBulletType().apply {
                    trailLength = 4
                    trailColor = Pal.bulletYellowBack
                    backColor = Pal.bulletYellowBack
                    speed = 3f
                    width = 8f
                    hitEffect = Fx.blastExplosion
                    hitShake = 2f
                    despawnEffect = Fx.hitLancer
                    lifetime = 60f
                    homingPower2 = 3f
                    damage = 45f
                    homingRange = 400f
                }
            })
        }
    }
}