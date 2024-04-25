import functions.*;
import levels.*;

public class intu extends func {
    public static void main(String[] args) {

        System.out.println("==============================");
        System.out.println("[  Intuitive typing trainer  ]");
        System.out.println("==============================");

        choose_lang();
        menu.Main();

        long startTime = System.currentTimeMillis();
        chars.Main();
        if(!exit)words.Main();
        if(!exit)sentences.Main();

        if(!exit)finish.Main(startTime);

        IfJezykPrint(jezyk, "print",
                "Czy chcesz zagrać ponownie? [y/n]: ",
                "Do you want to play again? [y/n]: ");
        java.util.Scanner input = new java.util.Scanner(System.in);
        String choice = input.nextLine();
        if("y".equalsIgnoreCase(choice)){
            int_options.put("winstreak", 0);
            int_options.put("fails", 0);
            clearTerminal();
            main(args);
        }
        input.close();
    }
}

//TO DO:
// 3. visual menu with different modes and records (example: philosophy trainer, business trainer...)
// 4. more fancy improvements
// 5. racing difficulties (easy, medium, hard) & dynamic colors
// 6. ability to create your own races with your own words & sentences
// 7. graphics
