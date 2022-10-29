package java_thread;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Apply {
    //private 
	static int identified_client=0;
    public static void run_application() throws IOException {
    	
        System.out.println("Bienvenue sur l'application client-serveur officiel !\n\nQue voulez vous faire ?" +
                "\n\n1 - Inscrire un nouveau client\n2 - Envoyer un message\n3 - Voir les autres clients" +
                "\n4 - Consulter mes messages !\n Pour quitter le programme "
                + "appuyer sur une touche au hasard qui soit différent des numéros cités");
        int reponse=0;
        Scanner sc=new Scanner(System.in);
        reponse=sc.nextInt();

        InetAddress ip_client=InetAddress.getLocalHost();

        switch(reponse){
            case 1:
                String pseudo;
                Client customer;
                System.out.println("Veuillez entrer le nouveau pseudo de ce client");
                pseudo=sc.next();


                String string_ip_client=(String) ip_client.getHostAddress();
                //Socket socket_client=new Socket(string_ip_client,4);//On lui donne le port client.
                customer=new Client(pseudo, identified_client,"Thread"+identified_client);

                System.out.println(customer.getPseudo()+" est enregistré en tant que nouveau client et à le numéro d'identifiant unique "+ customer.getId_customer()+" ! ");
                //On ajoute le nouveau client.

                Client.getListe_cliente_official().add(customer);
                identified_client = identified_client +1;
                //thrd_create( thrd_t *thread, thrd_start_t startFunction, void * data );//création d'un thread
                break;

            case 2:
                System.out.println("Entrez votre identifiant : ");
                identified_client =sc.nextInt();
                String nom_client = "";
                int indice_client=0;
                //server.addr_server
                for(int i=0; i<Client.getListe_cliente_official().size();i++){
                    if(identified_client ==Client.getListe_cliente_official().get(i).getId_customer()){//C'est bon'
                        nom_client=Client.getListe_cliente_official().get(i).getPseudo();
                        indice_client=i;
                        i=Client.getListe_cliente_official().size()-1;//On sort de la boucle for

                    }else if(identified_client !=i && i==Client.getListe_cliente_official().size()-1){
                        System.err.println("Identifiant non trouvé !");
                    }
                }
                System.out.println("Bienvenue "+ nom_client + " !");
                //Faire connexion au serveur.
                System.out.println("Veuillez entrez votre message !");
                String message;
                message=sc.next();

                String numeros_clients;
                System.out.println("À qui voulez vous envoyer le message ? (Identifiant client)\nVoici la liste !\n"+Client.customers_list());
                
                System.out.println("Séparer les numéros du(es) client(s) auquel vous voulez envoyer avec un espace !");
                numeros_clients=sc.next();
                //int indice_chaine_numero_client=0;
                List<String>  numeros_destinataires=new ArrayList<String>();

                String num_a_inserer="";
                for (int i=0; i<numeros_clients.length();i++) {
                    if (numeros_clients.charAt(i)!=' '){
                        num_a_inserer=num_a_inserer+numeros_clients.charAt(i);
                    }
                    else{
                        numeros_destinataires.add(num_a_inserer);
                    }
                }
                Client.getListe_cliente_official().get(indice_client).connexion_server(6942);//C'est notre client on le fait connecter au serveur
                /*while (numeros_clients.charAt(indice_chaine_numero_client)!='\0'){
                    if(numeros_clients.charAt(indice_chaine_numero_client)==' '){

                    }
                    indice_chaine_numero_client++;
                }*/

                //thrd_start_t commencement_thread;//Ceci désigne ceux par quoi le thread nouvellement créer va se charger de faire. Cette variable est généralement affectée à une fonctionne
                //thrd_create( &liste_threads[indice_thread] , commencement_thread  ,(client *) &customer); //Passge d'un pointeur à une structure. Association du thread pour notre client

                break;
            case 3:
                //Voir les autre clients.
            	System.out.println(Client.customers_list());
                
                break;
            case 4: //Consulter mes messages
                //System.exit(1);
                break;
            default: //Quitter l'application
                System.exit(1);

                break;
        }

        String recommencer="";

        System.out.println("Voulez-vous recommencez ? [Oui/O/o/Yes/Y/y/oui/yes] - [Non/N/No/n/non/no/n]");
        recommencer=sc.next();

        if(recommencer.equals("Oui")|| recommencer.equals("O")  ||  recommencer.equals("o") || recommencer.equals("oui") || recommencer.equals("Yes")  || recommencer.equals("Y")  || recommencer.equals("y")  || recommencer.equals("yes")){
            run_application();
        }else if(recommencer.equals("Non")  || recommencer.equals("N")  || recommencer.equals("No")  || recommencer.equals("n") || recommencer.equals("non")  || recommencer.equals("no") || recommencer.equals("n") ){
            System.exit(1);
        }
    }

    public static void main(String[] args) throws IOException {
        run_application();
    }

}