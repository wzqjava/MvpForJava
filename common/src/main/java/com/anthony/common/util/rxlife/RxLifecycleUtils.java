package com.anthony.common.util.rxlife;


import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;


/**
 * 创建时间:2019/8/7
 * 创建人：anthony.wang
 * 功能描述：做事件的生命周期处理 防止出现内存泄露
 *写一个接口返回 AutoDisposeConverter，让 View 基层基层实现并绑定返回 AutoDisposeConverter 实例。
 * 这里的目的是为了让 Presenter 层能够拿到所对应的 View 层的 AutoDisposeConverter 实例，
 * 从而能让订阅关系能够绑定 View 层的生命周期，避免内存泄漏问题

 */
public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("Can't instance the RxLifecycleUtils");
    }

    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner)
        );
    }
}
