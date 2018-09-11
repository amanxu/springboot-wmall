package com.wmall.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author niexx <br>
 * @version 1.0 <br>
 * @Description:延时队列
 * @Company: Shanghai COS Software <br>
 * @Copyright: Copyright (c)2017 <br>
 * @Date 2018年4月28日 下午6:40:11 <br>
 */
public class DelayedQueueItem<T> implements Delayed {

    private T t;
    private long liveTime;
    private long removeTime;

    public DelayedQueueItem(T t, long liveTime) {
        this.setT(t);
        this.liveTime = liveTime;
        this.removeTime = TimeUnit.NANOSECONDS.convert(liveTime, TimeUnit.NANOSECONDS) + System.nanoTime();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(Delayed delayed) {
        if (delayed == null) {
            return 1;
        }
        if (delayed == this) {
            return 0;
        }
        if (delayed instanceof DelayedQueueItem) {
            DelayedQueueItem<T> tempDelayedItem = (DelayedQueueItem<T>) delayed;
            if (liveTime > tempDelayedItem.liveTime) {
                return 1;
            } else if (liveTime == tempDelayedItem.liveTime) {
                return 0;
            } else {
                return -1;
            }
        }
        long diff = getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
        return diff > 0 ? 1 : diff == 0 ? 0 : -1;

    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(removeTime - System.nanoTime(), unit);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public long getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }

    public long getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(long removeTime) {
        this.removeTime = removeTime;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelayedQueueItem) {
            return object.hashCode() == hashCode() ? true : false;
        }
        return false;
    }

}
