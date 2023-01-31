import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path;
        int count=0;
        Statistics statistics = new Statistics();
        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>:");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);

            boolean pathOfFile = file.exists();
            boolean isNotDirectory = !file.isDirectory();
            if (pathOfFile && isNotDirectory) {
                System.out.println("Путь к файлу является правильным");
                System.out.println("Это файл №" + ++count);
                try {
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader =
                            new BufferedReader(fileReader);

                    String line;


                    while ((line = reader.readLine()) != null) {
                        int length = line.length();

                        if (length > 1024) {
                            throw new RuntimeException("Длинна строки > 1024");
                        }

                        LogEntry e = new LogEntry(line);
                        statistics.addEntry(e);

                    }

                        System.out.println("Total traffic: " + statistics.totalTraffic + " bytes");
                        System.out.println("Min time: " + statistics.minTime);
                        System.out.println("Max time: " + statistics.maxTime);
                        System.out.println("Traffic rate: " + statistics.getTrafficRate() + " bytes per hour");

                        System.out.println("Peak user Visits: " + statistics.getPeakVisits());
                        System.out.println("User Max Visits: " + statistics.getErrorsRate());

                } catch (FileNotFoundException e) {
                    System.out.println(e.fillInStackTrace());
                } catch (IOException e) {
                    System.out.println(e.fillInStackTrace());
                };
            } else {
                System.out.println("Файл отсутствует или путь ведет к директории файла");}

        }
    }
}