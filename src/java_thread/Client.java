package java_thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private static int id_client=0;
	
	public Client(Socket socket,String username, int id_client) {
		try{
			this.socket=socket;
			this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username=username;
			this.id_client=id_client;
		}catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}
	
	public void sendMessage() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner scanner=new Scanner(System.in);
			
			while(socket.isConnected()) {
				String messageToSend=scanner.nextLine();//Ce que le client va écrire dans le chat

				for(int t=0;t<messageToSend.length();t++){

					if(messageToSend.charAt(t)=='@' && messageToSend.charAt(t-1)==' ' && t-1!=0 ){
						for (int i=t+1;i<messageToSend.length();i++) {//boucle for n°2
							if(messageToSend.charAt(i)==' '){ //Si le nom du client est séparé d'un espace
								String client_name="";
								for (int e=t+1;e<i;e++){
									client_name=client_name+messageToSend.charAt(e); // On rempli le tableau
								}
								//Vérifier que le client existe bien dans le gestionnaire
								for (ClientHandler clientHandler:ClientHandler.clientHandlers) {
									if(clientHandler.getClientUsername().equals(client_name)){
										bufferedWriter.write(username+ " a écrit à "+ client_name +" : " +messageToSend);
										bufferedWriter.newLine();
										bufferedWriter.flush();
									}else{
										bufferedWriter.write("Client introuvable ! ");
										bufferedWriter.newLine();
										bufferedWriter.flush();
									}
								}
							}
							i=messageToSend.length()-1; // On sort de la boucle For n°2.
						}
					}
					/*else{	//Il faut trouver un moyen de jerter ce else de la boucle for.
						bufferedWriter.write(username+ " a écrit : " +messageToSend);
						bufferedWriter.newLine();
						bufferedWriter.flush();
					}*/
				}
			}//Fin boucle while
			
		}catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
			
		}
	}


	
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msgFromGroupChat;
				
				while(socket.isConnected()) {
					try {
						msgFromGroupChat=bufferedReader.readLine();
						System.out.println(msgFromGroupChat);
						
					}catch(IOException e) {
						closeEverything(socket,bufferedReader,bufferedWriter);
					}
				}
			}
		}).start();
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if(bufferedReader!=null){
				bufferedReader.close();
			}
			if(bufferedWriter!=null){
				bufferedWriter.close();
			}
			
			if(socket!=null) {
				socket.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}

	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenue sur l'application client-serveur officiel !\nVeuillez entrer votre pseudo !");
		String username=sc.nextLine();
		Socket socket=new Socket("localhost",1234);
		Client client=new Client(socket,username,id_client);
		client.listenForMessage();
		client.sendMessage();
		id_client++;
	}

}


