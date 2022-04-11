package com.meng.expirationdate.network;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/***
 * 页面状态标识类
 */

@IntDef({PageStateType.LOADING, PageStateType.EMPTY, PageStateType.ERROR, PageStateType.NOWORK, PageStateType.NORMAL, PageStateType.CONTENT})
@Retention(RetentionPolicy.SOURCE)
public @interface PageStateType {
    int NOWORK = -4;
    //加载中
    int LOADING = -3;
    //暂无数据
    int EMPTY = -2;
    //加载失败
    int ERROR = -1;
    //初始化状态
    int NORMAL = 0;
    //显示内容布局
    int CONTENT = 1;
}
