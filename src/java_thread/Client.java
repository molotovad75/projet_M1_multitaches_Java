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
	private int id_client;
	
	public Client(Socket socket,String username) {
		try{
			this.socket=socket;
			this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username=username;
			//this.id_client=id_client;
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
				bufferedWriter.write(username+ " a écrit : " +messageToSend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
			
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
		int id_client=0;
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenue sur l'application client-serveur officiel !\nVeuillez entrer votre pseudo !");
		String username=sc.nextLine();
		Socket socket=new Socket("localhost",1234);
		Client client=new Client(socket,username);
		client.listenForMessage();
		client.sendMessage();
		id_client++;
	}

}


