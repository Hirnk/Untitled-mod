package Hirnk.src.content

import Hirnk.src.entities.bullet.PointMinerBulletType
import Hirnk.src.entities.parts.StaticPart
import arc.func.Prov
import mindustry.content.Fx
import mindustry.content.StatusEffects
import mindustry.entities.bullet.BulletType
import mindustry.entities.effect.MultiEffect
import mindustry.gen.Sounds
import mindustry.gen.UnitEntity
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.type.weapons.PointDefenseWeapon

object UntitledUnitTypes {
    lateinit var prospector: UnitType
    lateinit var dredger: UnitType

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
            range = 160f

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
                    shootSound = Sounds.blaster

                    bullet = PointMinerBulletType().apply {
                        lifetime = 20f
                        parentizeEffects = false
                        hitEffect = Fx.hitLancer
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
                        length = 155f
                    }
                },
                PointDefenseWeapon("untitled-mod-prospector-point-defense").apply {
                    x = 9f
                    y = -1f
                    reload = 8f
                    targetInterval = 9f
                    targetSwitchInterval = 12f
                    recoil = 0.5f
                    bullet = BulletType().apply {
                        shootSound = Sounds.lasershoot
                        shootEffect = Fx.sparkShoot
                        hitEffect = Fx.pointHit
                        maxRange = 100f
                        damage = 20f
                    }
                }
            )
        }
        dredger = UnitType("dredger").apply {
            flying = true
            rotateSpeed = 2.5f
            drag = 0.02f
            accel = 0.01f
            speed = 2.4f
            health = 1920f
            armor = 8f
            hitSize = 30f
            engineSize = 0f
            itemCapacity = 180
            outlineColor = Pal.darkOutline

            setEnginesMirror(
                UnitType.UnitEngine(16f / 4f, -58f / 4f, 3f, 270f)
            )

            constructor = Prov { UnitEntity.create() }
        }
    }
}