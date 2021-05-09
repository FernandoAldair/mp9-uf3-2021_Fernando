package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcpAdivina extends Thread {
/* CLient TCP que ha endevinar un número pensat per SrvTcpAdivina.java */

	String hostname;
	int port;
	boolean continueConnected;
	int numList;

	ObjectInputStream oInputS;
	ObjectOutputStream oOutputS;

	public ClientTcpAdivina(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		continueConnected = true;
	}

	public void run() {
		Llista serverData = null;
		Socket socket;


		try {
			socket = new Socket(InetAddress.getByName(hostname), port);
			oInputS = new ObjectInputStream(socket.getInputStream());
			oOutputS = new ObjectOutputStream(socket.getOutputStream());
			while(continueConnected){
				Llista llista = getRequest(serverData);
				oOutputS.writeObject(llista);
				oOutputS.flush();
				serverData = (Llista) oInputS.readObject();
			}
			close(socket);
		} catch (UnknownHostException ex) {
			System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("Error de connexió indefinit: " + ex.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Llista getRequest(Llista serverD) {
		if (serverD != null){
			System.out.println(serverD.getNom());
			for(Integer i: serverD.getNumberList()){
				System.out.println(i);
			}
		}


		List<Integer> num = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			num.add((int) (Math.random()*100+1));
		}
		numList += 1;
		return new Llista("lista num: "+ numList,num);
	}

	private void close(Socket socket){

		try {
			if(socket!=null && !socket.isClosed()){
				if(!socket.isInputShutdown()){
					socket.shutdownInput();
				}
				if(!socket.isOutputShutdown()){
					socket.shutdownOutput();
				}
				socket.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(ClientTcpAdivina.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		ClientTcpAdivina clientTcp = new ClientTcpAdivina("localhost",5558);
		clientTcp.start();
	}
}
