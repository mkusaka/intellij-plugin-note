package com.github.mkusaka.intellijpluginnote.toolWindow

import com.github.mkusaka.intellijpluginnote.toolWindow.components.ChatPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

/**
 * チャットUIを含むツールウィンドウのファクトリ
 */
class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val chatToolWindow = ChatToolWindow(project, toolWindow)
        chatToolWindow.initToolWindow()
    }

    override fun shouldBeAvailable(project: Project) = true

    /**
     * チャットUIを含むツールウィンドウ
     */
    class ChatToolWindow(private val project: Project, private val toolWindow: ToolWindow) {
        
        /**
         * ツールウィンドウを初期化します
         */
        fun initToolWindow() {
            val chatPanel = ChatPanel(project)
            val content = ContentFactory.getInstance().createContent(chatPanel, "チャット", false)
            toolWindow.contentManager.addContent(content)
        }
    }
}
