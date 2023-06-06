package code_zonder_codesmells;

import java.util.Scanner;

public class VerkiezingSysteemImpl extends VerkiezingSysteem 
{

    @Override
    protected void stemmen() 
    {
        System.out.println("Stemmen worden verzameld...");
        Scanner scanner = new Scanner(System.in);
        String kiezerNaam = "";
        while (!kiezerNaam.equalsIgnoreCase("stop")) 
        {
            System.out.print("Voer uw naam in om te stemmen (typ 'stop' om te stoppen): ");
            kiezerNaam = scanner.nextLine();
            if (!kiezerNaam.equalsIgnoreCase("stop")) 
            {
                Kiezer kiezer = new Kiezer(kiezerNaam);
                vraagStem(kiezer);
            }
        }
    }

    protected void vraagStem(Kiezer kiezer) 
    {
        System.out.print("Voer de naam in van de kandidaat waarop u wilt stemmen: ");
        Scanner scanner = new Scanner(System.in);
        String kandidaat = scanner.nextLine();
        registreerStem(kandidaat, kiezer);
    }
}
