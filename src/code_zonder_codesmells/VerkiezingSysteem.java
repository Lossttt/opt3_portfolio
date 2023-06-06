package code_zonder_codesmells;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public abstract class VerkiezingSysteem 
{
    private Kandidaten kandidaten;
    private Map<String, Integer> stemmenPerKandidaat;
    private Set<Kiezer> gestemdeKiezers;

    public void uitvoeren() 
    {
        initialiseren();
        stemmen();
        resultatenBekijken();
    }

    protected void initialiseren() 
    {
        System.out.println("Het verkiezingssysteem wordt geïnitialiseerd...");
        kandidaten = new Kandidaten();
        stemmenPerKandidaat = new HashMap<>();
        gestemdeKiezers = new HashSet<>();
        voegKandidatenToe();
    }

    protected abstract void stemmen();

    protected void resultatenBekijken() 
    {
        System.out.println("De resultaten van de verkiezing worden bekeken:");
        for (Map.Entry<String, Integer> entry : stemmenPerKandidaat.entrySet()) 
        {
            String kandidaat = entry.getKey();
            int stemmen = entry.getValue();
            System.out.println(kandidaat + ": " + stemmen + " stemmen");
        }
    }

    protected void voegKandidatenToe() 
    {
        System.out.println("Voeg kandidaten toe aan de verkiezing:");
        Scanner scanner = new Scanner(System.in);
        String kandidaat = "";
        while (!kandidaat.equalsIgnoreCase("stop")) 
        {
            System.out.print("Voer de naam van de kandidaat in (typ 'stop' om te stoppen): ");
            kandidaat = scanner.nextLine();
            if (!kandidaat.equalsIgnoreCase("stop")) 
            {
                kandidaten.voegKandidaatToe(kandidaat);
                stemmenPerKandidaat.put(kandidaat, 0);
            }
        }
    }

    protected void registreerStem(String kandidaat, Kiezer kiezer) 
    {
        if (kandidaten.bevatKandidaat(kandidaat) && !gestemdeKiezers.contains(kiezer)) 
        {
            int stemmen = stemmenPerKandidaat.get(kandidaat);
            stemmenPerKandidaat.put(kandidaat, stemmen + 1);
            System.out.println("Stem voor " + kandidaat + " is geregistreerd voor kiezer: " + kiezer.getNaam());
            gestemdeKiezers.add(kiezer);
        } else if (gestemdeKiezers.contains(kiezer)) 
        {
            System.out.println("Deze kiezer heeft al gestemd.");
        } else {
            System.out.println("Ongeldige kandidaat. Stem niet geregistreerd.");
        }
    }
}
