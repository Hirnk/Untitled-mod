package Hirnk.entities.bullet

import arc.math.geom.Position
import mindustry.Vars.tilesize
import mindustry.gen.Bullet
import mindustry.gen.Call
import mindustry.gen.Itemsc
import mindustry.graphics.Drawf
import mindustry.world.Tile

//todo
class MiningBulletType : VelHomingType() {
    override fun init(b: Bullet) {
        super.init(b)
        b.data = b.aimTile
    }

    override fun target(b: Bullet): Position {
        return b.data as Position
    }

    override fun draw(b: Bullet) {
        super.draw(b)
        (b.data as Position).run {
            Drawf.dashSquare(b.team.color, x, y, 8f)
        }
    }

    override fun despawned(b: Bullet) {
        (b.data as Tile).run {
            val i = overlay().itemDrop
                if (i != null) Call.transferItemToUnit(i, x.toFloat() * tilesize, y.toFloat() * tilesize, (b.owner as Itemsc))
            super.despawned(b)
        }
    }

    override fun update(b: Bullet) {
        super.update(b)
    }
}