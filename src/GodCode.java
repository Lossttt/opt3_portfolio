import java.util.*;

abstract class VerkiezingssysteemFactory {
    public abstract Verkiezingssysteem createVerkiezingssysteem();

    public abstract Stemmachine createStemmachine();
}

class VerkiezingssysteemFactoryImpl extends VerkiezingssysteemFactory {
    public Verkiezingssysteem createVerkiezingssysteem() {
        return new VerkiezingssysteemImpl();
    }

    public Stemmachine createStemmachine() {
        return new StemmachineImpl();
    }
}

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

class Kiezer {
    private String naam;

    public Kiezer(String naam) {
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }
}

abstract class Verkiezingssysteem {
    private Kandidaten kandidaten;
    private Map<String, Integer> stemmenPerKandidaat;
    private Set<Kiezer> gestemdeKiezers;

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

    protected void registreerStem(String kandidaat, Kiezer kiezer) {
        if (kandidaten.bevatKandidaat(kandidaat) && !gestemdeKiezers.contains(kiezer)) {
            int stemmen = stemmenPerKandidaat.get(kandidaat);
            stemmenPerKandidaat.put(kandidaat, stemmen + 1);
            System.out.println("Stem voor " + kandidaat + " is geregistreerd voor kiezer: " + kiezer.getNaam());
            gestemdeKiezers.add(kiezer);
        } else if (gestemdeKiezers.contains(kiezer)) {
            System.out.println("Deze kiezer heeft al gestemd.");
        } else {
            System.out.println("Ongeldige kandidaat. Stem niet geregistreerd.");
        }
    }
}

class VerkiezingssysteemImpl extends Verkiezingssysteem {
    protected void stemmen() {
        System.out.println("Stemmen worden verzameld...");
        Scanner scanner = new Scanner(System.in);
        String kiezerNaam = "";
        while (!kiezerNaam.equalsIgnoreCase("stop")) {
            System.out.print("Voer uw naam in om te stemmen (typ 'stop' om te stoppen): ");
            kiezerNaam = scanner.nextLine();
            if (!kiezerNaam.equalsIgnoreCase("stop")) {
                Kiezer kiezer = new Kiezer(kiezerNaam);
                System.out.print("Voer de naam in van de kandidaat waarop u wilt stemmen: ");
                String kandidaat = scanner.nextLine();
                registreerStem(kandidaat, kiezer);
            }
        }
    }
}

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

class StemmachineImpl extends Stemmachine {
    protected Kiezer kiezer;

    protected void registreerStem() {
        stemmen++;
        System.out.println("Stem is geregistreerd voor kiezer: " + kiezer.getNaam());
    }

    protected void verifieerStemgerechtigheid() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Voer uw naam in om uw stemgerechtigdheid te verifiëren: ");
        String kiezerNaam = scanner.nextLine();
        kiezer = new Kiezer(kiezerNaam);
        System.out.println("Verifieer stemgerechtigdheid van de kiezer: " + kiezer.getNaam());
    }
}

public class Beste {
    public static void main(String[] args) {
        VerkiezingssysteemFactory factory = new VerkiezingssysteemFactoryImpl();
        Verkiezingssysteem verkiezingssysteem = factory.createVerkiezingssysteem();
        Stemmachine stemmachine = factory.createStemmachine();

        verkiezingssysteem.uitvoeren();

        stemmachine.stemUitbrengen();

        int stemmen = stemmachine.getAantalStemmen();
        System.out.println("Aantal stemmen: " + stemmen);
    }
}
