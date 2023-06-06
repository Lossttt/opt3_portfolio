package code_zonder_codesmells;

import java.util.ArrayList;
import java.util.List;

public class Kandidaten 
{
    private List<String> kandidaten;

    public Kandidaten() 
    {
        this.kandidaten = new ArrayList<>();
    }

    public void voegKandidaatToe(String kandidaat) 
    {
        kandidaten.add(kandidaat);
    }

    public boolean bevatKandidaat(String kandidaat) 
    {
        return kandidaten.contains(kandidaat);
    }

    public int getAantalKandidaten() 
    {
        return kandidaten.size();
    }

    public List<String> getKandidatenLijst() 
    {
        return new ArrayList<>(kandidaten);
    }
}
