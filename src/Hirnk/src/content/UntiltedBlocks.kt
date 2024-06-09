package Hirnk.src.content

import Hirnk.entities.bullet.VelHomingType
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.content.StatusEffects
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.effect.MultiEffect
import mindustry.entities.effect.WaveEffect
import mindustry.entities.pattern.ShootSpread
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.blocks.defense.turrets.PowerTurret
import mindustry.world.blocks.liquid.LiquidRouter

object UntiltedBlocks {
    //turret
    lateinit var concentrate: Block
    //fluid
    lateinit var copperTank: Block
    lateinit var copperTankBig: Block

    fun load() {
        concentrate = PowerTurret("concentrate").apply {
            requirements(Category.turret, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 950
            armor = 4f
            size = 3
            rotateSpeed = 3f

            shootSound = Sounds.shockBlast
            reload = 100f
            range = 240f

            shoot = ShootSpread().apply {
                shots = 2
                spread = 30f
            }

            shootType = VelHomingType().apply {
                sprite = "large-orb"
                speed = 3f
                damage = 30f
                lifetime = 100f
                width = 15f
                height = 15f
                hitSize = 7f
                pierce = true
                pierceCap = 6
                shake = 2f
                backColor = Pal.lancerLaser
                trailEffect = Fx.missileTrail
                trailInterval = 3f
                trailColor = Pal.lancerLaser
                trailParam = 4f
                shrinkX = 0f
                shrinkY = 0f
                consumePower(12f)

                shootEffect = MultiEffect(Fx.shootTitan, WaveEffect().apply {
                    colorTo = Pal.lancerLaser
                    sizeTo = 26f
                    lifetime = 14f
                    strokeFrom = 4f
                })

                intervalBullets = 3
                intervalSpread = 120f
                intervalRandomSpread = 0f
                hitEffect = Fx.hitLancer
                despawnEffect = Fx.hitLancer
                status = StatusEffects.shocked

                intervalBullet = VelHomingType().apply {
                    trailLength = 4
                    trailColor = Pal.lancerLaser
                    speed = 4f
                    backColor = Pal.lancerLaser
                    homingDelay = 20f
                    speed = 4f
                    width = 8f
                    hitEffect = Fx.hitLancer
                    despawnEffect = Fx.hitLancer
                    lifetime = 60f
                    homingPower2 = 0.4f
                    status = StatusEffects.shocked
                    damage = 15f
                    homingRange = 400f
                }
            }
            limitRange(8f)
        }

        copperTank = LiquidRouter("copper-tank-2").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 550
            armor = 2f
            size = 2
            liquidCapacity = 500f
            squareSprite = false
        }

        copperTankBig = LiquidRouter("copper-tank-4").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45*4, Items.lead, 20*4))
            health = 3500
            armor = 6f
            size = 4
            liquidCapacity = 2200f
            squareSprite = false

            consumeLiquid(Liquids.water, 0.5f)
        }
    }
}