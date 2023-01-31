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
    final HashSet<String> notFoundPages;
    final HashMap<String, Integer> osStat;
    final HashMap<String, Integer> browserStat;
    long totalTraffic = 0;
    LocalDateTime minTime = LocalDateTime.MAX;
    LocalDateTime maxTime = LocalDateTime.MIN;

    Statistics() {
        entries = new LogEntry[200000];
        okPages = new HashSet<>();
        notFoundPages = new HashSet<>();
        osStat = new HashMap<>();
        browserStat = new HashMap<>();
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
        } else if (entry.answerCode.equals("404")) {
            notFoundPages.add(entry.path);
        }

        String os = entry.userAgent.operatingSystem;
        osStat.put(os, osStat.getOrDefault(os, 0) + 1);

        String browser = entry.userAgent.browser;
        browserStat.put(browser, browserStat.getOrDefault(browser, 0) + 1);
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
    public List<String> getNoPages() {
        return Arrays.asList(notFoundPages.toArray(new String[0]));
    }

    private static HashMap<String, Double> getStatistics(HashMap<String, Integer> statMap) {
        HashMap<String, Double> result = new HashMap<>();
        for (var entry : statMap.entrySet()) {
            result.put(entry.getKey(), Double.valueOf(entry.getValue()) / statMap.size());
        }
        return result;
    }

    public HashMap<String, Double> getPlatformStatistics() {
        return getStatistics(osStat);
    }

    public HashMap<String, Double> getBrowserStatistics() {
        return getStatistics(browserStat);
    }
}
