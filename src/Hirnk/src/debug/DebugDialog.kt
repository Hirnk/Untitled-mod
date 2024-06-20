package Hirnk.src.debug

import Hirnk.src.gen.OreItemGen
import arc.scene.ui.Label
import arc.scene.ui.layout.Table
import mindustry.ui.dialogs.BaseDialog

object DebugDialog {
    fun oreGenerator() {
        BaseDialog("Debug Icon Generating").apply {
            val icons = Table()
            fun rebuild() {
                icons.clear()
                OreItemGen.list.forEachIndexed { i, it ->
                    icons.add(Table().apply {
                        image(it.uiIcon).size(100f).row()
                        image(OreItemGen.rawOre[it].uiIcon).size(100f).row()
                        image(OreItemGen.orePulver[it].uiIcon).size(100f).row()
                    }).minSize(120f).pad(10f)
                    if (i != 0 && i % 5 == 4) {
                        icons.row()
                    }
                }
            }
            rebuild()
            cont.add(icons).grow().row()
            val alpha = Label("Tint: ${OreItemGen.OreIconGenerator.alpha}")
            val saturation = Label("Saturation: ${OreItemGen.OreIconGenerator.saturation}")
            val brightness = Label("Brightness: ${OreItemGen.OreIconGenerator.brightness}")
            cont.add(alpha).row()
            cont.add(saturation).row()
            cont.add(brightness).row()
            fun reload() {
                OreItemGen.all.forEach {
                    it.loadIcon()
                }
                rebuild()
            }
            cont.slider(0f, 1f, 0.0001f, OreItemGen.OreIconGenerator.alpha) {
                OreItemGen.OreIconGenerator.alpha = it
                alpha.setText("$it")
                reload()
            }.width(1000f).row()
            cont.slider(0f, 100f, 1f, OreItemGen.OreIconGenerator.saturation.toFloat()) {
                OreItemGen.OreIconGenerator.saturation = it.toInt()
                saturation.setText("$it")
                reload()
            }.width(1000f).row()
            cont.slider(0f, 100f, 1f, OreItemGen.OreIconGenerator.brightness.toFloat()) {
                OreItemGen.OreIconGenerator.brightness = it.toInt()
                brightness.setText("$it")
                reload()
            }.width(1000f).row()

            addCloseButton()

            buttons.button("Reload") {
                reload()
            }
        }.show()
    }
    fun researchLab() {
        BaseDialog("Research").apply {

        }
    }
}