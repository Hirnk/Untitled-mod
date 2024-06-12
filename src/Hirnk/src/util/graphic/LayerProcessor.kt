package Hirnk.src.util.graphic

import arc.graphics.Color
import plumy.texture.ILayerProcessor
import plumy.texture.ILayerView
import plumy.texture.Pixel

//Shift the colour of the layer in HSV
class HSVLayerProcessor (val h: Int = 0, val s: Int = 0, val v: Int = 0) : ILayerProcessor {
    override fun process(original: ILayerView): ILayerView {
        val width = original.width
        val height = original.height
        for (x in 0 until width) {
            for (y in 0 until height) {
                val c = Pixel(original[x, y])
                if (c.isVisible) {
                    val hsv = Color.RGBtoHSV(c.rf, c.gf, c.bf)
                    hsv[0] += h
                    hsv[1] += s
                    hsv[2] += v
                    original[x, y] = Color.HSVtoRGB(hsv[0].toFloat(), hsv[1].toFloat(), hsv[2].toFloat()).rgba8888()
                }
            }
        }
        return original
    }
}