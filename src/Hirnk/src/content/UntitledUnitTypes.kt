package Hirnk.src.content

import Hirnk.src.entities.bullet.PointMinerBulletType
import arc.func.Prov
import arc.graphics.Color
import arc.util.Time
import mindustry.content.Fx
import mindustry.content.StatusEffects
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType
import mindustry.entities.effect.MultiEffect
import mindustry.entities.part.DrawPart
import mindustry.entities.part.RegionPart
import mindustry.gen.MechUnit
import mindustry.gen.Sounds
import mindustry.gen.UnitEntity
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.type.weapons.PointDefenseWeapon

object UntitledUnitTypes {
    lateinit var prospector: UnitType
    lateinit var dredger: UnitType
    lateinit var cogwheel: UnitType

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

        cogwheel = UnitType("cogwheel").apply {
            hitSize = 14f
            speed = 0.7f
            rotateSpeed = 3f
            health = 870f
            armor = 2f
            itemCapacity = 5

            outlineColor = Color.valueOf("433c4c")

            constructor = Prov { MechUnit.create() }

            parts.add(RegionPart("-cog").apply {
                moveRot = 360f
                progress = DrawPart.PartProgress { p -> ((Time.time / 180f) % 1f * (p.warmup + 1f)) % 1f }
                y = -2f
                layerOffset = -0.01f
            })

            weapons.add(Weapon("untitled-mod-cogwheel-gun").apply {
                reload = 40f
                ejectEffect = Fx.casing2
                shootSound = Sounds.shootBig
                shootWarmupSpeed = 0.04f
                soundPitchMax = 1.3f
                top = false
                recoil = 0.4f
                x = 9.5f
                y = 3.75f

                shoot.apply {
                    shots = 3
                    shotDelay = 5f
                }

                bullet = BasicBulletType(5.2f, 25f).apply {
                    width = 6f
                    height = 9f
                    lifetime = 60f
                    trailColor = Pal.bulletYellowBack
                    trailLength = 5
                    trailScl = 0.3f
                    recoil = 1f
                }
            })
        }
    }
}