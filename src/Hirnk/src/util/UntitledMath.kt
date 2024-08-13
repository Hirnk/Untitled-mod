package Hirnk.src.util

import arc.math.Mathf
import arc.math.geom.Vec2

object UntitledMath {


    fun Vec2.ang() = Mathf.atan2(x, y) * Mathf.radiansToDegrees
}