package levels;
import functions.func;

import java.util.Scanner;

public class chars extends func {
    public static void Main() {

        Scanner input = new Scanner(System.in);
        boolean proces = true;
        char[] polskie_znaki = {'ą', 'ć', 'ę', 'ł', 'ń', 'ó', 'ś', 'ź', 'ż', 'Ą', 'Ć', 'Ę', 'Ł', 'Ń', 'Ó', 'Ś', 'Ź', 'Ż'};

        int progress = 0;

        System.out.println("----------------------------\n");
        IfJezykPrint(jezyk, "println",
        "< Poziom 1/3 >\nWymagane poprawne wpisy: [" + int_options.get("liczba_inputow_na_poziom") + "]\n",
        "< Level 1/3 >\nCorrect entries required: [" + int_options.get("liczba_inputow_na_poziom") + "]\n");

        while (proces) {
            Powiadomienia(int_options.get("winstreak"), int_options.get("co_ile_powiad"));

            int znak;
            if (jezyk.equalsIgnoreCase("pol")) {
                int los = (int) (Math.random() * (95 + polskie_znaki.length));
                if (los >= 95) {
                    los -= 95;
                    znak = polskie_znaki[los];
                } else {
                    znak = (int) (Math.random() * 94 + 33);
                }
            } else {
                znak = (int) (Math.random() * 94 + 33);
            }

            IfJezykPrint(jezyk, "print", "Wpisz: ", "Enter: ");
            System.out.println((char) znak);
            System.out.print("       ");

            String linia1 = input.nextLine();
            String[] slowa1 = linia1.split("\\s+");

            if (slowa1.length > 0) {
                String s1 = slowa1[0];
                if (s1.length() == 1) {
                    char firstChar = s1.charAt(0);
                    if (firstChar == (char) znak) {
                        int_options.put("winstreak", int_options.get("winstreak") + 1);
                        progress++;
                    } else {
                        if (int_options.get("winstreak") > 0)
                            IfJezykPrint(jezyk, "println", "Win-streak utracony :(", "Win-streak lost :(");
                        int_options.put("winstreak", 0);
                        int_options.put("fails", int_options.get("fails") + 1);
                    }
                } else if (s1.equalsIgnoreCase("exit")){proces = false; exit = true;}
                else {
                    if (int_options.get("winstreak") > 0) IfJezykPrint(jezyk, "println", "Win-streak utracony :(", "Win-streak lost :(");
                    int_options.put("winstreak", 0);
                    int_options.put("fails", int_options.get("fails") + 1);
                }
            }
            if (progress >= int_options.get("liczba_inputow_na_poziom"))proces = false;
        }
    }
}