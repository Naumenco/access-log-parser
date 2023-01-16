import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    LogEntry[] entries;
    int lastEntryIndex = 0;

    long totalTraffic = 0;
    LocalDateTime minTime = LocalDateTime.MAX;
    LocalDateTime maxTime = LocalDateTime.MIN;

    Statistics() {
        entries = new LogEntry[200000];
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
    }

    public int getTrafficRate() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return (int)(totalTraffic / hours);
    }
}
