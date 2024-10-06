package Hirnk.src.world.block.distribution.mecharm

import Hirnk.src.util.UntitledMath.ang
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
import mindustry.type.ItemStack
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
        = other.block.hasItems && dst(other) <= range()

    inner class MechanicalArmBuild : Building() {
        var inputMode = true
        var progress = 0f
        var payload = ItemStack()

        var indexIn = 0
        var indexOut = 0
        var carrying = false

        val mover by lazy { Pair(
            Array(arm.joints + 2) { Vec2(arm.offset[it] + x, y) },
            FloatArray(arm.joints + 1) { 0f }
        )}

        var inputs = ArrayList<Building>()
        var outputs = ArrayList<Building>()
        var positions = HashMap<Int, Pair<Array<Vec2>, FloatArray>>()

        var offsetSpeed = FloatArray(arm.joints + 1) { 0f }

        override fun created() {
            obtainTarget()
        }

        override fun updateTile() {
            progress += getProgressIncrease(payloadTime)

            if (progress <= 1f) {
                for (i in 0 until arm.joints + 1) {
                    val vert = mover.first

                    vert[i + 1].trns(
                        offsetSpeed[i] * progress,
                        arm.offset[i + 1] - arm.offset[i]
                    ).add(vert[i])
                }
            } else {
                progress -= 1

                obtainTarget() ?: return
            }
        }

        fun obtainTarget(): Building? {
            var target: Building? = null

            if (!carrying && inputs.size > 0) {
                indexIn = (indexIn + 1) % inputs.size
                target = inputs[indexIn]
                setOffset(target)
                carrying = false
            } else if (outputs.size > 0) {
                indexOut = (indexOut + 1) % outputs.size
                target = outputs[indexOut]
                setOffset(target)
                carrying = true
            } else {
                offsetSpeed.forEachIndexed { i, _ -> offsetSpeed[i] = 0f }
            }
            return target
        }

        fun setOffset(other: Building) {
            for (i in offsetSpeed.indices) {
                offsetSpeed[i] = (positions[other.pos()]!!.second[i] - mover.second[i])
            }
        }

        fun registerPoint(input: Boolean, building: Building) {
            if (input) inputs.add(building) else outputs.add(building)

            Tmp.v1.set(building).sub(this).normal(if (input) 1f else -1f).nor()

            val pos = Array<Vec2>(arm.joints + 2) { i ->
                Tmp.v3.set(Tmp.v1).scl(arm.offset[i])
                return@Array Vec2().set(this).add(Tmp.v3)
            }

            pos.solve(Tmp.v1.set(building))

            Tmp.v2.set(1f, 0f)

            val angles = FloatArray(arm.joints + 1) { i ->
                Tmp.v1.set(pos[i + 1]).sub(pos[i]).ang()
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

            Lines.beginLine()
            mover.first.forEach {
                Lines.linePoint(it)
            }
            Lines.endLine()

            Draw.color(Pal.slagOrange)

            if (positions.size == 0) return

            positions.forEach { (_, v) ->
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