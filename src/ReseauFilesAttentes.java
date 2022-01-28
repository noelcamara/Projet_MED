
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;




public class ReseauFilesAttentes {
    private int duree;
    private double Temps;//Le temps de service de coordinateur
    private  ArrayList<Requete> envoyerAuCoordinateur;

    
    private  ArrayList<Serveur> server;
    private  ArrayList<Requete> sortie;
    private int[] arrives;
    private int[] sorties;

       /**
         * Constructeur de la classe ReseauFilesAttentes
         */
 
    public ReseauFilesAttentes(int duree,double Temps,ArrayList<Serveur> server){
        this.duree = duree;
        this.Temps=Temps;
        this.envoyerAuCoordinateur = new ArrayList<>();
        this.server=server;
        this.sortie = new ArrayList<>();
    }

       /**
         * Getteurs
         */
    public int getDuree() {
        return duree;
    }

    public ArrayList<Requete> getEnvoyerAuCoordinateur(){
        return envoyerAuCoordinateur;
    }

    public ArrayList<Requete> getSortie(){
        return sortie;
    }

        /**
         * findX méthode qui génère une valeur suivant la loi de poisson pour alimenter le coordinateur.
         */
    public double findX(double lambda) {
        double r = Math.random();
        return -Math.log(1-r)/lambda;
    }
        /**
         * addRequeteC méthode qui permet d'ajoputer les requêtes au coordinateur
         */
  
    public void addRequeteC(Requete r){
        this.getEnvoyerAuCoordinateur().add(r);
    }
        /**
         * Les donnees de coordinateur
         */
    
    public void coordinateurFA(double lambda){
        double x=0;
        int y=0;
        double dateEntreeI = 0;
        double dateEntreeC = 0;
        double dateSortie = 0;
        while (x < duree)
        {
            x=x+findX(lambda);
            if(x>duree) break;
            dateEntreeI = x;
            dateEntreeC = x;

            if(y==0){
                dateSortie = dateEntreeI+Temps;

            }else{
                if(dateSortie<dateEntreeI)
                    dateSortie = dateEntreeI+Temps;
                else
                    dateSortie = dateSortie+Temps;
            }
            addRequeteC(new Requete(y,dateEntreeI,dateEntreeC,dateSortie));
            y++;
        }
    }
        /**
         * triRequetes méthode qui permet de trier les requêtes du tableau coordinateur
         */
    public void triRequetes(){
        double dateEntreeC;
        int id1;
        double dateEntreeI1;
        double dateEntreeC1;
        double dateSortie1;
        for (int j = 0; j < this.getEnvoyerAuCoordinateur().size(); j++)
            for (int i = 0; i < this.getEnvoyerAuCoordinateur().size()-1; i++) {
                dateEntreeC = this.getEnvoyerAuCoordinateur().get(i).getDateEntreeC();
                id1 = this.getEnvoyerAuCoordinateur().get(i+1).getId();
                dateEntreeI1  = this.getEnvoyerAuCoordinateur().get(i+1).getDateEntreeI();
                dateEntreeC1 = this.getEnvoyerAuCoordinateur().get(i+1).getDateEntreeC();
                dateSortie1 = this.getEnvoyerAuCoordinateur().get(i+1).getDateSortie();
                
                if (dateEntreeC1 < dateEntreeC) {
                    this.getEnvoyerAuCoordinateur().remove(i+1);
                    this.getEnvoyerAuCoordinateur().add(i,new Requete(id1,dateEntreeI1,dateEntreeC1,dateSortie1));
                }
            }
    }
    
        /**
         * triSortie méthode qui permet de trier le tableau id de la requête
         */

    public void triSortie(){
        int id;
        int id1;
        double dateEntreeI1;
        double dateEntreeC1;
        double dateSortie1;
        for (int j = 0; j < this.getSortie().size(); j++)
            for (int i = 0; i < this.getSortie().size()-1; i++) {
                id = this.getSortie().get(i).getId();
                id1 = this.getSortie().get(i+1).getId();
                dateEntreeI1  = this.getSortie().get(i+1).getDateEntreeI();
                dateEntreeC1 = this.getSortie().get(i+1).getDateEntreeC();
                dateSortie1 = this.getSortie().get(i+1).getDateSortie();
              
                if (id1 < id) {
                    this.getSortie().remove(i+1);
                    this.getSortie().add(i,new Requete(id1,dateEntreeI1,dateEntreeC1,dateSortie1));
                }
            }
    }



