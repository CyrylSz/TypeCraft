package functions;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class func{
    public static boolean exit = false;
    public static char czy_norma = 'y';
    public static Map<String, Integer> int_options = new HashMap<>();
    public static int[] edycja_manualna  = new int[4];
    public static String jezyk = "eng", wybor_edycji = "none";
    public static final Logger logger = Logger.getLogger("MyLog");
    private static final String logs_file = getDynamicFilePath("\\logs\\log%g");

    static {
        try {
            FileHandler fh = new FileHandler(logs_file, 1024 * 1024, 5, true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
    }

    public static void IfJezykPrint(String jezyk,String type, String IfPol, String IfAng){
        if (type.equals("println")) {
            if (jezyk.equalsIgnoreCase("pol"))System.out.println(IfPol);
            else System.out.println(IfAng);
        } else if (type.equals("print")) {
            if (jezyk.equalsIgnoreCase("pol"))System.out.print(IfPol);
            else System.out.print(IfAng);
        }
    }
    public static int Konfiguracja(String jezyk, Scanner input, int opcja) {

        boolean czy_inne_niz_int = true;
        int wartosc = 0;

        while (czy_inne_niz_int) {
            try {
                if (opcja == 1) {
                    IfJezykPrint(jezyk, "print","  1. Podaj liczbę kroków na pierwszy poziom: ","  1. Enter the number of steps in the first level: ");
                } else if (opcja == 2) {
                    IfJezykPrint(jezyk, "print","  2. Wprowadź liczbę odejmowanych kroków co poziom: ","  2. Enter the number of subtracted steps per level: ");
                } else if (opcja == 3) {
                    IfJezykPrint(jezyk, "print","  3. Podaj co ile kroków wyświetlać powiadomienia o stanie win-streaku: ","  3. Enter how often to display notifications about the win-streak status: ");
                } else if (opcja == 4) {
                    IfJezykPrint(jezyk, "print","  1. Podaj liczbę kroków na pierwszy poziom: ","  1. Enter the number of steps in the first level: ");
                } else if (opcja == 5) {
                    IfJezykPrint(jezyk, "print","  2. Podaj liczbę kroków na drugi poziom: ","  2. Enter the number of steps in the second level: ");
                } else if (opcja == 6) {
                    IfJezykPrint(jezyk, "print","  3. Podaj liczbę kroków na trzeci poziom: ","  3. Enter the number of steps in the third level: ");
                } else if (opcja == 7) {
                    IfJezykPrint(jezyk, "print","  4. Podaj co ile kroków wyświetlać powiadomienia o stanie win-streaku: ","  4. Enter how often to display notifications about the win-streak status: ");
                }
                String line = input.nextLine();

                if (line.matches("\\s*\\d+\\s*")) {
                    wartosc = Integer.parseInt(line.trim());
                    czy_inne_niz_int = false;
                } else {
                    IfJezykPrint(jezyk, "println","  Niepoprawny format!","  Invalid format!");
                }
            } catch (InputMismatchException | NumberFormatException e) {
                IfJezykPrint(jezyk, "println","  Liczba jest za długa!","  Integer is too long!");
            }
        }
        return wartosc;
    }
    public static void choose_lang(){
        java.util.Scanner input = new java.util.Scanner(System.in);

        boolean i_jezyk = true;
        System.out.print("* ");
        while(i_jezyk){
            System.out.print("Choose language [eng/pol]: ");
            String wj = input.nextLine();
            if(wj.equalsIgnoreCase("pol") || wj.equalsIgnoreCase("eng")){
                i_jezyk = false;jezyk=wj;
            } else {System.out.println("  There is no such language :(");System.out.print("  ");}
        }
        System.out.println("----------------------------");
    }
    public static void Powiadomienia(int winstreak, int co_ile_powiad){
        if (winstreak % co_ile_powiad == 0 && winstreak > 0)System.out.println("Win-streak: "+winstreak+"!");
    }
    public static int kontrolaLosowania(String[] tab, ArrayList<Integer> historia, int procent){
        boolean powtorzenie_slowa = true;
        int index = 0;
        while (powtorzenie_slowa) {
            index = (int)(Math.random()*tab.length);
            if (!historia.contains(index)) {
                historia.add(index);
                powtorzenie_slowa = false;
                if (historia.size() == tab.length - tab.length/procent)historia.clear();
            }
        }
        return index;
    }
    public static long odczytajLiczbeZPliku(String sciezka) {
        long wczytanaLiczba = 0;
        try (Scanner scanner = new Scanner(new File(sciezka))) {
            if (scanner.hasNextLong())
                wczytanaLiczba = scanner.nextLong();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
        return wczytanaLiczba;
    }
    public static void nadpiszLiczbeWPliku(String sciezka, long nowaLiczba) {
        try (FileWriter fileWriter = new FileWriter(sciezka)) {
            fileWriter.write(Long.toString(nowaLiczba));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
    }
    public static void sprawdzIStworzPlik(String path, int val) {
        File plik = new File(path);
        try {
            if (!plik.exists()) {
                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write(val);
                fileWriter.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
    }
    private static String[] txt_to_array(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error: ", e);
        }
        String c = sb.toString();
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher m = pattern.matcher(c);
        sb = new StringBuilder();

        while (m.find()) {
            if (!sb.isEmpty()) {
                sb.append("\", \"");
            }
            sb.append(m.group(1));
        }

        return sb.toString().split("\"\\s*,\\s*\\n*\"");
    }
    public static String[] WordArr(String jezyk) {
        String[] arr;
        if (jezyk.equalsIgnoreCase("pol")) {
            arr = txt_to_array(getDynamicFilePath("\\texts\\words_pol.txt"));
        } else {
            arr = txt_to_array(getDynamicFilePath("\\texts\\words_eng.txt"));
        }
        return arr;
    }
    public static String[] SentenceArr(String jezyk) {
        String[] arr;
        if (jezyk.equalsIgnoreCase("pol")) {
            arr = txt_to_array(getDynamicFilePath("\\texts\\sentence_pol.txt"));
        } else {
            arr = txt_to_array(getDynamicFilePath("\\texts\\sentence_eng.txt"));
        }
        return arr;
    }
    public static void clearTerminal(){
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Unix/Linux
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Error: ", e);
        }
    }
    public static String getDynamicFilePath(String relativePath) {
        String userDir = System.getProperty("user.dir");

        if (userDir.contains("out\\production")) {
            return userDir + relativePath;
        } else {
            return userDir + "\\src" + relativePath;
        }
    }
}
