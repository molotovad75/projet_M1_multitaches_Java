package java_thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


//Cette classe est mon gestionnaire de client.
public class GestionnaireClient implements Runnable {
	public static ArrayList<GestionnaireClient> gestionnaire_client=new ArrayList<GestionnaireClient>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWritter;

	//Message privées pour un autre client.=

	private String clientUsername;
	//private int id_client;

	public GestionnaireClient(Socket socket) {
		try {
			this.socket=socket;

			this.bufferedWritter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientUsername=bufferedReader.readLine();
			//this.id_client= bufferedReader.readLine();
			Date date=new Date(); 
			DateFormat dateformat=DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
			
			gestionnaire_client.add(this);
			broadcastMessage("Serveur : "+clientUsername+ " est apparu dans la messagerie le "+ dateformat.format(date)+" !");
		
		}catch(IOException e) {
			closeEverything(socket, bufferedReader,bufferedWritter);
			
		}
	}
	
	@Override
	public void run(){
        String messageFromClient;

        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                boolean privateMessage = false;

                if( messageFromClient.contains("@")){
                    String username;

                    for (GestionnaireClient clientHandler : gestionnaire_client){

                        username = "@" + clientHandler.clientUsername + ":";

                        try{
                            if(messageFromClient.contains(username)){
                                clientHandler.bufferedWritter.write(messageFromClient);
                                clientHandler.bufferedWritter.newLine();
                                clientHandler.bufferedWritter.flush();
                                privateMessage = true;
                            }
                        } catch (IOException e){
                            closeEverything(socket, bufferedReader, bufferedWritter);
                        }
                    }
                }
                if (!privateMessage){
                    broadcastMessage(messageFromClient);
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWritter);
                break;
            }
        }
    }
	
	
	public void broadcastMessage(String messageToSend) {
		for(GestionnaireClient clientHandler:gestionnaire_client) {
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
	
	public void broadcastMessage_client_DM(String messageTosend,String username) {
		for(GestionnaireClient clientHandler:gestionnaire_client) {
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
		gestionnaire_client.remove(this);
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
