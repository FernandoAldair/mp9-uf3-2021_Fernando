package TCP;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadSevidorAdivina implements Runnable {

	Socket clientSocket;
	ObjectInputStream oInputS;
	ObjectOutputStream oOutputS;
	Llista llistaEntrant;

	boolean acabat;
	List<Integer> numerosDesordanados,numerosOrdenados;

	public ThreadSevidorAdivina(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		System.out.println(clientSocket.getInetAddress());
		acabat = false;
	}

	@Override
	public void run() {
		try {
			oOutputS = new ObjectOutputStream(clientSocket.getOutputStream());
			oInputS = new ObjectInputStream(clientSocket.getInputStream());

			while(!acabat) {
				llistaEntrant = (Llista) oInputS.readObject();
				System.out.println(llistaEntrant.getNom());
				for(Integer i:llistaEntrant.getNumberList()){
					System.out.println(i);
				}
				llistaEntrant = generaResposta(llistaEntrant);
				oOutputS.writeObject(llistaEntrant);
				oOutputS.flush();
			}
		}catch(IOException | ClassNotFoundException e){
			System.out.println(e.getLocalizedMessage());
		}

		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Llista generaResposta(Llista lista) {
		if (lista != null){
			numerosDesordanados = lista.getNumberList();
			numerosOrdenados = numerosDesordanados.stream().sorted().distinct().collect(Collectors.toList());
			lista.setNumberList(numerosOrdenados);
			return lista;
		}else return null;
	}

}
