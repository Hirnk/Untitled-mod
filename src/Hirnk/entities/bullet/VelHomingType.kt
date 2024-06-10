package Hirnk.entities.bullet

import arc.math.geom.Position
import arc.util.Time
import arc.util.Tmp
import mindustry.entities.Units
import mindustry.entities.bullet.BasicBulletType
import mindustry.gen.Building
import mindustry.gen.Bullet
import mindustry.gen.Unit

open class VelHomingType : BasicBulletType() {
    var homingPower2 = 0.3f
    var range2 = 200f

    override fun updateHoming(b: Bullet) {
        super.updateHoming(b)
        if (b.time < b.type.homingDelay) return
        val t = target(b) ?: return
        b.vel.add(Tmp.v2.set(t).sub(b).setLength(homingPower2 * Time.delta * (b.dst(t) / range2))).limit(b.type.speed * Time.delta)
    }

    open fun target(b: Bullet): Position? {
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
                    { t: Building? -> t != null && collidesGround && !b.hasCollided(t.id) }
                )
            }
        }
    }
}