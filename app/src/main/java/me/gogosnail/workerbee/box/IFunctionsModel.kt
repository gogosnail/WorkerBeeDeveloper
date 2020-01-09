package me.gogosnail.workerbee.box

import me.gogosnail.workerbee.data.FunctionsItem

/** Created by max on 2018/12/27.<br/>
 */
interface IFunctionsModel {

    /**
     * 加载工具列表
     */
    fun loadFunctions():List<FunctionsItem>
}