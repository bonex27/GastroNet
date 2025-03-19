package domainModel;

import java.util.List;

public class Ordine {

    private Person customer;
    private List<Prodotti> ordini;

    public Ordine(Person customer, List<Prodotti> ordini) {

    }

    public int GetCostoTotale(){
        int tot=0;
        for(Prodotti p : ordini){
            tot+= p.getCosto();
        }
        return tot;
    }
}
