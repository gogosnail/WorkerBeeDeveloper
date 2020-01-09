package me.gogosnail.workerbee.box.utils

import android.view.accessibility.AccessibilityNodeInfo

object AccessibilityUtils {

    /**
     * 查找第一个[className] 的节点
     */
    fun findFirstChildByClassName(className:String, rootNode: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        rootNode?:return null
        if (rootNode.className == className) return rootNode
        for (i in 0 until rootNode.childCount) {
            val node = findFirstChildByClassName(className, rootNode.getChild(i))
            if (node != null) return node
        }
        return null
    }

    fun findNodesByText(nodeInfo: AccessibilityNodeInfo?, text: String): List<AccessibilityNodeInfo>? {
        return nodeInfo?.findAccessibilityNodeInfosByText(text)
    }

    fun hasNodes(nodeInfo: AccessibilityNodeInfo?, text: String): Boolean {
        return !nodeInfo?.findAccessibilityNodeInfosByText(text)?.filter { it?.text == text }.isNullOrEmpty()
    }

    fun hasTextOnlyOneNode(nodeInfo: AccessibilityNodeInfo?, text: String): Boolean {
        val list = nodeInfo?.findAccessibilityNodeInfosByText(text)?.filter { it?.text == text }
        return !list.isNullOrEmpty() && list.size == 1
    }

    /**
     * 按文本点击
     */
    fun clickNodesByText(nodeInfo: AccessibilityNodeInfo?, text: String) {
        nodeInfo?:return
        val nodes = findNodesByText(nodeInfo, text)
        if (nodes == null || nodes.isEmpty())
            return
        for (node in nodes) {
            if (node.isEnabled && node.isClickable) {
//                Logger.d(InstallAccessibilityService.TAG, "onAccessibilityEvent clickNodesByText text=$text nodeInfo=$nodeInfo")
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                node.recycle()
            }
        }
    }
}