import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path;
        int count =0;
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
                    int linesCount = 0;
                    int minLineLength = Integer.MAX_VALUE;
                    int maxLineLength = 0;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();

                        if (length > 1024) {
                            throw new RuntimeException("Длинна строки > 1024");
                        }

                        if (length > maxLineLength) {
                            maxLineLength = length;
                        }
                        if (length < minLineLength) {
                            minLineLength = length;
                        }
                        linesCount++;
                    }

                    System.out.println("Кол-во строк: " + linesCount);
                    System.out.println("Длинна максимальной строки: " + maxLineLength);
                    System.out.println("Длинна минимальной строки:" + minLineLength);
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