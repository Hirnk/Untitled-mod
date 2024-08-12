package Hirnk.src.util.ik

import arc.math.geom.Vec2
import arc.util.Tmp

object IKSolver {

    //FABRIK solver...
    fun Array<Vec2>.solve(end: Vec2, minDst: Float = 0.2f, cap: Int = 10) {
        val start = Vec2(this[0])

        val length = FloatArray(this.size - 1) { i ->
            Tmp.v2.set(this[i + 1]).sub(this[i]).len()
        }

        for (i in 0 until cap) {
            this.reverse()
            length.reverse()

            val reversed = i % 2 == 0

            this[0].set(if (reversed) end else start)

            for (j in 1 until this.size) {
                val dirScl = Tmp.v2.set(this[j]).sub(this[j - 1]).nor().scl(length[j - 1])
                this[j].set(dirScl.add(this[j - 1]))
            }

            if (!reversed && Tmp.v2.set(this[this.size - 1]).sub(end).len() <= minDst) return
        }
    }
}