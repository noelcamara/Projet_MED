
public class Requete{
    private int id;
    private double dateEntreeI;
    private double dateEntreeC;
    private double dateSortie;

    public Requete(int id,double dateEntreeI,double dateEntreeC,double dateSortie) {
        this.id = id;
        this.dateEntreeI = dateEntreeI;
        this.dateEntreeC = dateEntreeC;
        this.dateSortie = dateSortie;
    }
    /**
     * Getteurs
     */

    public int getId() {
        return this.id;
    }

    public double getDateEntreeI() {
        return this.dateEntreeI;
    }

    public double getDateEntreeC(){
        return this.dateEntreeC;
    }

    public double getDateSortie() {
        return this.dateSortie;
    }



    /**
     * Setteurs
     */
    public void setId(int id){
        this.id=id;
    }

    public void setDateEntreeI(double a){
        this.dateEntreeI=a;
    }
    public void setDateEntreeC(double b){
        this.dateEntreeC=b;
    }
    public void setDateSortie(double c){
        this.dateSortie=c;
    }








}