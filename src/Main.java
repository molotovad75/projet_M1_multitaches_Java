import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Bienvenue sur l'application client-serveur officiel !\n\nQue voulez vous faire ?" +
                "\n\n1 - Inscrire un nouveau client\n2 - Envoyer un message\n3 - Voir les autres clients" +
                "\n4 - Quittez l'application !\n");
        int reponse=0;
        Scanner sc=new Scanner(System.in);
        reponse=sc.nextInt();
        int identifiant_client=0;


        InetAddress ip_client=InetAddress.getLocalHost();

        switch(reponse){
            case 1:
                String pseudo;
                Client customer;
                System.out.println("Veuillez entrer le nouveau pseudo de ce client\n");
                pseudo=sc.next();


                String string_ip_client=(String) ip_client.getHostAddress();
                Socket socket_client=new Socket(string_ip_client,4001);//On lui donne le port client.
                customer=new Client(pseudo,identifiant_client, socket_client);
                identifiant_client++;
                System.out.println(customer.getPseudo()+" est enregistré en tant que nouveau client et à le numéro d'identifiant unique "+ customer.getId_customer()+" ! ");
                //On ajoute le nouveau client.

                Client.getListe_cliente_official().add(customer);

                //thrd_create( thrd_t *thread, thrd_start_t startFunction, void * data );//création d'un thread
                break;

            case 2:
                System.out.println("Entrez votre identifiant : ");
                identifiant_client=sc.nextInt();
                String nom_client;
                int indice_client;
                //server.addr_server
                for(int i=0; i<Client.getListe_cliente_official().size();i++){
                    if(identifiant_client==Client.getListe_cliente_official().get(i).getId_customer()){//C'est bon'
                        nom_client=Client.getListe_cliente_official().get(i).getPseudo();
                        indice_client=i;
                        i=Client.getListe_cliente_official().size()-1;//On sort de la boucle for

                    }else if(identifiant_client!=i && i==Client.getListe_cliente_official().size()-1){
                        System.err.println("Identifiant non trouvé !");
                    }
                }
                System.out.println("Bienvenue "+ nom_client + " !");
                //Faire connexion au serveur.
                System.out.println("Veuillez entrez votre message !");
                String message;
                message=sc.next();

                String numeros_clients;
                System.out.println("À qui voulez vous envoyer le message ? (Identifiant client)");
                Client.customers_list();
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
                //printf("%s\n",);
                Client.customers_list();
                break;
            case 4: //Quitter l'application
                System.exit(1);
                break;
            default: //Quitter l'application
                //System.exit(1);

                break;
        }

        char * recommencer=malloc(sizeof(char));
        printf("Voulez-vous recommencez ? [Oui/O/o/Yes/Y/y/oui/yes] - [Non/N/No/n/non/no/n]\n");
        scanf("%s",recommencer);
        if(strcmp(recommencer,"Oui")==0 || strcmp(recommencer,"O")==0 || strcmp(recommencer,"o")==0 || strcmp(recommencer,"oui")==0 || strcmp(recommencer,"Yes")==0 || strcmp(recommencer,"Y")==0 || strcmp(recommencer,"y")==0 || strcmp(recommencer,"yes")==0){
            run_application();
        }else if(strcmp(recommencer,"Non")==0  || strcmp(recommencer,"N")==0  || strcmp(recommencer,"No")==0 || strcmp(recommencer,"n")==0 || strcmp(recommencer,"non")==0 || strcmp(recommencer,"no")==0 || strcmp(recommencer,"n")==0){
            free(list_customer_official);
            free(processus_fils);
            free(liste_threads);
            System.exit(1);
        }
        free(recommencer);



    }


}