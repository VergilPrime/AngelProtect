package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.AngelProtect;
import com.vergilprime.angelprotect.datamodels.APConfig;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class UtilTiming {

    private final String name;
    private final List<Long> log = new ArrayList<>();

    public UtilTiming(String name) {
        this.name = name;
    }

    public void submit(long nanoTime) {
        synchronized (log) {
            log.add(nanoTime);
        }
    }

    public void reset() {
        synchronized (log) {
            log.clear();
        }
    }

    public String getName() {
        return name;
    }

    public List<Long> getLog() {
        return Collections.unmodifiableList(log);
    }

    public LongStream stream() {
        return log.stream().mapToLong(l -> l);
    }

    public double getAverage() {
        return stream().summaryStatistics().getAverage();
    }

    public double getStDev() {
        double avg = getAverage();
        return Math.sqrt(stream().mapToDouble(l -> Math.pow(l - avg, 2)).sum());
    }

    public long min() {
        return stream().min().orElse(0);
    }

    public long max() {
        return stream().max().orElse(0);
    }

    @Override
    public String toString() {
        return "UtilTiming{" +
                "name='" + name + '\'' +
                ",sync=" + Bukkit.isPrimaryThread() +
                '}';
    }

    public static class Timing {
        private long start = System.nanoTime();
        private UtilTiming timing;

        public Timing(UtilTiming timing) {
            this.timing = timing;
        }

        public void stop() {
            if (timing != null) {
                if (start != -1) {
                    timing.submit(System.nanoTime() - start);
                    start = -1;
                } else {
                    Debug.log("Error stopping timing '" + timing.getName() + "', already stopped!");
                }
            }
        }
    }

    private static final Map<String, UtilTiming> syncRecords = new HashMap<>();
    private static final Map<String, UtilTiming> asyncRecords = new HashMap<>();

    private static final UtilTiming noOpTiming = new UtilTiming("No Operation");

    public static UtilTiming get(String name) {
        if (!APConfig.get().doTimings) {
            return noOpTiming;
        }
        Map<String, UtilTiming> records = Bukkit.isPrimaryThread() ? syncRecords : asyncRecords;
        synchronized (records) {
            return records.computeIfAbsent(name, s -> new UtilTiming(name));
        }
    }

    public static Timing start(String name) {
        if (!APConfig.get().doTimings) {
            return new Timing(null);
        }
        return new Timing(get(name));
    }

    public static Timing dummy() {
        return new Timing(null);
    }

    public static void resetAll() {
        synchronized (syncRecords) {
            synchronized (asyncRecords) {
                syncRecords.clear();
                asyncRecords.clear();
            }
        }
    }

    public static Map<String, UtilTiming> getSyncRecords() {
        return Collections.unmodifiableMap(syncRecords);
    }

    public static Map<String, UtilTiming> getAsyncRecords() {
        return Collections.unmodifiableMap(asyncRecords);
    }

    public static void dumpFiles() {
        if (!APConfig.get().doTimings) {
            Debug.log("Timings is disabled in config, skipping writing files.");
            return;
        }
        Debug.log("Writing timings to files...");
        dump(syncRecords, "sync.txt");
        dump(asyncRecords, "async.txt");
        Debug.log("Done writing timings.");
    }

    private static void dump(Map<String, UtilTiming> map, String filename) {
        String data = "Total time used: " + UtilString.roundNanoSeconds(map.values().stream().flatMapToLong(t -> t.stream()).sum());
        data += "\nTotal events logged: " + map.size();
        data += "\nTimestamp written: " + new Date().toString();
        data += "\n\n";
        data += map.values().stream()
                .sorted(Comparator.comparingDouble(t -> -t.getAverage()))
                .map(t -> t.name + ": Average: " + UtilString.roundNanoSeconds(t.getAverage()) + ", Std.Dev.: " + UtilString.roundNanoSeconds(t.getStDev()) + ", Count: " + t.getLog().size() + ", Min: " + UtilString.roundNanoSeconds(t.min()) + ", Max: " + UtilString.roundNanoSeconds(t.max()))
                .collect(Collectors.joining("\n"));
        File folder = new File(AngelProtect.getInstance().getDataFolder(), "timings");
        folder.mkdirs();
        UtilSerialize.writeFile(new File(folder, filename), data);
    }
}
