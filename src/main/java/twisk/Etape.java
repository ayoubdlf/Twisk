package twisk;

import java.util.Iterator;

public abstract class Etape implements Iterable<Etape> {

    private String nom;

    public Etape(String nom) {
        assert(nom != null) : "Le nom de l'etape ne doit pas etre nulle";
        this.nom = nom;
    }

   public void ajouterSuccesseur(Etape etape) {
        assert(etape != null) : "L'etape ne doit pas etre nulle";
   }

   public abstract boolean estUneActivite();

    public abstract boolean estUnGuichet();

    public Iterator<Etape> iterator() {
        return null;
    }

}
