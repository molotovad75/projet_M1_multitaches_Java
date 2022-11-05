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
	private static boolean envoi_DM_privee=false;
	private static ClientHandler clienthandler_for_sending;
	private static String message_to_send_DM,client_name_sending_DM;
	private static Scanner scanner;
	

	public Client(Socket socket,String username, int id_client) {
		try{
			this.socket=socket;
			this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username=username;
			Client.id_client=id_client;
		}catch(IOException e) {
			closeEverything(socket,bufferedReader,bufferedWriter);
		}
	}
	
	public void sendMessage() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			scanner=new Scanner(System.in);
			
			while(socket.isConnected()) {
				String messageToSend=scanner.nextLine();//Ce que le client va écrire dans le chat
				String client_name="";
				boolean message_privee=false;
				for(int t=0;t<messageToSend.length();t++){
					
					if(messageToSend.charAt(t)=='@' ){
						message_privee=true;
						/*for(int p=t+1;p<messageToSend.length();p++) {
							if(messageToSend.charAt(p)!=' ' || messageToSend.charAt(p)!='@'){
								client_name=client_name+messageToSend.charAt(p); // On rempli la chaine de caractères
							}
							else if(messageToSend.charAt(p)==' ') {
								p=messageToSend.length()+3;//On sort de cette petite boucle for
								
							}
						}*/
						int p=t+1;
						while(p<messageToSend.length()) {
							if(messageToSend.charAt(p)!=' ' || messageToSend.charAt(p)!='@'){
								client_name=client_name+messageToSend.charAt(p); // On rempli la chaine de caractères
								if(client_name.charAt(client_name.length()-1)==' ') {
									p=messageToSend.length()+1;
									break;
								}
							}
							else if(messageToSend.charAt(p)==' ') {
								p=messageToSend.length()+1;
								break;//On sort de cette petite boucle for
							}
							p++;
						}
						
						System.out.println(client_name);
						
						/*bufferedWriter.write(username+ " a écrit : " +messageToSend);
						bufferedWriter.newLine();
						bufferedWriter.flush();*/
						int i=0;
						for (ClientHandler clientHandler:ClientHandler.clientHandlers) {
							if(clientHandler.getClientUsername().equals(client_name)){
								envoi_DM_privee=true;
								clienthandler_for_sending=clientHandler;
								message_to_send_DM=messageToSend;
								client_name_sending_DM=client_name;
								bufferedWriter.write(username+ " a écrit : " +messageToSend);
								bufferedWriter.newLine();
								bufferedWriter.flush();
								//send_DM_client(clientHandler);
								
							}
							else if(!clientHandler.getClientUsername().equals(client_name)){
								i++;
							}
							else if(ClientHandler.clientHandlers.size()<=i){
								bufferedWriter.write("Client introuvable ! ");
								bufferedWriter.newLine();
								bufferedWriter.flush();
							}
						}//Fin for each
					}
				}//Fin boucle for sur tout le message.
					
				
				if(message_privee==false) {
					envoi_DM_privee=false;
					bufferedWriter.write(username+ " a écrit : " +messageToSend);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}				
			}//Fin boucle while socket ouverte
			
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
	
	public static void send_DM_client(ClientHandler client_handler) {
		try {
			client_handler.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean get_envoi_DM_privee() {
		return envoi_DM_privee;
	}
	
	public static ClientHandler get_clienthandler_for_sending() {
		return clienthandler_for_sending;
	}
	
	public static String get_message_to_send_DM() {
		return message_to_send_DM;
	}
	
	public static String get_client_name_sending_DM() {
		return client_name_sending_DM;
	}
	
	
	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		Client.id_client = id_client;
	}

	public static void main(String[] args) throws IOException{
		scanner = new Scanner(System.in);
		System.out.println("Bienvenue sur l'application client-serveur officiel !\nVeuillez entrer votre pseudo !");
		String username=scanner.nextLine();
		Socket socket=new Socket("localhost",1234);
		Client client=new Client(socket,username,id_client);
		client.listenForMessage();
		client.sendMessage();
		id_client++;
	}

}


