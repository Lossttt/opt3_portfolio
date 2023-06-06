package code_zonder_codesmells;

import java.util.*;


public class GodCode 
{
    public static void main(String[] args) 
    {
        VerkiezingSysteemFactory factory = new VerkiezingSysteemFactoryImpl();
        VerkiezingSysteem verkiezingssysteem = factory.createVerkiezingSysteem();
        StemMachine stemmachine = factory.createStemMachine();

        verkiezingssysteem.uitvoeren();

        stemmachine.stemUitbrengen();

        int stemmen = stemmachine.getAantalStemmen();
        System.out.println("Aantal stemmen: " + stemmen);
    }
}
