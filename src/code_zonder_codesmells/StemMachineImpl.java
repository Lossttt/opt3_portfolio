package code_zonder_codesmells;

import java.util.Scanner;

public class StemMachineImpl extends StemMachine 
{
    protected Kiezer kiezer;

    @Override
    protected void registreerStem() 
    {
        stemmen++;
        System.out.println("Stem is geregistreerd voor kiezer: " + kiezer.getNaam());
    }

    @Override
    protected void verifieerStemgerechtigheid() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Voer uw naam in om uw stemgerechtigdheid te verifiëren: ");
        String kiezerNaam = scanner.nextLine();
        kiezer = new Kiezer(kiezerNaam);
        System.out.println("Verifieer stemgerechtigdheid van de kiezer: " + kiezer.getNaam());
    }
}
