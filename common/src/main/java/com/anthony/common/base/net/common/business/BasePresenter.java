package com.anthony.common.base.net.common.business;

/**
 * 创建时间:2019/8/9
 * 创建人：anthony.wang
 * 功能描述：由于 Presenter 需要和 View 层通信,所以 Presenter 需要持有 View 层实例
 * View用泛型约束，使得 View 接口是 BaseView 的子类接口
 * 并在该类中可以写一些常用到的Presenter 通知 View 的方法，或者子 Presenter 需要常处理的逻辑
 */
public class BasePresenter<V extends BaseView> {
  protected V view;

  public BasePresenter(V view) {
    this.view = view;
  }

  protected String formatUrl(String needFormatUrl, String... params) {
    if (needFormatUrl != null && params.length > 0) {
      return String.format(needFormatUrl, params);
    }
    return null;
  }
}
