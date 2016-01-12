package org.xutils.common;

import java.lang.reflect.Type;

/**
 * Created by wyouflf on 15/6/5.
 * 通用回调接口
 */
public interface Callback {

    public interface CommonCallback<ResultType> extends Callback {
        void onSuccess(ResultType result,int tag);

        void onError(Throwable ex, boolean isOnCallback,int tag);

        void onCancelled(CancelledException cex,int tag);

        void onFinished(int tag);
    }

    public interface TypedCallback<ResultType> extends CommonCallback<ResultType> {
        Type getResultType();
    }

    public interface CacheCallback<ResultType> extends CommonCallback<ResultType> {
        boolean onCache(ResultType result,int tag);
    }

    public interface PrepareCallback<PrepareType, ResultType> extends CommonCallback<ResultType> {
        ResultType prepare(PrepareType rawData);
    }

    public interface ProgressCallback<ResultType> extends CommonCallback<ResultType> {
        void onWaiting();

        void onStarted();

        void onLoading(long total, long current, boolean isDownloading);
    }

    public interface GroupCallback<ItemType> extends Callback {
        void onSuccess(ItemType item);

        void onError(ItemType item, Throwable ex, boolean isOnCallback);

        void onCancelled(ItemType item, CancelledException cex);

        void onFinished(ItemType item);

        void onAllFinished();
    }

    public interface Callable<ResultType> {
        void call(ResultType result);
    }

    public interface Cancelable {
        void cancel();

        boolean isCancelled();
    }

    public static class CancelledException extends RuntimeException {
        public CancelledException(String detailMessage) {
            super(detailMessage);
        }
    }
}
