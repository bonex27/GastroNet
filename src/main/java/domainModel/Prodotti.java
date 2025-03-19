package domainModel;
import java.util.List;

public class Prodotti {

    private int idProdotto;
    private List<Ingredienti> ingredienti;
    private int costo;
    private String descrizione;

    public int getCosto() {
        return costo;
    }
}
