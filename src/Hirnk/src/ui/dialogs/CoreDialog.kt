package Hirnk.src.ui.dialogs

import Hirnk.src.ui.UntitledUI
import arc.scene.event.Touchable
import arc.scene.ui.ImageButton
import arc.scene.ui.layout.Table
import mindustry.Vars.control
import mindustry.Vars.ui
import mindustry.content.Blocks
import mindustry.gen.Tex
import mindustry.ui.dialogs.BaseDialog

class CoreDialog(title: String = "Core") : BaseDialog(title) {
    val deployable = arrayListOf(Blocks.router, Blocks.duo)
    init {
        val t = Table { table ->
            deployable.forEach {
                val blob = ImageButton(it.uiIcon)
                blob.touchable { Touchable.enabled }
                blob.background(Tex.button)
                blob.setSize(40f)
                blob.clicked {
                    hide()
                    ui.hudfrag.shown = false
                    control.input.block = null
                    UntitledUI.deploymentFrag.deployment = it
                    UntitledUI.deploymentFrag.visible = true
                }

                table.add(blob)
            }
            table.fill()
        }
        cont.add(t)
        addCloseButton()
    }
}