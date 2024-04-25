package levels;
import functions.func;

import java.util.ArrayList;
import java.util.Scanner;

public class words extends func {
    public static void Main() {

        Scanner input = new Scanner(System.in);
        boolean proces = true;
        if (wybor_edycji.equals("2"))int_options.put("liczba_inputow_na_poziom", edycja_manualna[1]);
        int lo = int_options.get("liczba_inputow_na_poziom") - int_options.get("odejmowane_kroki_co_poziom");
        if (lo < 0) lo = 0;
        IfJezykPrint(jezyk, "println",
                "\nPrzechodzisz do następnego poziomu!\n< Poziom 2/3 >\nWymagane poprawne wpisy: [" + lo + "]\n",
                "\nYou're advancing to the next level!\n< Level 2/3 >\nCorrect entries required: [" + lo + "]\n");

        int progress = 0;
        String[] arr = WordArr(jezyk);
        ArrayList<Integer> historia_slow = new ArrayList<>();

        while (proces) {
            Powiadomienia(int_options.get("winstreak"), int_options.get("co_ile_powiad"));

            int index1 = kontrolaLosowania(arr, historia_slow, 15);

            IfJezykPrint(jezyk, "print", "Wpisz: ", "Enter: ");
            System.out.println(arr[index1]);
            System.out.print("       ");

            String linia2 = input.nextLine();

            if (linia2.equals(arr[index1])) {
                int_options.put("winstreak", int_options.get("winstreak") + 1);
                progress++;
            } else if (linia2.equalsIgnoreCase("exit")) {
                proces = false;
                exit = true;
            } else {
                if (int_options.get("winstreak") > 0) IfJezykPrint(jezyk, "println", "Win-streak utracony :(", "Win-streak lost :(");
                int_options.put("winstreak", 0);
                int_options.put("fails", int_options.get("fails") + 1);
            }

            if (progress >= lo) proces = false;
        }
    }
}