package code_zonder_codesmells;

public class VerkiezingSysteemFactoryImpl extends VerkiezingSysteemFactory 
{
    @Override
    public VerkiezingSysteem createVerkiezingSysteem() {
        return new VerkiezingSysteemImpl();
    }

    @Override
    public StemMachine createStemMachine() {
        return new StemMachineImpl();
    }
}