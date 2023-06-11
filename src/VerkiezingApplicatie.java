import java.util.*;
package src.abstracttemplate.without;

interface VerkiezingSysteem 
{
    void uitvoeren(AbstractStemMachine stemmachine);
}

class Kiezer 
{
    private String naam;

    public Kiezer(String naam) 
    {
        this.naam = naam;
    }

    public String getNaam() 
    {
        return naam;
    }
}


class StemInputHandler 
{
    private final KandidatenBeheer kandidaten;
    private final StemProcessor stemProcessor;

    public StemInputHandler(KandidatenBeheer kandidaten, StemProcessor stemProcessor) 
    {
        this.kandidaten = kandidaten;
        this.stemProcessor = stemProcessor;
    }

    // Verwerkt de invoer van de gebruiker en registreert de stem als de kandidaat geldig is
    // Retourneert true als de invoer niet 'exit' is, anders false
    public boolean handleInput(String input, AbstractStemMachine stemmachine) 
    {
        if (input.equalsIgnoreCase("exit")) 
        {
            return false; // Stoppen met de verkiezing
        } else {
            int keuzeIndex = parseInput(input);
            if (keuzeIndex != -1) {
                registerVote(keuzeIndex, stemmachine);
            }
            return true; // Doorgaan met de verkiezing
        }
    }

    private int parseInput(String input) 
    {
        try 
        {
            int keuzeIndex = Integer.parseInt(input) - 1;
            List<String> kandidatenLijst = kandidaten.getKandidatenLijst();
            if (keuzeIndex >= 0 && keuzeIndex < kandidatenLijst.size())
            {
                return keuzeIndex;
            } else {
                System.out.println("Ongeldige kandidaat. Stem niet geregistreerd.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige invoer. Stem niet geregistreerd.");
        }
        return -1;
    }
    
    private void registerVote(int keuzeIndex, AbstractStemMachine stemmachine) 
    {
        String gekozenKandidaat = kandidaten.getKandidatenLijst().get(keuzeIndex);
        Kiezer kiezer = stemmachine.getKiezer();
        stemProcessor.registreerStem(gekozenKandidaat, kiezer);
    }
}

class StemVerzamelaar implements VerkiezingSysteem 
{
    private final KandidatenBeheer kandidaten;
    private final StemProcessor stemProcessor;

    public StemVerzamelaar(KandidatenBeheer kandidaten, StemProcessor stemProcessor) {
        this.kandidaten = kandidaten;
        this.stemProcessor = stemProcessor;
    }

    @Override
    public void uitvoeren(AbstractStemMachine stemmachine) 
    {
        stemProcessor.initialiseerStemmen();
        Scanner scanner = new Scanner(System.in);
        StemInputHandler inputHandler = new StemInputHandler(kandidaten, stemProcessor);
        
        String keuze;
        do 
        {
            System.out.println("Beschikbare kandidaten:");
            List<String> kandidatenLijst = kandidaten.getKandidatenLijst();
            for (int i = 0; i < kandidatenLijst.size(); i++) 
            {
                System.out.println((i + 1) + ". " + kandidatenLijst.get(i));
            }
            System.out.print("Selecteer een kandidaat (of typ 'exit' om te stoppen): ");
            keuze = scanner.nextLine();
        } while (inputHandler.handleInput(keuze, stemmachine));

        stemProcessor.toonResultaten();
    }
}

class StemProcessor {
    private final KandidatenBeheer kandidaten;
    private final StemRegistratie stemRegistratie;
    private final StemResultaten stemResultaten;

    public StemProcessor(KandidatenBeheer kandidaten) {
        this.kandidaten = kandidaten;
        this.stemRegistratie = new StemRegistratie();
        this.stemResultaten = new StemResultaten();
    }

    public void initialiseerStemmen() {
        System.out.println("Het verkiezingssysteem wordt geïnitialiseerd...");
        stemResultaten.initialiseerStemResultaten(kandidaten.getKandidatenLijst());
    }

    public void registreerStem(String kandidaat, Kiezer kiezer) {
        if (kandidaten.bevatKandidaat(kandidaat)) {
            stemRegistratie.registreerStem(kandidaat, kiezer);
            stemResultaten.updateResultaten(stemRegistratie.getStemmenPerKandidaat());
        } else {
            System.out.println("Ongeldige kandidaat. Stem niet geregistreerd.");
        }
    }

    public void toonResultaten() {
        stemResultaten.toonResultaten();
    }
}

class StemRegistratie {
    private final Set<Kiezer> gestemdeKiezers;
    private final Map<String, Integer> stemmenPerKandidaat;

    public StemRegistratie() {
        this.gestemdeKiezers = new HashSet<>();
        this.stemmenPerKandidaat = new HashMap<>();
    }

    public void registreerStem(String kandidaat, Kiezer kiezer) {
        if (!gestemdeKiezers.contains(kiezer)) {
            int stemmen = stemmenPerKandidaat.getOrDefault(kandidaat, 0);
            stemmenPerKandidaat.put(kandidaat, stemmen + 1);
            System.out.println("Stem voor " + kandidaat + " is geregistreerd voor kiezer: " + kiezer.getNaam());
            gestemdeKiezers.add(kiezer);
        } else {
            System.out.println("Deze kiezer heeft al gestemd.");
        }
    }

