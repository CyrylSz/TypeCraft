package functions;

public class finish extends func {
    public static void Main(long startTime){

        String data_path = getDynamicFilePath("\\texts\\record.txt");
        int entry_val = 69696969;
        sprawdzIStworzPlik(data_path, entry_val);

        long czasMilisekundyRekord = odczytajLiczbeZPliku(data_path);
        long czasMinutyRekord = czasMilisekundyRekord / 60000;
        long czasSekundyRekord = (czasMilisekundyRekord % 60000) / 1000;

        long czasMilisekundy = System.currentTimeMillis() - startTime;
        if((czy_norma == 'y' || czy_norma == 'Y') && (czasMilisekundy < czasMilisekundyRekord))
            nadpiszLiczbeWPliku(data_path, czasMilisekundy);
        long czasMinuty = czasMilisekundy / 60000;
        long czasSekundy = (czasMilisekundy % 60000) / 1000;

        if(jezyk.equalsIgnoreCase("pol")) {
            System.out.println("\n============");
            System.out.println("[ WYGRALEŚ ]");
            System.out.println("============");
            System.out.println("* Staty:");
            if (czy_norma == 'n' || czy_norma == 'N'){
                if (wybor_edycji.equals("1")){
                    System.out.println("  - Wygraleś mając [" + int_options.get("liczba_inputow_na_poziom") + "] kroków na Poziomie 1/3 :D\n" +
                            "  - Z [" + int_options.get("odejmowane_kroki_co_poziom") + "] odejmowanych kroków co poziom!");
                } else {
                    System.out.println("  - Wygraleś mając 1.["+edycja_manualna[0]+"] 2.["+edycja_manualna[1]+"] 3.["+edycja_manualna[2]+"] kroków na Poziomach 1-3 :D");
                }
            }
            if((czy_norma == 'y' || czy_norma == 'Y') && (czasMilisekundy < czasMilisekundyRekord)){
                if(czasMinuty!=0)System.out.println("  - NOWY REKORD! Czas: "+czasMinuty+"m | "+czasSekundy+"s :D");
                else System.out.println("  - NOWY REKORD! Czas: "+czasSekundy+"s :D");
            } else {
                if (czy_norma == 'y' || czy_norma == 'Y') {
                    if(czasMinuty!=0)System.out.println("  - Obecny czas: "+czasMinuty+"m | "+czasSekundy+"s");
                    else System.out.println("  - Obecny czas: "+czasSekundy+"s");
                    if(czasMinuty!=0)System.out.println("  - Twój rekord: "+czasMinutyRekord+"m | "+czasSekundyRekord+"s");
                    else System.out.println("  - Twój rekord: "+czasSekundyRekord+"s");
                } else {
                    if(czasMinuty!=0)System.out.println("  - Czas: "+czasMinuty+"m | "+czasSekundy+"s");
                    else System.out.println("  - Czas: "+czasSekundy+"s");
                }
            }
            if (int_options.get("fails") > 0) System.out.println("  - Utracone win-streaki: " + int_options.get("fails"));
            else System.out.println("  - PLATYNA! Nie stracileś żadnego win-streaku :o");
        }
        else {
            System.out.println("\n============");
            System.out.println("[ YOU WON! ]");
            System.out.println("============");
            System.out.println("* Stats:");
            if (czy_norma == 'n' || czy_norma == 'N') {
                if (wybor_edycji.equals("1")){
                    System.out.println("  - You won with [" + int_options.get("liczba_inputow_na_poziom") + "] steps on Level 1/3 :D\n" +
                            "  - With [" + int_options.get("liczba_inputow_na_poziom") + "] subtracted steps per level!");
                } else {
                    System.out.println("  - You won with 1.["+edycja_manualna[0]+"] 2.["+edycja_manualna[1]+"] 3.["+edycja_manualna[2]+"] steps on Levels 1-3 :D");
                }
            }
            if((czy_norma == 'y' || czy_norma == 'Y') && (czasMilisekundy < czasMilisekundyRekord)){
                if(czasMinuty!=0)System.out.println("  - NEW RECORD! Time: "+czasMinuty+"m | "+czasSekundy+"s :D");
                else System.out.println("  - NEW RECORD! Time: "+czasSekundy+"s :D");
            } else {
                if (czy_norma == 'y' || czy_norma == 'Y') {
                    if(czasMinuty!=0)System.out.println("  - Current time: "+czasMinuty+"m | "+czasSekundy+"s");
                    else System.out.println("  - Current time: "+czasSekundy+"s");
                    if(czasMinuty!=0)System.out.println("  - Your record: "+czasMinutyRekord+"m | "+czasSekundyRekord+"s");
                    else System.out.println("  - Your record: "+czasSekundyRekord+"s");
                } else {
                    if(czasMinuty!=0)System.out.println("  - Time: "+czasMinuty+"m | "+czasSekundy+"s");
                    else System.out.println("  - Time: "+czasSekundy+"s");
                }
            }
            if (int_options.get("fails") > 0) System.out.println("  - Lost win-streak count: " + int_options.get("fails"));
            else System.out.println("  - PLATINUM! You didn't lose any win-streak :o");
        }
        System.out.println();
    }
}
