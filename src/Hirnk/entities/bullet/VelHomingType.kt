package Hirnk.entities.bullet

import arc.util.Time
import arc.util.Tmp
import mindustry.entities.Units
import mindustry.entities.bullet.BasicBulletType
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.gen.Teamc
import mindustry.gen.Unit

class VelHomingType : BasicBulletType() {
    var homingPower2 = 0.2f
    var range2 = 120f

    override fun updateHoming(b: Bullet) {
        super.updateHoming(b)
        if (b.time < b.type.homingDelay) return
        val t = target(b) ?: return
        b.vel.add(Tmp.v2.trns(b.angleTo(t), homingPower2 * Time.delta)).limit(b.type.speed * Time.delta)
    }

    fun target(b: Bullet): Teamc? {

        //from BulletType.java
        return if (heals()) {
            Units.closestTarget(null, b.x, b.y, range2,
                { e: Unit -> e.checkTarget(collidesAir, collidesGround) && e.team != b.team && !b.hasCollided(e.id) },
                { t: Building -> collidesGround && (t.team !== b.team || t.damaged()) && !b.hasCollided(t.id) }
            )
        } else {
            if (
                b.aimTile != null
                && b.aimTile.build != null
                && b.aimTile.build.team != b.team
                && collidesGround &&
                !b.hasCollided(b.aimTile.build.id)
            ) {
                b.aimTile.build
            } else {
                Units.closestTarget(b.team, b.x, b.y, range2,
                    { e: Unit? -> e != null && e.checkTarget(collidesAir, collidesGround) && !b.hasCollided(e.id) },
                    { t: Building? -> t != null && collidesGround && !b.hasCollided(t.id) })
            }
        }
    }
}