    public Map<String, Integer> getStemmenPerKandidaat() {
        return stemmenPerKandidaat;
    }
}

class StemResultaten {
    private Map<String, Integer> stemmenPerKandidaat;

    public void initialiseerStemResultaten(List<String> kandidatenLijst) {
        stemmenPerKandidaat = new HashMap<>();
        for (String kandidaat : kandidatenLijst) {
            stemmenPerKandidaat.put(kandidaat, 0);
        }
    }

    public void updateResultaten(Map<String, Integer> stemmenPerKandidaat) {
        this.stemmenPerKandidaat = stemmenPerKandidaat;
    }

    public void toonResultaten() {
        System.out.println("De resultaten van de verkiezing worden bekeken:");
        for (Map.Entry<String, Integer> entry : stemmenPerKandidaat.entrySet()) {
            String kandidaat = entry.getKey();
            int stemmen = entry.getValue();
            System.out.println(kandidaat + ": " + stemmen + " stemmen");
        }
    }
}

class KandidatenBeheer 
{
    private final List<String> kandidaten; // Lijst met kandidaten

    public KandidatenBeheer() 
    {
        this.kandidaten = new ArrayList<>();
    }

    public void voegKandidaatToe(String kandidaat) 
    {
        kandidaten.add(kandidaat); // Kandidaat toevoegen aan de lijst
    }

    public boolean bevatKandidaat(String kandidaat) 
    {
        return kandidaten.contains(kandidaat); // Controleren of de lijst de opgegeven kandidaat bevat
    }

    public List<String> getKandidatenLijst() 
    {
        return new ArrayList<>(kandidaten); // Een kopie van de lijst met kandidaten retourneren
    }
}

abstract class AbstractStemMachine 
{
    protected Kiezer kiezer;

    public void stemUitbrengen() 
    {
        verifieerStemgerechtigheid(); // De stemgerechtigdheid van de kiezer verifiëren
        registreerStem(); // De stem registreren
    }

    protected void verifieerStemgerechtigheid() 
    {
        System.out.println("Verifieer stemgerechtigdheid van de kiezer...");
    }

    protected abstract void registreerStem(); // Abstracte methode om de stem te registreren

    public int getAantalStemmen() 
    {
        return 0; // Het aantal stemmen retourneren (standaardwaarde)
    }

    public Kiezer getKiezer() 
    {
        return kiezer; // De kiezer retourneren
    }
}

class StemGerechtigheidVerifier extends AbstractStemMachine 
{
    @Override
    protected void registreerStem() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Voer uw naam in om uw stemgerechtigdheid te verifiëren: ");
        String kiezerNaam = scanner.nextLine(); // Naam van de kiezer lezen
        kiezer = new Kiezer(kiezerNaam); // Een nieuwe kiezer maken
        System.out.println("Verifieer stemgerechtigdheid van de kiezer: " + kiezer.getNaam());
    }
}

class VerkiezingSysteemFactoryImpl implements VerkiezingSysteemFactory 
{
    public VerkiezingSysteem createVerkiezingSysteem() 
    {
        // Een nieuw KandidatenBeheer-object maken
        KandidatenBeheer kandidatenBeheer = new KandidatenBeheer(); 

        // Een StemVerzamelaar-object maken en retourneren
        return new StemVerzamelaar(kandidatenBeheer, new StemProcessor(kandidatenBeheer)); 
    }

    public AbstractStemMachine createStemMachine() 
    {
        // Een StemGerechtigheidVerifier-object maken en retourneren
        return new StemGerechtigheidVerifier(); 
    }
}

interface VerkiezingSysteemFactory 
{
    VerkiezingSysteem createVerkiezingSysteem();
    AbstractStemMachine createStemMachine();
}

class VerkiezingApplicatie {
    public static void main(String[] args) {
        // Een nieuw KandidatenBeheer-object maken
        KandidatenBeheer kandidatenBeheer = new KandidatenBeheer();
        
        // Kandidaten toevoegen aan het KandidatenBeheer-object
        kandidatenBeheer.voegKandidaatToe("Kandidaat 1");
        kandidatenBeheer.voegKandidaatToe("Kandidaat 2");
        kandidatenBeheer.voegKandidaatToe("Kandidaat 3");

        // Een nieuw StemProcessor-object maken
        StemProcessor stemProcessor = new StemProcessor(kandidatenBeheer);

        // Een nieuw StemVerzamelaar-object maken met het KandidatenBeheer-object en StemProcessor-object
        StemVerzamelaar stemVerzamelaar = new StemVerzamelaar(kandidatenBeheer, stemProcessor);

        // Een nieuw StemGerechtigheidVerifier-object maken
        StemGerechtigheidVerifier stemGerechtigheidVerifier = new StemGerechtigheidVerifier();

        // Een nieuwe Kiezer maken
        Kiezer kiezer = new Kiezer("John Doe");

        // De Kiezer toewijzen aan de StemGerechtigheidVerifier
        stemGerechtigheidVerifier.kiezer = kiezer;

        // De stem uitbrengen met de StemGerechtigheidVerifier
        stemGerechtigheidVerifier.stemUitbrengen();

        // Het VerkiezingSysteem uitvoeren met de StemVerzamelaar
        stemVerzamelaar.uitvoeren(stemGerechtigheidVerifier);
    }
}
