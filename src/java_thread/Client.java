package java_thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client extends Thread {
    private String pseudo,threadName;
    private int id_customer;
    private InetAddress ip_adress;

    private Socket socketClient;
    
    private static ArrayList<Client> liste_cliente_official=new ArrayList<Client>();

    public Client(String pseudo, int id_customer,String threadName) throws UnknownHostException {
        this.pseudo = pseudo;
        this.id_customer = id_customer;
        this.ip_adress=InetAddress.getLocalHost();
        this.threadName=threadName;
        this.start();
        //this.socketClient = socketClient;

    }

    public String getPseudo() {
        return pseudo;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setIp_adress(InetAddress ip_adress) {
        this.ip_adress = ip_adress;
    }

    public void connexion_server(int port) {
        //serveur.getLocalSocketAddress();

        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Lancement du serveur");

            while (true) {
                this.socketClient = socketServeur.accept();
                String message = "";

                System.out.println("Connexion avec : " + socketClient.getInetAddress());

                // InputStream in = socketClient.getInputStream();
                // OutputStream out = socketClient.getOutputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                PrintStream out = new PrintStream(socketClient.getOutputStream());
                message = in.readLine();
                out.println(message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String customers_list(){ //Dans cette méthode on veut afficher la liste de tous les pseudos sauvegardés ainsi que les id_customers.
        String list_customer="";
        for(int i=0;i<liste_cliente_official.size();i++){
            list_customer=list_customer+"Pseudo client :"+liste_cliente_official.get(i).getPseudo()+"  "+"Identifiant client : "+ liste_cliente_official.get(i).getId_customer()+"\n";
        }
        return list_customer;
    }

    public static ArrayList<Client> getListe_cliente_official() {
        return liste_cliente_official;
    }

    public static void setListe_cliente_official(ArrayList<Client> liste_cliente_official) {
        Client.liste_cliente_official = liste_cliente_official;
    }
}


