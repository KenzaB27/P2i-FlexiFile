package fr.insalyon.p2i2.javaarduino;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoManager;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FlexiFile {
    
    final String DB_NAME = "flexifile"; //G223_B_BD2
    final String DB_LOGIN = "root";
    final String DB_PW = "";
    final Console console = new Console();
    ArduinoManager arduino;
    private Connection connection;
    public GestionnaireFile gestionnaire ; 
    public GestionnaireStats gestionnaireStat;
    
    public static void main(String[] args)
    {
        try {
            FlexiFile main = new FlexiFile();
            main.setup();
            main.start();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            
        }
        
    }
       


    public void setup() {
        try {
            // Chargement de la classe du driver par le DriverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            // Création d'une connexion sur la base de donnée
            //connection = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/" + DB_NAME, DB_LOGIN, DB_PW);
            connection = DriverManager.getConnection("jdbc:mysql://62.210.182.114:3306/" + DB_NAME, DB_LOGIN, DB_PW);
            System.out.println("Connexion établie...");

        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        // Objet matérialisant la console d'exécution (Affichage Écran / Lecture Clavier)
       

        // Affichage sur la console
        console.log("DÉBUT du programme FlexiFile");

        console.log("TOUS les Ports COM Virtuels:");
        for (String port : ArduinoManager.listVirtualComPorts()) {
            console.log(" - " + port);
        }
        console.log("----");

        // Recherche d'un port disponible (avec une liste d'exceptions si besoin)
        String myPort = ArduinoManager.searchVirtualComPort("COM0", "/dev/tty.usbserial-FTUS8LMO", "COM1", "COM2");

        console.log("CONNEXION au port " + myPort);
        
        arduino = new ArduinoManager(myPort) {
            @Override
            protected void onData(String line) {

                // Cette méthode est appelée AUTOMATIQUEMENT lorsque l'Arduino envoie des données
                // Affichage sur la Console de la ligne transmise par l'Arduino
                console.println("ARDUINO >> " + line);
                try {
                    int syncTime = (int)(System.currentTimeMillis() / 1000L);
                    arduino.write("T"+syncTime);
                } catch (IOException ex) {
                    Logger.getLogger(FlexiFile.class.getName()).log(Level.SEVERE, null, ex);
                }
                String[] splitted = line.split(",");
                //insertion de la mesure en BD
                try 
                {
                    int idCapteur = Integer.parseInt(splitted[0]);
                    double valeur = Double.parseDouble(splitted[1]);
                    
                    java.sql.Timestamp times = new java.sql.Timestamp(System.currentTimeMillis());

                    insertMeasures(idCapteur, times, valeur);
                    

                    //calcul longueur file une fois le système initialisé
                    if (gestionnaire.isSetup())
                    {
                        for (Groupe grp: gestionnaire.getListeGroupe()){

                            int idGroupe = grp.getId(idCapteur);
                            if (idGroupe > -1){
                                 insertIntoFile(grp, idCapteur, times);  
                            }
                        }
                        
                    }
                    
                }
                catch (NumberFormatException e)
                {
                    
                }
            }     
        };
   
        gestionnaire = new GestionnaireFile (new Client ("DDR_INSA"), connection );
        gestionnaire.setup();
    }
            
    
    public void start (){
        // Objet matérialisant la console d'exécution (Affichage Écran / Lecture Clavier)

        try {

            console.log("DÉMARRAGE de la connexion");
            // Connexion à l'Arduino
            arduino.start();
            gestionnaire.start();
            for (Groupe grp: gestionnaire.getListeGroupe()){
                gestionnaireStat = new GestionnaireStats (grp.getIdGroupe(), connection);
                gestionnaireStat.calcul();
            } 
            console.log("BOUCLE infinie en attente du Clavier");
            // Boucle d'ecriture sur l'arduino (execution concurrente au thread)
            boolean exit = false;

            while (!exit) {

                // Lecture Clavier de la ligne saisie par l'Utilisateur
                String line = console.readLine("Envoyer une ligne (ou 'stop') > ");

                if (line.length() != 0) {

                    // Affichage sur l'écran
                    console.log("CLAVIER >> " + line);

                    // Test de sortie de boucle
                    exit = line.equalsIgnoreCase("stop");

                    if (!exit) {
                        // Envoi sur l'Arduino du texte saisi au Clavier
                        arduino.write(line);
                    }
                }
            }

            console.log("ARRÊT de la connexion");
            // Fin de la connexion à l'Arduino
            arduino.stop();

        } catch (IOException ex) {
            // Si un problème a eu lieu...
            console.log(ex);
        }
    }
    
    public FlexiFile()
    {
    }
 
    
//
// ------ Méthodes utiles  -------
//
    
     
    /** Méthode permettant de récupérer la longueur de la file d'attente d'un groupe passée en paramètre
    *  @param grp: groupe de capteurs
    * @return retourne la longeur de la file d'attente à un instant t  */
    public int getLength(Groupe grp){ 
        int idGroupe = grp.getIdGroupe();
        int longueur=0;
        int offset; 
           
        try {

            String query = "SELECT Min(l.pos) as longueur " +
                           "FROM Localisation l, Mesure m , Capteur c " +
                           "WHERE c.idGroupe = ? " +
                           "AND m.idCapteur= c.idCapteur " +
                           "AND l.idCapteur = c.idCapteur " +
                           "AND TIME_TO_SEC(TIMEDIFF(now(), m.dateMesure)) between 0 and 60 " +
                           "AND m.valeur >= l.distanceX";
            
            String q = "SELECT Max(l.pos) as offset "
                    +"FROM Localisation l, Capteur c "
                    +"WHERE c.idGroupe = ? " 
                    +"AND l.idCapteur = c.idCapteur "
                    +"AND l.pos <?" ;
            // Construction de l'objet « requête parametrée »
            PreparedStatement ps = connection.prepareStatement(query);
            
            // transformation en requête statique
            ps.setInt(1,idGroupe) ; 
            System.out.println(ps);
            //execution de la requete
            ResultSet rs = ps.executeQuery();
            System.out.println("requete executee ....");
            rs.next();
            longueur= rs.getInt("longueur");
            
            ps = connection.prepareStatement(q);
            ps.setInt(1, idGroupe);
            ps.setInt(2, longueur);
            rs= ps.executeQuery();
           
            rs.next();
            offset = rs.getInt("offset");
            
            if (offset<longueur)
                longueur = longueur - (longueur-offset);
            
            System.out.println("longueur : " + longueur);
        }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
            System.exit(0);
        }
        return longueur;
    }
    
    public int getTmpAttente (Groupe grp, int idMesure, int longueur){
        //distanace entre 2 capteurs = 3;
        //densite = 4personne/m²;
        //vitesse =0.05 personne/s;
        int tmpAttente=0;
        try{
            String query = "SELECT ROUND(SUM(l.distanceX *3*4*0.05),2) as TmpAttente " +
                           "FROM Localisation l , Capteur c " +
                           "WHERE l.idCapteur = c.idCapteur " +
                           "AND c.idGroupe=? " +
                           "AND c.idCapteur IN (SELECT c.idCapteur " +
                                                "FROM Localisation l, Capteur c, Groupe g, Mesure m " +
                                                "WHERE g.idGroupe = c.idGroupe " +
                                                "AND m.idMesure=? " +
                                                "AND TIME_TO_SEC(TIMEDIFF(now(),m.dateMesure)) between 0 and 60 " +
                                                "AND l.idCapteur = c.idCapteur " +
                                                "AND l.pos <= ?)";
            // Construction de l'objet « requête parametrée »
                PreparedStatement ps = connection.prepareStatement(query);

                // transformation en requête statique
                ps.setInt(1,grp.getIdGroupe()); 
                ps.setInt(2, idMesure);
                ps.setInt(3, longueur);
                System.out.print(ps);
                //execution de la requete
                ResultSet rs = ps.executeQuery();
                System.out.println("requete executee ....");

                while ( rs.next () ) {
                    tmpAttente = rs.getInt("TmpAttente");
                    System.out.println ("temps d'attente :"+tmpAttente);
                }
            }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            System.out.println(e.getMessage());
            System.exit(0);                      
        }            
        return tmpAttente; 
    }
    
    public void insertMeasures (int idCapteur , java.sql.Timestamp dateMesure , double valeur){
        try {
 
                String sqlStr = "INSERT INTO Mesure(idCapteur, dateMesure, valeur) VALUES (?,?,?)";
                PreparedStatement ps= connection.prepareStatement(sqlStr);

                ps.setInt(1, idCapteur);
                ps.setTimestamp(2, dateMesure);
                ps.setDouble(3, valeur);

                //execution de la requete
             
                ps.executeUpdate();
                System.out.println (ps); 
                
            }
        catch(NumberFormatException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
        } 
        catch (SQLException e) {
            //si une erreur se produit, affichage du message correspondant
           e.printStackTrace();
        }
            
    }
    
    public void insertIntoFile ( Groupe grp , int idCapteur, java.sql.Timestamp times){
        try {
                String sqlStr = "SELECT idMesure from Mesure where dateMesure=? and idCapteur=?";
                
                PreparedStatement ps = connection.prepareStatement(sqlStr);
                ps.setTimestamp(1, times);
                ps.setInt(2, idCapteur);
                ResultSet rs = ps.executeQuery();
                System.out.print(ps);
                int idMesure = 0;
                while ( rs.next () ) {
                   idMesure = rs.getInt("idMesure");
                }
                

                int length = getLength(grp);
                int tmpAttente = getTmpAttente(grp, idMesure, length);
            
                //Creation de la requete
                sqlStr = "INSERT INTO File(longueur, tmpAttente, idGroupe, dateMesure) VALUES (?,?,?,?)";

                ps = connection.prepareStatement(sqlStr);

                ps.setInt(1,length);
                ps.setInt(2,tmpAttente);
                ps.setInt (3,grp.getIdGroupe());
                ps.setTimestamp(4,times);

                //execution de la requete
                ps.executeUpdate();
            }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
        }
    }
    
}

