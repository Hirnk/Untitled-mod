package Hirnk.src.util

import arc.Core
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf

fun String.sheet(
    sliceWidth: Int,
    sliceHeight: Int,
    size: Int = 32
) = Core.atlas.find(this).sheet(sliceWidth, sliceHeight, size)

fun TextureRegion.sheet(
    tileWidth: Int,
    tileHeight: Int,
    size: Int = 32
): Array<TextureRegion> {
    val regions = tileWidth * tileHeight

    val tw = (u2 - u) / tileWidth
    val th = (v2 - v) / tileHeight

    return Array(regions) {
        val r = TextureRegion(this)

        val x = (it % tileWidth).toFloat() / tileWidth
        val y = (it / tileWidth).toFloat() / tileHeight

        //prevent lines
        r.u = Mathf.map(x, u, u2) + tw * 0.01f
        r.v = Mathf.map(y, v, v2) + th * 0.01f

        r.u2 = r.u + tw * 0.98f
        r.v2 = r.v + th * 0.98f
        r.width = size
        r.height = size

        return@Array r
    }
}