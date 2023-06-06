import java.util.*;

// Abstract Factory voor het maken van verschillende soorten verkiezingssystemen
abstract class VerkiezingssysteemFactory {
    public abstract Verkiezingssysteem createVerkiezingssysteem();

    public abstract Stemmachine createStemmachine();
}

// Concrete fabriek die een specifiek verkiezingssysteem en stemmachine maakt
class VerkiezingssysteemFactoryImpl extends VerkiezingssysteemFactory {
    public Verkiezingssysteem createVerkiezingssysteem() {
        return new VerkiezingssysteemImpl();
    }

    public Stemmachine createStemmachine() {
        return new StemmachineImpl();
    }
}

// Klasse voor het beheren van kandidaten in de verkiezing
class Kandidaten {
    private List<String> kandidaten;

    public Kandidaten() {
        this.kandidaten = new ArrayList<>();
    }

    public void voegKandidaatToe(String kandidaat) {
        kandidaten.add(kandidaat);
    }

    public boolean bevatKandidaat(String kandidaat) {
        return kandidaten.contains(kandidaat);
    }

    public int getAantalKandidaten() {
        return kandidaten.size();
    }

    public List<String> getKandidatenLijst() {
        return new ArrayList<>(kandidaten);
    }
}

// Abstracte klasse voor het verkiezingssysteem
abstract class Verkiezingssysteem {
    private Kandidaten kandidaten;
    private Map<String, Integer> stemmenPerKandidaat;
    private Set<String> gestemdeKiezers;

    public void uitvoeren() {
        initialiseren();
        stemmen();
        resultatenBekijken();
    }

    protected void initialiseren() {
        System.out.println("Het verkiezingssysteem wordt geïnitialiseerd...");
        kandidaten = new Kandidaten();
        stemmenPerKandidaat = new HashMap<>();
        gestemdeKiezers = new HashSet<>();
        voegKandidatenToe();
    }

    protected abstract void stemmen();

    protected void resultatenBekijken() {
        System.out.println("De resultaten van de verkiezing worden bekeken:");
        for (Map.Entry<String, Integer> entry : stemmenPerKandidaat.entrySet()) {
            String kandidaat = entry.getKey();
            int stemmen = entry.getValue();
            System.out.println(kandidaat + ": " + stemmen + " stemmen");
        }
    }

    protected void voegKandidatenToe() {
        System.out.println("Voeg kandidaten toe aan de verkiezing:");
        Scanner scanner = new Scanner(System.in);
        String kandidaat = "";
        while (!kandidaat.equalsIgnoreCase("stop")) {
            System.out.print("Voer de naam van de kandidaat in (typ 'stop' om te stoppen): ");
            kandidaat = scanner.nextLine();
            if (!kandidaat.equalsIgnoreCase("stop")) {
                kandidaten.voegKandidaatToe(kandidaat);
                stemmenPerKandidaat.put(kandidaat, 0);
            }
        }
    }

    protected void registreerStem(String kandidaat, String kiezer) {
        if (kandidaten.bevatKandidaat(kandidaat) && !gestemdeKiezers.contains(kiezer)) {
            int stemmen = stemmenPerKandidaat.get(kandidaat);
            stemmenPerKandidaat.put(kandidaat, stemmen + 1);
            System.out.println("Stem voor " + kandidaat + " is geregistreerd!");
            gestemdeKiezers.add(kiezer);
        } else if (gestemdeKiezers.contains(kiezer)) {
            System.out.println("Deze kiezer heeft al gestemd.");
        } else {
            System.out.println("Ongeldige kandidaat. Stem niet geregistreerd.");
        }
    }
}

// Concrete klasse voor het verkiezingssysteem
class VerkiezingssysteemImpl extends Verkiezingssysteem {
    protected void stemmen() {
        System.out.println("Stemmen worden verzameld...");
        Scanner scanner = new Scanner(System.in);
        String kiezer = "";
        while (!kiezer.equalsIgnoreCase("stop")) {
            System.out.print("Voer uw naam in om te stemmen (typ 'stop' om te stoppen): ");
            kiezer = scanner.nextLine();
            if (!kiezer.equalsIgnoreCase("stop")) {
                System.out.print("Voer de naam in van de kandidaat waarop u wilt stemmen: ");
                String kandidaat = scanner.nextLine();
                registreerStem(kandidaat, kiezer);
            }
        }
    }
}

// Abstracte klasse voor de stemmachine
abstract class Stemmachine {
    protected int stemmen;

    public void stemUitbrengen() {
        verifieerStemgerechtigheid();
        registreerStem();
    }

    protected void verifieerStemgerechtigheid() {
        System.out.println("Verifieer stemgerechtigdheid van de kiezer...");
    }

    protected abstract void registreerStem();

    public int getAantalStemmen() {
        return stemmen;
    }
}

// Concrete klasse voor de stemmachine
class StemmachineImpl extends Stemmachine {
    protected void registreerStem() {
        stemmen++;
        System.out.println("Stem is geregistreerd!");
    }
}

// Hoofdprogramma
public class MainGoedeCode {
    public static void main(String[] args) {
        // Gebruik de fabriek om het verkiezingssysteem en de stemmachine te maken
        VerkiezingssysteemFactory factory = new VerkiezingssysteemFactoryImpl();
        Verkiezingssysteem verkiezingssysteem = factory.createVerkiezingssysteem();
        Stemmachine stemmachine = factory.createStemmachine();

        // Voer het verkiezingssysteem uit
        verkiezingssysteem.uitvoeren();

        // Kies een kiezer en laat hem/haar stemmen
        stemmachine.stemUitbrengen();

        // Bekijk het aantal stemmen
        int stemmen = stemmachine.getAantalStemmen();
        System.out.println("Aantal stemmen: " + stemmen);
    }
}
