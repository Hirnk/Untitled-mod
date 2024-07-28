package Hirnk.src.entities.bullet

import Hirnk.src.content.UntitledFx
import Hirnk.src.content.UntitledMisc
import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.math.Interp
import arc.math.Mathf
import arc.util.Tmp
import mindustry.Vars
import mindustry.Vars.tilesize
import mindustry.content.Fx
import mindustry.core.World
import mindustry.entities.Damage
import mindustry.entities.bullet.BulletType
import mindustry.gen.Bullet
import mindustry.graphics.Drawf
import plumy.core.assets.TR

class PointMinerBulletType : BulletType() {
    var color = Color.white
    var sprite = "drill-laser-boost"
    var width = 0.5f
    var interp = Interp.pow3
    var mineTier = 2
    var mineAmt = 3
    var mineEffect = UntitledFx.mineOre
    var length = 100f
    var itemType = UntitledMisc.basicItem

    lateinit var laser: TR
    lateinit var laserEnd: TR

    init {
        removeAfterPierce = false
        speed = 0f
        despawnEffect = Fx.none
        impact = true
        keepVelocity = false
        collides = false
        pierce = true
        hittable = false
        absorbable = false
    }

    override fun init() {
        super.init()
        range = length
    }

    override fun init(b: Bullet) {
        super.init(b)

        Tmp.v1.set(b.aimX, b.aimY).sub(b.x, b.y).limit(length).add(b.x, b.y)

        b.data = Tmp.v1

        b.aimX = Tmp.v1.x
        b.aimY = Tmp.v1.y
        Damage.collidePoint(b, b.team, hitEffect, b.aimX, b.aimY)

        val t = Vars.world.tile(World.toTile(b.aimX), World.toTile(b.aimY))
        if (t != null) {
            val d = t.overlay().itemDrop
            if (d != null) {
                val tx = t.x * tilesize.toFloat()
                val ty = t.y * tilesize.toFloat()
                mineEffect.at(tx, ty, b.rotation(), color, t)
                itemType.create(tx, ty, d, mineAmt, Mathf.random(360f))
            }
        }
    }

    override fun draw(b: Bullet) {
        super.draw(b)

        Draw.color(color)
        Drawf.laser(laser, laserEnd, b.x, b.y, b.aimX, b.aimY, interp.apply(b.fslope() * width))

        Draw.reset()
    }

    override fun load() {
        super.load()
        laser = Core.atlas.find(sprite)
        laserEnd = Core.atlas.find("$sprite-end")
    }
}