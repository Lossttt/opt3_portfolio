package code_zonder_codesmells;

public abstract class StemMachine {
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
