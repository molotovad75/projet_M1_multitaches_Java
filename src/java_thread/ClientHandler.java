package java_thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


//Cette classe est mon gestionnaire de client.
public class ClientHandler implements Runnable {
	public static ArrayList<ClientHandler> clientHandlers=new ArrayList<ClientHandler>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWritter;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket=socket;
			this.bufferedWritter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername=bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("Serveur : "+clientUsername+" est apparu dans la messagerie !");
		
		}catch(IOException e) {
			closeEverything(socket, bufferedReader,bufferedWritter);
			
		}
	}
	
	@Override
	public void run() {
		String messageFromClient;
		
		while(socket.isConnected()) {
			try {
				messageFromClient=bufferedReader.readLine();
				broadcastMessage(messageFromClient);
				
				
			}catch(IOException e) {
				closeEverything(socket,bufferedReader,bufferedWritter);
				break;
				
			}
		}
		
	}
	
	
	public void broadcastMessage(String messageToSend) {
		for(ClientHandler clientHandler:clientHandlers) {
			try {
				if(!clientHandler.clientUsername.equals(clientUsername)) {
					clientHandler.bufferedWritter.write(messageToSend);
					clientHandler.bufferedWritter.newLine();
					clientHandler.bufferedWritter.flush();
				}
			}catch(IOException e) {
				closeEverything(socket,bufferedReader,bufferedWritter);
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadcastMessage("Serveur : "+ clientUsername+" a quitté la messagerie!");
		
	}
	
	public void closeEverything(Socket socket, BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
		removeClientHandler();
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
	
}
