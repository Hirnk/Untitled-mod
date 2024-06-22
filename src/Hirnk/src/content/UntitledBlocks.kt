package Hirnk.src.content

import Hirnk.src.entities.bullet.VelHomingType
import Hirnk.src.world.block.crafter.MultiCrafter
import Hirnk.src.world.block.crafter.recipe.Recipe
import Hirnk.src.world.block.crafter.recipe.RecipeProgress
import Hirnk.src.world.block.storage.UntitledCore
import Hirnk.src.world.block.transportation.GridNode
import Hirnk.src.world.block.transportation.GridPort
import mindustry.content.*
import mindustry.entities.effect.MultiEffect
import mindustry.entities.effect.WaveEffect
import mindustry.entities.pattern.ShootSpread
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.blocks.defense.turrets.PowerTurret
import mindustry.world.blocks.environment.SteamVent
import mindustry.world.blocks.liquid.LiquidRouter
import mindustry.world.consumers.ConsumeItems
import mindustry.world.meta.Attribute

object UntitledBlocks {
    //turret
    lateinit var concentrate: Block
    //transportation
    lateinit var itemNode: Block
    lateinit var itemPort: Block
    //fluid
    lateinit var copperTank: Block
    lateinit var copperTankBig: Block
    //crafter
    lateinit var furnace: Block
    //core
    lateinit var coreShelter: Block
    //env
    lateinit var vent: Block

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
                    backColor = Pal.lancerLaser
                    homingDelay = 20f
                    speed = 4f
                    width = 8f
                    hitEffect = Fx.hitLancer
                    despawnEffect = Fx.hitLancer
                    lifetime = 60f
                    homingPower2 = 1.5f
                    status = StatusEffects.shocked
                    damage = 15f
                    homingRange = 400f
                }
            }
            limitRange(8f)
        }

        itemNode = GridNode("grid-node").apply {
            requirements(Category.distribution, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 550
            armor = 2f
            size = 2
        }

        itemPort = GridPort("grid-port").apply {
            requirements(Category.distribution, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 800
            armor = 2f
            size = 3
        }

        copperTank = LiquidRouter("copper-tank-2").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 550
            armor = 2f
            size = 2
            liquidCapacity = 500f
            squareSprite = false
            liquidPadding = 3f
        }

        copperTankBig = LiquidRouter("copper-tank-4").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45*4, Items.lead, 20*4))
            health = 3500
            armor = 6f
            size = 4
            liquidCapacity = 2200f
            squareSprite = false
            liquidPadding = 8f
        }

        furnace = MultiCrafter("furnace").apply {
            requirements(Category.crafting, ItemStack.with(Items.copper, 80, Items.lead, 20))
            health = 750
            size = 3
            recipes.add(
                Recipe().apply {
                    progresses += RecipeProgress().apply {
                        consumers.add(ConsumeItems(ItemStack.with(Items.copper, 1)))
                        outputItems.add(ItemStack(Items.tungsten, 2))
                    }
                    progresses += RecipeProgress().apply {
                        consumers.add(ConsumeItems(ItemStack.with(Items.coal, 1)))
                        outputItems.add(ItemStack(Items.graphite, 3))
                        craftTime = 80f
                        craftEffect = Fx.smokePuff
                    }
                }
            )
        }

        coreShelter = UntitledCore("core-shelter").apply {
            requirements(Category.effect, ItemStack.with(Items.copper, 300))
            isFirstTier = true
            unitType = UnitTypes.gamma
            health = 8000
            itemCapacity = 800
            size = 3
            armor = 3f
            alwaysUnlocked = true
            incinerateNonBuildable = true
            requiresCoreZone = true
            buildCostMultiplier = 0.5f
            unitCapModifier = 4
            researchCostMultiplier = 0.05f
        }

        vent = SteamVent("vent").apply {
            parent = Blocks.basalt
            blendGroup = Blocks.basalt
            attributes.set(Attribute.steam, 1f)
        }
    }
}