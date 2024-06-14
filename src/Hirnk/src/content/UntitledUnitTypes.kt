package Hirnk.src.content

import Hirnk.src.entities.bullet.PointMinerBulletType
import Hirnk.src.entities.parts.StaticPart
import arc.func.Prov
import mindustry.content.Fx
import mindustry.content.StatusEffects
import mindustry.entities.effect.MultiEffect
import mindustry.gen.Sounds
import mindustry.gen.UnitEntity
import mindustry.type.UnitType
import mindustry.type.Weapon

object UntitledUnitTypes {
    lateinit var prospector: UnitType

    fun load() {
        prospector = UnitType("prospector").apply {
            flying = true
            rotateSpeed = 3f
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

            setEnginesMirror(
                UnitType.UnitEngine(32f / 4f, -32f / 4f, 2.4f, 280f)
            )

            constructor = Prov { UnitEntity.create() }

            weapons.add(
                Weapon("untitled-mod-prospector-beam").apply {
                    x = 0f
                    mirror = false
                    reload = 80f
                    cooldownTime = 60f
                    parts.add(StaticPart())
                    range = 110f
                    shootSound = Sounds.blaster

                    bullet = PointMinerBulletType().apply {
                        lifetime = 20f
                        parentizeEffects = false
                        shootEffect = MultiEffect(
                            UntitledFx.mineLaserShoot,
                            Fx.shootSmokeSquareSparse
                        )
                        mineEffect = MultiEffect(
                            UntitledFx.mineOre,
                            Fx.mineBig
                        )
                        damage = 50f
                        status = StatusEffects.shocked
                    }
                }
            )
        }
    }
}