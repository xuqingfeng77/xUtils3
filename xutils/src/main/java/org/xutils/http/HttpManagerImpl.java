package org.xutils.http;

import android.util.Log;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wyouflf on 15/7/23.
 * HttpManager实现
 */
public final class HttpManagerImpl implements HttpManager {

    private static final Object lock = new Object();
    private static HttpManagerImpl instance;

    private HttpManagerImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new HttpManagerImpl();
                }
            }
        }
        x.Ext.setHttpManager(instance);
    }

    @Override
    public <T> Callback.Cancelable get(RequestParams entity, Callback.CommonCallback<T> callback) {
        return request(HttpMethod.GET, entity, callback);
    }

    @Override
    public <T> Callback.Cancelable post(RequestParams entity, Callback.CommonCallback<T> callback) {
        return request(HttpMethod.POST, entity, callback);
    }

    @Override
    public <T> Callback.Cancelable request(HttpMethod method, RequestParams entity, Callback.CommonCallback<T> callback) {
        entity.setMethod(method);
        Callback.Cancelable cancelable = null;
        if (callback instanceof Callback.Cancelable) {
            cancelable = (Callback.Cancelable) callback;
        }
        List<KeyValue>  mListNameVlue=entity.getQueryStringParams();
//        if(mListNameVlue!=null)
//        {
//            int size=mListNameVlue.size();
//            String strHttpLog=entity.getUri()+"?";
//            for (int i = 0; i <size; i++) {
//                strHttpLog+=mListNameVlue.get(i).getValueKey()+"="+mListNameVlue.get(i).getValueStr()+"&";
//            }
//            LogUtil.d("tag="+entity.getTag()+"\n"+strHttpLog);
//        }


        HttpTask<T> task = new HttpTask<T>(entity, cancelable, callback);
        return x.task().start(task);
    }
    @Override
    public <T> Callback.Cancelable request(HttpMethod method, RequestParams entity, Callback.CommonCallback<T> callback,int tag) {
        entity.setMethod(method);
        Callback.Cancelable cancelable = null;
        if (callback instanceof Callback.Cancelable) {
            cancelable = (Callback.Cancelable) callback;
        }
        entity.setTag(tag);
//        List<KeyValue>  mListNameVlue=entity.getQueryStringParams();
//        if(mListNameVlue!=null)
//        {
//            int size=mListNameVlue.size();
//            String strHttpLog=entity.getUri()+"?";
//            for (int i = 0; i <size; i++) {
//                strHttpLog+=mListNameVlue.get(i).getValueKey()+"="+mListNameVlue.get(i).getValueStr()+"&";
//            }
//            LogUtil.d("tag="+tag+"\n"+strHttpLog);
//        }

        HttpTask<T> task = new HttpTask<T>(entity, cancelable, callback);
        return x.task().start(task);
    }
    @Override
    public <T> T getSync(RequestParams entity, Class<T> resultType) throws Throwable {
        return requestSync(HttpMethod.GET, entity, resultType);
    }

    @Override
    public <T> T postSync(RequestParams entity, Class<T> resultType) throws Throwable {
        return requestSync(HttpMethod.POST, entity, resultType);
    }

    @Override
    public <T> T requestSync(HttpMethod method, RequestParams entity, Class<T> resultType) throws Throwable {
        DefaultSyncCallback<T> callback = new DefaultSyncCallback<T>(resultType);
        return requestSync(method, entity, callback);
    }

    @Override
    public <T> T requestSync(HttpMethod method, RequestParams entity, Callback.TypedCallback<T> callback) throws Throwable {
        entity.setMethod(method);
        HttpTask<T> task = new HttpTask<T>(entity, null, callback);
        return x.task().startSync(task);
    }

    private class DefaultSyncCallback<T> implements Callback.TypedCallback<T> {

        private final Class<T> resultType;

        public DefaultSyncCallback(Class<T> resultType) {
            this.resultType = resultType;
        }

        @Override
        public Type getResultType() {
            return resultType;
        }

        @Override
        public void onSuccess(T result,int tag) {

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback,int tag) {

        }

        @Override
        public void onCancelled(CancelledException cex,int tag) {

        }

        @Override
        public void onFinished(int tag) {

        }
    }
}
