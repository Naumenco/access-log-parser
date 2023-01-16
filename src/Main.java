import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
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
                    int googleBotCount = 0;
                    int yandexBotCount = 0;
                    int minLineLength = Integer.MAX_VALUE;
                    int maxLineLength = 0;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();

                        if (length > 1024) {
                            throw new RuntimeException("Длинна строки > 1024");
                        }
                        char bracket = '"';
                        int[] bracketIndices = new int[6];
                        bracketIndices[0] = line.indexOf(bracket);
                        bracketIndices[1] = line.indexOf(bracket, bracketIndices[0] + 1);
                        bracketIndices[2] = line.indexOf(bracket, bracketIndices[1] + 1);
                        bracketIndices[3] = line.indexOf(bracket, bracketIndices[2] + 1);
                        bracketIndices[4] = line.indexOf(bracket, bracketIndices[3] + 1);
                        bracketIndices[5] = line.indexOf(bracket, bracketIndices[4] + 1);

                        String thirdBrackets = line.substring(bracketIndices[4], bracketIndices[5]); // Берем 3 строку в кавычках

                        String[] parts = thirdBrackets.split(";");
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].trim();
                        }
                        if (parts.length >= 2) {
                            String fragment = parts[1];

                            int slashIndex = fragment.indexOf('/');
                            if (slashIndex > 0) {
                                String search_bot = fragment.substring(0, slashIndex);

                                if (search_bot.equalsIgnoreCase("Googlebot")) {
                                    googleBotCount++;
                                } else if (search_bot.equalsIgnoreCase("YandexBot")) {
                                    yandexBotCount++;
                                }
                            }
                        }

                        linesCount++;
                    }
                    System.out.println("Кол-во строк: " + linesCount);
                    System.out.println("Кол-во GoogleBot: " + googleBotCount + " (" + ((double)googleBotCount / (double)linesCount * 100.0) + "%)");
                    System.out.println("Кол-во YandexBot: " + yandexBotCount + " (" + (double)yandexBotCount / (double)linesCount * 100.0 + "%)");

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