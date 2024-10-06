package Hirnk.src.content

import Hirnk.src.entities.bullet.VelHomingType
import Hirnk.src.world.block.crafter.MultiCrafter
import Hirnk.src.world.block.crafter.recipe.Recipe
import Hirnk.src.world.block.crafter.recipe.RecipeProgress
import Hirnk.src.world.block.distribution.fluid.GasPipe
import Hirnk.src.world.block.environment.PulseCrystal
import Hirnk.src.world.block.storage.UntitledCore
import Hirnk.src.world.block.distribution.grid.GridNode
import Hirnk.src.world.block.distribution.grid.GridPort
import Hirnk.src.world.block.distribution.mecharm.Arm
import Hirnk.src.world.block.distribution.mecharm.MechanicalArm
import Hirnk.src.world.draw.DrawRod
import Hirnk.src.world.draw.DrawPane
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
import mindustry.world.blocks.defense.turrets.Turret
import mindustry.world.blocks.environment.SteamVent
import mindustry.world.blocks.liquid.LiquidRouter
import mindustry.world.consumers.ConsumeItems
import mindustry.world.draw.DrawDefault
import mindustry.world.draw.DrawRegion
import mindustry.world.meta.Attribute
import plumy.dsl.DrawMulti

object UntitledBlocks {
    //turret
    lateinit var concentrate: Block
    lateinit var rod: Block
    //transportation
    lateinit var transporter: Block
    lateinit var itemNode: Block
    lateinit var itemPort: Block
    //fluid
    lateinit var copperPipe: Block
    lateinit var copperTank: Block
    lateinit var copperTankBig: Block
    //crafter
    lateinit var centrifuge: Block
    lateinit var refinery: Block
    //core
    lateinit var coreShelter: Block
    //env
    lateinit var vent: Block
    lateinit var duitiumCluster: Block

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

        rod = Turret("rod").apply {
            requirements(Category.turret, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 950
            armor = 4f
            size = 2
            customShadow = true
            outlineIcon = false

            drawer = DrawMulti {
                +DrawDefault()
                +DrawRod()
            }
        }

        transporter = MechanicalArm("transporter").apply {
            requirements(Category.distribution, ItemStack.with(Items.copper, 45, Items.lead, 20))
            health = 240
            armor = 5.5f
            size = 1

            arm = Arm().apply {
                joints = 2
                offset = arrayOf(0f, 20f, 40f, 60f)
            }
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
            itemCapacity = 80
        }

        copperPipe = GasPipe("gas-pipe").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 20, Items.lead, 20))
            health = 220
            size = 1
            liquidCapacity = 20f
            squareSprite = false
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

        centrifuge = MultiCrafter("centrifuge").apply {
            requirements(Category.crafting, ItemStack.with(Items.copper, 80, Items.lead, 20))
            health = 750
            size = 3
            warmupSpeed = 0.002f
            recipes.add(
                Recipe().apply {
                    progresses += RecipeProgress().apply {
                        consumers.add(ConsumeItems(ItemStack.with(Items.copper, 1)))
                        outputItems.add(ItemStack(UntitledItems.recycledScrap, 2))
                    }
                }
            )
            drawer = DrawMulti {
                +DrawRegion("-bottom")
                +DrawPane()
                +DrawDefault()
            }
        }

        refinery = MultiCrafter("refinery").apply {
            requirements(Category.liquid, ItemStack.with(Items.copper, 45*4, Items.lead, 20*4))
            health = 3200
            armor = 6f
            size = 4
            squareSprite = false
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

        duitiumCluster = PulseCrystal("duitium-crystal").apply {
            variants = 2
        }
    }
}