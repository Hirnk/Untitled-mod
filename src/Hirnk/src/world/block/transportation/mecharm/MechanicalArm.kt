package Hirnk.src.world.block.transportation.mecharm

import Hirnk.src.util.ik.IKSolver.solve
import arc.func.Prov
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import arc.math.geom.Vec2
import arc.util.Tmp
import mindustry.Vars
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.world.Block
import mindustry.world.meta.BlockGroup
import plumy.core.math.normal
import plumy.dsl.castBuild
import plumy.dsl.config

class MechanicalArm(name: String) : Block(name) {
    var payloadAmount = 8
    var payloadTime = 60f
    var arm: Arm = EmptyArm

    fun range(): Float {
        var t = 0f
        for (i in 1 until arm.offset.size) {
            t += arm.offset[i] - arm.offset[i-1]
        }
        return t
    }

    init {
        solid = true
        update = true
        group = BlockGroup.transportation
        noUpdateDisabled = true
        configurable = true

        buildType = Prov { MechanicalArmBuild() }

        config<MechanicalArmBuild, Int> {
            val t = it.castBuild<Building>() ?: return@config

            if (!validConnect(t)) return@config

            registerPoint(inputMode, t)
        }
    }

    fun MechanicalArmBuild.validConnect(other: Building)
        = dst(other) <= range()

    enum class ArmState {
        INPUT, OUTPUT, IDLE
    }

    inner class MechanicalArmBuild : Building() {
        var inputMode = true
        var progress = 0f

        var inputs = ArrayList<Building>()
        var outputs = ArrayList<Building>()
        var positions = HashMap<Int, Pair<Array<Vec2>, FloatArray>>()

        var angleMultipier = FloatArray(arm.joints + 1) { 0f }

        fun registerPoint(input: Boolean, building: Building) {
            if (input) inputs.add(building) else outputs.add(building)

            Tmp.v1.set(building).sub(this).normal(if (input) 1f else -1f).nor()

            val pos = Array<Vec2>(arm.joints + 2) { i ->
                Tmp.v3.set(Tmp.v1).scl(arm.offset[i])
                return@Array Vec2().set(this).add(Tmp.v3)
            }

            pos.solve(Tmp.v1.set(building))

            val angles = FloatArray(arm.joints + 1) { i ->
                Tmp.v1.set(pos[i]).sub(this)
                    .angle(Tmp.v2.set(pos[i + 1]).sub(this))
            }

            positions[building.pos()] = Pair(pos, angles)
        }

        override fun drawConfigure() {
            inputs.forEach { it.run {
                Drawf.select(x, y, block.size.toFloat() * 2f, Pal.lancerLaser)
            }}

            outputs.forEach { it.run {
                Drawf.select(x, y, block.size.toFloat() * 2f, Pal.slagOrange)
            }}

            Drawf.select(x, y, size.toFloat() * Vars.tilesize,
                if (inputMode) Pal.lancerLaser else Pal.slagOrange
            )

            Drawf.dashCircle(x, y, range(), team.color)
        }

        override fun onConfigureBuildTapped(other: Building): Boolean {
            if (other != this) {
                configure(other.pos())
                return false
            } else {
                inputMode = !inputMode
                return true
            }
        }

        override fun draw() {
            Lines.stroke(2f)
            Draw.color(Pal.lancerLaser)

            if (positions.size == 0) return

            positions.forEach { k, v ->
                Lines.beginLine()
                v.first.forEach { Lines.linePoint(it) }
                Lines.endLine()
            }
        }
    }
}

open class Arm {
    var joints = 0
    var offset = emptyArray<Float>()
}

object EmptyArm : Arm() {
    init {
        joints = 0
        offset = emptyArray()
    }
}