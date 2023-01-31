import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Statistics {
    LogEntry[] entries;
    int lastEntryIndex = 0;
    final HashSet<String> okPages;
    final HashSet<String> notFoundPages;
    final HashMap<String, Integer> osStat;
    final HashMap<String, Integer> browserStat;
    long totalTraffic = 0;
    long totalVisits = 0;
    long totalErrors = 0;
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

        if (entry.userAgent.isReal) {
            ++totalVisits;
        }

        if (entry.date.isBefore(minTime)) {
            minTime = entry.date;
        }
        if (entry.date.isAfter(maxTime)) {
            maxTime = entry.date;
        }

        String code = entry.answerCode;
        if (code.equals("200")) {
            okPages.add(entry.path);
        } else if (code.startsWith("4") || code.startsWith("5")) {
            if (code.equals("404")) {
                notFoundPages.add(entry.path);
            }
            ++totalErrors;
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

    public int getVisitRate() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0)
            hours = 1;
        return (int) (totalVisits / hours);
    }

    public int getErrorsRate() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hours == 0)
            hours = 1;
        return (int) (totalErrors / hours);
    }

    public int getUserRate() {
        long users = Arrays.stream(entries)
                .limit(lastEntryIndex)
                .filter(entry -> entry.userAgent.isReal)
                .map(entry -> entry.IPAddress)
                .distinct()
                .count();
        if (users == 0)
            return 0;
        return (int) (totalVisits / users);
    }

    public int getPeakVisits() {
        var visits = Arrays.stream(entries)
                .limit(lastEntryIndex)
                .filter(entry -> entry.userAgent.isReal)
                .map(entry -> ChronoUnit.SECONDS.between(entry.date, minTime))
                .collect(Collectors.groupingBy(seconds -> seconds, Collectors.counting()));
        return visits.values().stream()
                .max(Long::compare)
                .orElse(0L)
                .intValue();
    }

    public int getUserMaxVisits() {
        var usersStat = Arrays.stream(entries)
                .limit(lastEntryIndex)
                .filter(entry -> entry.userAgent.isReal)
                .map(entry -> entry.IPAddress)
                .collect(Collectors.groupingBy(ip -> ip, Collectors.counting()));
        return usersStat.values().stream()
                .max(Long::compare)
                .orElse(0L)
                .intValue();
    }

    public List<String> getReferrers() {
        return Arrays.stream(entries)
                .limit(lastEntryIndex)
                .map(entry -> entry.referer)
                .filter(Predicate.not(String::isEmpty))
                .map(ref -> ref.replaceAll("http(s)?://|www\\.|/.*", ""))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getOkPages() {
        return Arrays.asList(okPages.toArray(new String[0]));
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
