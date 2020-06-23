package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Cache<K, V> {

    public static final int CLEANUP_INTERVAL = 20 * 30;
    private static BukkitTask cleanupTask;
    private static List<WeakReference<Cache<?, ?>>> caches = new ArrayList<>();

    private Map<K, CacheValue<V>> map = new HashMap<>();
    private long timeout;

    public Cache(long timeout) {
        init();
        this.timeout = timeout;
        synchronized (caches) {
            caches.add(new WeakReference<>(this));
        }
    }

    public void put(K key, V value) {
        synchronized (map) {
            map.put(key, new CacheValue<>(value));
        }
    }

    public V get(K key) {
        return get(key, null);
    }

    public V get(K key, V def) {
        synchronized (map) {
            CacheValue<V> value = map.get(key);
            if (value == null) {
                return def;
            }
            if (value.hasExpirted(timeout)) {
                map.remove(key);
                return def;
            }
            return value.getValue();
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void clean() {
        synchronized (map) {
            for (Iterator<Map.Entry<K, CacheValue<V>>> it = map.entrySet().iterator(); it.hasNext(); ) {
                if (it.next().getValue().hasExpirted(timeout)) {
                    it.remove();
                }
            }
        }
    }

    public static class CacheValue<V> {

        private final long access = System.currentTimeMillis();
        private final V value;

        public CacheValue(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        public boolean hasExpirted(long timeoutMs) {
            return System.currentTimeMillis() > access + timeoutMs;
        }
    }

    public static void init() {
        if (cleanupTask == null) {
            cleanupTask = new BukkitRunnable() {
                @Override
                public void run() {
                    synchronized (caches) {
                        for (Iterator<WeakReference<Cache<?, ?>>> it = caches.iterator(); it.hasNext(); ) {
                            WeakReference<Cache<?, ?>> next = it.next();
                            if (next.isEnqueued()) {
                                it.remove();
                            } else {
                                next.get().clean();
                            }
                        }
                    }
                }
            }.runTaskTimer(AngelProtect.getInstance(), 0, CLEANUP_INTERVAL);
        }
    }

}
