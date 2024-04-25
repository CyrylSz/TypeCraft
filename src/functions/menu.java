package functions;

public class menu extends func {
    public static void Main(){
        int_options.put("winstreak",0);
        int_options.put("fails",0);
        int_options.put("liczba_inputow_na_poziom",25);
        int_options.put("odejmowane_kroki_co_poziom",10);
        int_options.put("co_ile_powiad", 5);

        IfJezykPrint(jezyk, "println",
                """
                        * Info:
                          - Wpisz 'exit' aby wyjść z programu.
                          - Nie rób literówek, aby przejść do następnego poziomu.
                          - Staraj się nie patrzeć na klawiature.""",
                """
                        * Info:
                          - Type 'exit' to exit the program.
                          - Don't make typos to move to the next level.
                          - Try not to look at the keyboard.""");
        System.out.println("----------------------------");

        IfJezykPrint(jezyk, "print",
                "* Graj w trybie domyślnym [y/n]: ",
                "* Play normal mode [y/n]: ");
        java.util.Scanner input = new java.util.Scanner(System.in);
        czy_norma = input.next().charAt(0);

        if(czy_norma == 'n' || czy_norma == 'N') {
            boolean brak_wyb = true;
            input = new java.util.Scanner(System.in);
            while (brak_wyb) {
                IfJezykPrint(jezyk, "print",
                        "  Wybierz tryb [1.Dekrementacyjny] lub [2.Manualny] -> [1/2]: ",
                        "  Choose [1.Decrementation] or [2.Manual] mode -> [1/2]: ");
                wybor_edycji = input.nextLine();
                if(wybor_edycji.equals("1")){ //Dekrementacja
                    IfJezykPrint(jezyk, "println",
                            "  Ustawienia trybu domyslnego:  1.[25]  2.[10]  3.[5]\n",
                            "  Normal mode settings:  1.[25]  2.[10]  3.[5]\n");
                    int_options.put("liczba_inputow_na_poziom", Konfiguracja(jezyk, input, 1));
                    int_options.put("odejmowane_kroki_co_poziom", Konfiguracja(jezyk, input, 2));
                    int_options.put("co_ile_powiad", Konfiguracja(jezyk, input, 3));
                    brak_wyb = false;
                } else if (wybor_edycji.equals("2")){ //Manualna
                    IfJezykPrint(jezyk, "println",
                            "  Ustawienia trybu domyslnego:  1.[25]  2.[15]  3.[5]  4.[5]\n",
                            "  Normal mode settings:  1.[25]  2.[15]  3.[5]  4.[5]\n");
                    for (int i = 0; i<edycja_manualna.length;i++){
                        edycja_manualna[i] = Konfiguracja(jezyk, input, 4+i);
                    }
                    int_options.put("liczba_inputow_na_poziom", edycja_manualna[0]);
                    int_options.put("odejmowane_kroki_co_poziom", 0);
                    int_options.put("co_ile_powiad", edycja_manualna[3]);
                    brak_wyb = false;
                } else {
                    IfJezykPrint(jezyk, "println",
                            "  Nie ma takiej metody :(",
                            "  There is no such method :(");
                }
            }
            if (int_options.get("co_ile_powiad") <= 0)int_options.put("co_ile_powiad", 1);
        }
    }
}
