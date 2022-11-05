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

	//Message privées pour un autre client.=

	private String clientUsername;
	//private int id_client;

	public ClientHandler(Socket socket) {
		try {
			this.socket=socket;

			this.bufferedWritter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername=bufferedReader.readLine();
			//this.id_client= bufferedReader.readLine();

			clientHandlers.add(this);
			broadcastMessage("Serveur : "+clientUsername+ " est apparu dans la messagerie !");
		
		}catch(IOException e) {
			closeEverything(socket, bufferedReader,bufferedWritter);
			
		}
	}
	
	@Override
	public void run() {
		String messageFromClient;
		String nom_client_DM;
		while(socket.isConnected()) {
			try {
				messageFromClient=bufferedReader.readLine();
				nom_client_DM=bufferedReader.readLine();
				
				if(Client.get_envoi_DM_privee()==false) {
					broadcastMessage(messageFromClient);
				}else if(Client.get_envoi_DM_privee()==true) {
					//broadcastMessage_client_DM(Client.get_message_to_send_DM(),Client.get_client_name_sending_DM());
					broadcastMessage_client_DM(messageFromClient,nom_client_DM);
				}

				
			}catch(IOException e) {
				closeEverything(socket,bufferedReader,bufferedWritter);
				break;
				
			}
		}
		
	}
	
	
	public void broadcastMessage(String messageToSend) {
		for(ClientHandler clientHandler:clientHandlers) {
			try {
				if(!clientHandler.clientUsername.equals(clientUsername)) {//Qu'on envoie le message au même client.
					clientHandler.bufferedWritter.write(messageToSend);
					clientHandler.bufferedWritter.newLine();
					clientHandler.bufferedWritter.flush();
				}
			}catch(IOException e) {
				closeEverything(socket,bufferedReader,bufferedWritter);
			}
		}
	}
	
	public void broadcastMessage_client_DM(String messageTosend,String username) {
		for(ClientHandler clientHandler:clientHandlers) {
			if(clientHandler.clientUsername.equals(username) && !clientHandler.clientUsername.equals(clientUsername)) {
				try{
					clientHandler.bufferedWritter.write(messageTosend);
					clientHandler.bufferedWritter.newLine();
					clientHandler.bufferedWritter.flush();
				}catch (IOException e){
					closeEverything(socket,bufferedReader,bufferedWritter);
				}

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

	public String getClientUsername() {
		return clientUsername;
	}
	
}
