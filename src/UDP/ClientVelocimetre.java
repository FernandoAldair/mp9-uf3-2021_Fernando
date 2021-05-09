package UDP;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ClientVelocimetre {
    private InetAddress multicatIP;

    InetSocketAddress groupMulticast;
    NetworkInterface netIf;

    public ClientVelocimetre(String ip, int port)throws IOException {

        this.multicatIP = InetAddress.getByName(ip);
        this.groupMulticast = new InetSocketAddress(multicatIP,port);
        this.netIf = NetworkInterface.getByName("wlp0s20f3");
    }

    public void runClient() throws IOException{
        byte [] receivedData = new byte[1024];
        MulticastSocket  socket = new MulticastSocket(5557);
        socket.joinGroup(groupMulticast, netIf);

        DatagramPacket packet;

        int media = 0;
        float result;
        int cont = 0;
        boolean continueRunning= true;

        while(continueRunning) {
          packet = new DatagramPacket(receivedData, 1024);
          socket.setSoTimeout(5000);

          try {
              socket.receive(packet);
              media = media + ByteBuffer.wrap(packet.getData()).getInt();
              System.out.println(ByteBuffer.wrap(packet.getData()).getInt());
              cont++;
              if (cont == 5){
                  result = (float) media/5;
                  System.out.println("La velocidad media es: " + result);
                  cont = 0;
                  media = 0;
              }
          }catch(SocketTimeoutException e) {
              System.out.println("El servidor no responde");
              continueRunning = false;
          }

        }
        socket.leaveGroup(groupMulticast,netIf);

    }

    public static void main(String[] args) {
        try {
            ClientVelocimetre clientVelocimetre = new ClientVelocimetre("224.0.32.16",5557);
            clientVelocimetre.runClient();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
