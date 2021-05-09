package TCP;

import a3.NombreSecret;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SrvTcpAdivina {
/* Servidor TCP que genera un número perquè ClientTcpAdivina.java jugui a encertar-lo 
 * i on la comunicació dels diferents jugador passa per el Thread : ThreadServidorAdivina.java
 * */

	int port;

	public SrvTcpAdivina(int port ) {
		this.port = port;
	}

	public void listen() {
		ServerSocket serverSocket;
		Socket clientSocket;

		try {
			serverSocket = new ServerSocket(port);
			while(true) {
				clientSocket = serverSocket.accept();
				ThreadSevidorAdivina FilServidor = new ThreadSevidorAdivina(clientSocket);
				Thread client = new Thread(FilServidor);
				client.start();
			}
		} catch (IOException ex) {
			Logger.getLogger(SrvTcpAdivina.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		SrvTcpAdivina srv = new SrvTcpAdivina(5558);
		srv.listen();

	}
}
