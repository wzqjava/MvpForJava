package com.anthony.common.base.net.client.request.form;

import com.anthony.common.base.net.client.base.BaseNetClient;
import com.anthony.common.base.net.common.observer.AppObserver;
import com.anthony.common.base.net.common.observer.SubscribeObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 创建时间:2019/8/6
 * 创建人：anthony.wang
 * 功能描述：表单提交的请求类
 * 其中 requestObservable 作为请求时候的被观察者，
 * SubscribeObserver 对象被 subscribe 调用作为观察者绑定到 requestObservable 的被观察者对象上，
 * 每一次的网络请求就有一堆请求响应的事件处于订阅关系，响应结果出来后通知 View 层刷新UI，
 * 这个时候可能就会造成内存泄露，比如在 A 页面请求接口想要改变当前页面某个TextView 的值，
 * 在请求发出后响应结果前，跳转到 B 页面，此刻 A 页面可能被销毁，但是刚刚的请求响应的订阅关系还存在，
 * 这个时候当响应成功后，就会造成内存泄露的问题

 */
public abstract class FormRequestClient extends BaseNetClient {


    public <T>Observable executeGet(String url,Map<String,Object> params, AppObserver<T> observer){
        return requestData(RequestType.GET,null,url,params,observer);
    }
    public <T>Observable executePost(String url,Map<String,Object> params, AppObserver<T> observer){
        return requestData(RequestType.POST,null,url,params,observer);
    }
    public <T>Observable executeGetWithHeader(Map<String,String> headerMap,String url,Map<String,Object> params, AppObserver<T> observer){
        return requestData(RequestType.GET,headerMap,url,params,observer);
    }
    public <T>Observable executePostWithHeader(Map<String,String> headerMap,String url,Map<String,Object> params, AppObserver<T> observer){
        return requestData(RequestType.POST,headerMap,url,params,observer);
    }

    private <T>Observable requestData(RequestType requestType,Map<String,String> headerMap, String url, Map<String, Object> params, AppObserver<T> observer) {
        if (requestType == null) {
            requestType = RequestType.GET;
        }
        if(params == null){
            params = new HashMap<>();
        }
        Observable<ResponseBody> requestObservable = null;
        switch (requestType) {
            case GET:
                if(headerMap!=null){
                    requestObservable = apiService.executeGetWithHeader(headerMap,url, params);
                }else{
                    requestObservable = apiService.executeGet(url, params);
                }

                break;
            case POST:
                if(headerMap!=null) {
                    requestObservable = apiService.executePostWithHeader(headerMap,url, params);
                }else{
                    requestObservable = apiService.executePost(url, params);

                }
                break;
        }
        if (requestObservable != null) {
            //若订阅需要绑定View层生命周期则走此方法 防止内存泄漏的问题
            if(observer.getAutoDisposeConverter()!=null){
                requestObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnDispose(() -> observer.onDispose())
                        .as(observer.getAutoDisposeConverter())
                        .subscribe(new SubscribeObserver(observer));
            }else{
                requestObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SubscribeObserver(observer));
            }
        }
        return requestObservable;
    }

}
