package java_thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client{
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	private static int id_client=0;
	private static boolean envoi_DM_privee=false;
	private static GestionnaireClient clienthandler_for_sending;
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
				Date date=new Date(); 
				DateFormat dateformat=DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
					
				envoi_DM_privee=false;
				bufferedWriter.write(username+ " a écrit : "+dateformat.format(date)+" " +messageToSend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
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
	
	public static void send_DM_client(GestionnaireClient client_handler) {
		try {
			client_handler.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean get_envoi_DM_privee() {
		return envoi_DM_privee;
	}
	
	public static GestionnaireClient get_clienthandler_for_sending() {
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


