package com.mega.core.jdbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class ObjectPool<T> {

    /**
     * 正在使用的，保存新建的资源
     */
    private ConcurrentHashMap<T, Long> used;

    /**
     * 未被使用的，保存释放的、未失效的资源，池子里面保存有效可用的对象
     */
    private ConcurrentHashMap<T, Long> pool;

    /**
     * 默认 最大连接数
     */
    private static int DEFAULT_MAX_CONNECT_SIZE = 100;
    /**
     * 默认 池中对象最大生存时间
     */
    private static int DEFAULT_MAX_KEEP_ALIVE_TIME = 3000;

    int maxConnectSize = DEFAULT_MAX_CONNECT_SIZE;

    int maxKeepAliveTime = DEFAULT_MAX_KEEP_ALIVE_TIME;

    /**
     * 声明一个lock锁
     */
    private Lock lock = new ReentrantLock();
    /**
     * 声明1个等待队列
     */
    private Condition usedCondition = lock.newCondition();

    /**
     * 创建对象
     *
     * @return 对象
     */
    protected abstract T create();

    /**
     * 验证对象有效性
     *
     * @param t          对象
     * @param createTime 创建时间
     * @return 是否有效
     */
    protected abstract boolean validate(T t, long createTime);

    /**
     * 使对象失效
     *
     * @param t 资源对象
     */
    protected abstract void expire(T t);

    /**
     * 获取资源
     *
     * @return 资源对象
     */
    T getResource() {
        T obj = null;
        lock.lock();
        try {
            if (pool == null) {
                pool = new ConcurrentHashMap<>();
            }
            if (used == null) {
                used = new ConcurrentHashMap<>();
            }
            while (used.size() >= maxConnectSize) {
                try {
                    usedCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (pool.size() > 0) {
                for (Map.Entry<T, Long> entry : pool.entrySet()) {
                    obj = entry.getKey();
                    if (obj != null) {
                        pool.remove(obj);
                        break;
                    }
                }
            }
            if (obj == null) {
                obj = create();
            }
            used.put(obj, System.currentTimeMillis());
            usedCondition.signal();
        } finally {
            lock.unlock();
        }
        return obj;
    }

    /**
     * 释放资源
     *
     * @param t 资源对象
     */
    void release(T t) {
        lock.lock();
        try {
            if (t == null || !used.containsKey(t)) return;
            if (validate(t, used.get(t))) {
                pool.put(t, System.currentTimeMillis());
                used.remove(t);
            } else {
                expire(t);
                used.remove(t);
            }
            usedCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}
