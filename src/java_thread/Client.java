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
	
	public Client(Socket socket,String username) {
		try{
			this.socket=socket;
			this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username=username;
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
				String messageToSend=scanner.nextLine();
				bufferedWriter.write(username+": "+messageToSend);
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
	
	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in);
		System.out.println("Bonjour, cher client !");
		String username=sc.nextLine();
		Socket socket=new Socket("localhost",1234);
		Client client=new Client(socket,username);
		client.listenForMessage();
		client.sendMessage();
	}

	
	
	
	
	/*
    private static String pseudo;
    private String threadName;
    private static int id_customer;
    private InetAddress ip_adress;

    private Socket socketClient;
    
    private static ArrayList<Client> liste_cliente_official=new ArrayList<Client>();

    public Client(String pseudo, int id_customer,String threadName,Socket socket) throws UnknownHostException {
        Client.pseudo = pseudo;
        Client.id_customer = id_customer;
        this.ip_adress=InetAddress.getLocalHost();
        this.threadName=threadName;

        //this.socketClient = socketClient;

    }

    public static String getPseudo() {
        return pseudo;
    }

    public static int getId_customer() {
        return id_customer;
    }

    public static void setPseudo(String pseudo) {
        Client.pseudo = pseudo;
    }

    public void setIp_adress(InetAddress ip_adress) {
        this.ip_adress = ip_adress;
    }

    

    public static String customers_list(){ //Dans cette méthode on veut afficher la liste de tous les pseudos sauvegardés ainsi que les id_customers.
        String list_customer="";
        for(int i=0;i<liste_cliente_official.size();i++){
            list_customer=list_customer+"Pseudo client :"+liste_cliente_official.get(i).getPseudo()+"  "+"Identifiant client : "+ liste_cliente_official.get(i).getId_customer()+"\n";
        }
        return list_customer;
    }

    public static ArrayList<Client> getListe_cliente_official() {
        return liste_cliente_official;
    }

    public static void setListe_cliente_official(ArrayList<Client> liste_cliente_official) {
        Client.liste_cliente_official = liste_cliente_official;
    }

	

	public  String getThreadName() {
		return this.threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	@Override
	public void run() {
		
		
	}
	*/
	
	
	
}


