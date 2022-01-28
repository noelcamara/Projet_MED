import java.util.ArrayList;

public class Serveur {

    private int id;
    private double q;
    private double p;
    private double temps;
    private ArrayList<Requete> serveur;
    private int cmp;

    public Serveur(int id,double q,double p,double temps,ArrayList<Requete> serveur,int cmp){

        this.id=id;
        this.q=q;
        this.p=p;
        this.temps=temps;
        this.serveur=serveur;
        this.cmp=cmp;

    }

   /**
         * Getteurs
         */

    public double getQ(){
        return this.q;
    }
    public double getP(){
        return this.p;
    }

    public double getTemps(){
        return this.temps;
    }

    public ArrayList<Requete> getServeur(){
        return serveur;
    }

     /**
         * Setteurs
         */
    public void setQ(double q){
        this.q=q;
    }

    public void addRequete(Requete requete){
        this.serveur.add(requete);
    }

}