        /**
         * TempsTraitementR méthode qui permet de réceptionner et de traiter les requêtes arrivant sur le serveur.
         */
    public String[][] TempsTraitementR(ArrayList<Requete> req){
        String[][] tab=new String[req.size()][2];
        for(int i=0;i< req.size();i++){
            tab[i][0]= String.valueOf(i+1);
            tab[i][1]= String.valueOf(req.get(i).getDateSortie()-req.get(i).getDateEntreeI());
        }
        return tab;
    }


    public double TempsMoyen(){
        double moyen = 0;
        for(int i=0;i<this.getSortie().size();i++){
            moyen += this.getSortie().get(i).getDateSortie() - this.getSortie().get(i).getDateEntreeI();
        }
        return moyen/this.getSortie().size();
    }


       /**
         * simulation méthode principale qui oriente les requêtes vers les serveurs et qui effectue 
         *également la principale partie de la simulation.
         */

    public void simulation(){
        double r;
        double r1;

        arrives=new int[this.server.size()];
        sorties=new int[this.server.size()];

        Requete requete=new Requete(0,0,0,0);

        int nb = 0;

        for(int i=0;i<server.size();i++){
            this.arrives[i]=0;
        }
        for(int i=0;i<server.size();i++){
            this.sorties[i]=0;
        }

// On traite les probabilités par ordre croissant

        for(int i=0;i<this.server.size();i++){
            for(int j=1;j<=i;j++){
                if(this.server.get(j).getQ()>this.server.get(i).getQ()){
                    Serveur serv=this.server.get(i);
                    this.server.set(i,this.server.get(j));
                    this.server.set(j,serv);
                }
            }
        }

        while((this.getEnvoyerAuCoordinateur().size()!=0)&&(this.getEnvoyerAuCoordinateur().get(0).getDateEntreeC()<this.getDuree())){

            nb++;
            r = Math.random();

            double valueQ=this.server.get(0).getQ();//On initialise la valeur de Q pour le traitement

            for(int i=0;i<this.server.size();i++){

            if(r<=valueQ){
                this.server.get(i).addRequete(this.getEnvoyerAuCoordinateur().get(0));
                this.arrives[i]++;
                nb++;
                if(requete.getDateEntreeC()==0){ //la condition n'a pas de requete précedente
                    requete.setId(this.getEnvoyerAuCoordinateur().get(0).getId());
                    requete.setDateEntreeI(this.getEnvoyerAuCoordinateur().get(0).getDateEntreeI());
                    requete.setDateEntreeC(this.server.get(i).getServeur().get(0).getDateSortie());
                    requete.setDateSortie(requete.getDateEntreeC() + this.server.get(i).getTemps());

                }else{//la condition a la requete précedente
                    if (this.server.get(i).getServeur().get(0).getDateSortie() < requete.getDateSortie()){
                        requete.setId(this.getEnvoyerAuCoordinateur().get(0).getId());
                        requete.setDateEntreeI(this.getEnvoyerAuCoordinateur().get(0).getDateEntreeI());
                        requete.setDateEntreeC(requete.getDateSortie());
                        requete.setDateSortie( requete.getDateEntreeC()+ this.server.get(i).getTemps());
                    }else{
                        requete.setId(this.getEnvoyerAuCoordinateur().get(0).getId());
                        requete.setDateEntreeI(this.getEnvoyerAuCoordinateur().get(0).getDateEntreeI());
                        requete.setDateEntreeC(this.server.get(i).getServeur().get(0).getDateSortie());
                        requete.setDateSortie(requete.getDateEntreeC() +this.server.get(i).getTemps());
                    }
                }
                this.server.get(i).getServeur().get(0).setDateEntreeC(requete.getDateEntreeC());
                this.server.get(i).getServeur().get(0).setDateSortie(requete.getDateSortie());
                this.getEnvoyerAuCoordinateur().remove(0);

                
                r1 = Math.random();

                if(this.server.get(i).getP()==0){
                    sortie.add(requete);
                    if(requete.getDateSortie()<=this.getDuree()){
                    this.sorties[i]++;
                    }
                }else{
                    if(r1<=this.server.get(i).getP()){
                        this.getEnvoyerAuCoordinateur().add(requete);
                        this.triRequetes();
                    }else{
                        sortie.add(requete);
                        if(requete.getDateSortie()<=this.getDuree()){
                            this.sorties[i]++;
                        }
                    }
                }

                this.server.get(i).getServeur().remove(0);


             }


            }

        }
        System.out.println("Le nombre de requêtes entrées dans le système durant la période de simulation est : "+nb);
        
    }

