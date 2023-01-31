import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Statistics {
    LogEntry[] entries;
    int lastEntryIndex = 0;
    final HashSet<String> okPages;
    final HashMap<String, Integer> osStat;
    long totalTraffic = 0;
    LocalDateTime minTime = LocalDateTime.MAX;
    LocalDateTime maxTime = LocalDateTime.MIN;

    Statistics() {
        entries = new LogEntry[200000];
        okPages = new HashSet<>();
        osStat = new HashMap<>();
    }

    public void addEntry(LogEntry entry) {
        entries[lastEntryIndex] = entry;
        lastEntryIndex++;

        totalTraffic += entry.answerSize;

        if (entry.date.isBefore(minTime)) {
            minTime = entry.date;
        }
        if (entry.date.isAfter(maxTime)) {
            maxTime = entry.date;
        }

        if (entry.answerCode.equals("200")) {
            okPages.add(entry.path);
        }

        String os = entry.userAgent.operatingSystem;
        osStat.put(os, osStat.getOrDefault(os, 0) + 1);
    }

    public int getTrafficRate() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0)
            hours = 1;
        return (int)(totalTraffic / hours);
    }

    public List<String> getOkPages() {
        return Arrays.asList(okPages.toArray(new String[0]));
    }

    public HashMap<String, Double> getPlatformStatistics() {
        HashMap<String, Double> result = new HashMap<>();
        for (var entry : osStat.entrySet()) {
            result.put(entry.getKey(), Double.valueOf(entry.getValue()) / osStat.size());
        }
        return result;
    }
}
