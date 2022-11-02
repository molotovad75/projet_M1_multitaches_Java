package java_thread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur  {
	
	private ServerSocket serverSocket;
	
	public Serveur(ServerSocket serverSocket) {
		this.serverSocket=serverSocket;
	}
	
	public void startServer() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket=serverSocket.accept();
				System.out.println("Un nouveau client s'est connecté");
				ClientHandler clientHandler=new ClientHandler(socket);
				Thread thread =new Thread(clientHandler);
				thread.start();
			}
		}catch(IOException e) {
			e.getMessage();
		}
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket!=null) {
				serverSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		ServerSocket serverSocket=new ServerSocket(1234);
		Serveur server=new Serveur(serverSocket);
		server.startServer();
	}
}