        /**
         * supprimerSortieRequete méthode qui permet de supprime de la liste chaînées des requêtes en sortie toutes les requê-
         *tes lesquelles le temps de sortie est supérieur à la durée de simulation.
         */
    public void supprimerSortieRequete(){
        for(int i=0;i<this.getSortie().size();i++){
            if(this.getSortie().get(i).getDateSortie()>this.getDuree())
                this.getSortie().remove(i);
        }
    }
        /**
         * analyseRequete analyse la requete du tableau sortie,renvoie null, s'il n'existe pas dans le tableau
         */
    
    public Requete analyseRequete(int id){
        for(int i=0;i<this.getSortie().size();i++) {
            if(this.getSortie().get(i).getId() == id) return this.getSortie().get(i);
        }
        return null;
    }

    //calculer le nombre des requetes dans le systeme à chaque instant
    public String nbRequetesMilliseconde(ArrayList<Requete> r) {
        int nb;
        String contenu = "";
        Requete requete;
        for (int i = 1; i <= this.getDuree(); i++) {
            nb = 0;
            for (int j = 0; j < r.size(); j++) {//j id de la requete)
                requete = this.analyseRequete(j);
                if(requete != null){
                    if (requete.getDateEntreeI() < i&&requete.getDateSortie() > i) nb++;
                }else{
                    if (r.get(j).getDateEntreeI() < i) nb++;
                }
            }
            contenu += i + " " + nb + "\n";
        }
        return contenu;
    }


        /**
         * generateData permet d'enregistre les données générées dans un fichier.
         */

    public void generateData(String filename, String partieNom, String contenu) {
        try {
            String filepath = System.getProperty("user.dir") + File.separator + "Data/" + filename
                    + String.valueOf(partieNom) + ".dat";
            System.out.println("Ecriture du fichier : " + filepath);
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenu);
            bw.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

  /**
   * Fonction main permettant de tester toutes les fonctions et méthodes demandées
    */

    public static void main(String[] args) {

        //Construction d une liste de serveurs avec paramètres
        ArrayList<Serveur> server = new ArrayList<>(2);
        Serveur server1 = new Serveur(0, 0.5, 0.5, 100, new ArrayList<>(1), 0);
        Serveur server2 = new Serveur(1, 0.5, 0.5, 200, new ArrayList<>(1), 0);
        server.add(server1);
        server.add(server2);
        System.out.println();
        ReseauFilesAttentes rfa = new ReseauFilesAttentes(100000, 10, server);
        //valeur de lambda
        rfa.coordinateurFA(0.006);
        System.out.println("Le nombre de requetes presentes dans le système : " + rfa.getEnvoyerAuCoordinateur().size());
        var c = new ArrayList<Requete>();//copie tout les donnees du table coordinateur dans le table c
        for (int i = 0; i < rfa.getEnvoyerAuCoordinateur().size(); i++) {
            c.add(new Requete(rfa.getEnvoyerAuCoordinateur().get(i).getId(), rfa.getEnvoyerAuCoordinateur().get(i).getDateEntreeI(), rfa.getEnvoyerAuCoordinateur().get(i).getDateEntreeC(), rfa.getEnvoyerAuCoordinateur().get(i).getDateSortie()));
        }
        rfa.simulation();
        rfa.supprimerSortieRequete();
        //Le nombre de requetes envoyees par le coordinateur vers chaque serveur

        for (int i = 0; i < server.size(); i++) {
            rfa.generateData("Le nobre de requetes envoyées au serveur numero " + (i + 1) + ")", server.get(i).getP() + " lambda=0.006 ", String.valueOf(rfa.arrives[i]));

        }

        //Le temps de Traitement de chaque requete
        String[][] t = rfa.TempsTraitementR(rfa.sortie);
        StringBuilder contenu = new StringBuilder();
        for (String[] value : t) {
            contenu.append(value[0]).append(" ").append(value[1]).append("\n");
        }

        rfa.generateData("leTempsDeTraitementDeChaqueRequete", " lambda=0.006 ", contenu.toString());

        System.out.println("Le nombre de requêtes sorties du systeme durant la periode de simulation :" + rfa.getSortie().size());

        for (int i = 0; i < server.size(); i++) {
            System.out.println("Le nombre de requetes envoyees au serveur numero " + (i + 1) + " est " + rfa.arrives[i]);
        }

        System.out.println("Nombre de requêtes traitées par chaque serveur :");
        for (int i = 0; i < server.size(); i++) {
            System.out.println("Serveur : " + (i + 1) + " nombre de requêtes traitées (sorties) : " + rfa.sorties[i]);
        }
        System.out.println("Le temps  moyen de Traitement des requetes est: " + rfa.TempsMoyen());
    }
    
}


