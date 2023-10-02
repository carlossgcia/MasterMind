package servidor;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(5001);
            Socket sc;
            System.out.println("Servidor iniciado");

            while (true){
                sc = server.accept();

                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                out.writeUTF("Indica tu nombre");
                String nombreCliente = in.readUTF();

                ServidorHilo hilo = new ServidorHilo(in, out, server);
                hilo.start();

                System.out.println("creada la conexion con el cliente " + nombreCliente);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
