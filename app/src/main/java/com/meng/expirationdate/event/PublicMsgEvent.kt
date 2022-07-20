package com.meng.expirationdate.event

import com.jeremyliao.liveeventbus.LiveEventBus

object PublicMsgEvent {
    const val DATA_CHANGE_EVENT = "DATA_CHANGE_EVENT"

    /**
     * 通知数据更新
     * @param type 0添加 1删除
     * */
    fun dataChangeEvent(type: Int) {
        LiveEventBus.get(DATA_CHANGE_EVENT).postDelay(type, 500)
    }
}