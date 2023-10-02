package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {

        boolean salir = false;
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\n");
            Socket socket = new Socket("127.0.0.1", 5001);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String mensaje = in.readUTF();
            System.out.println(mensaje);

            String nombre = sc.next();
            out.writeUTF(nombre);
            while (!salir) {
                ClienteHilo hilo = new ClienteHilo(in, out);
                hilo.start();
                hilo.join();
                salir= true;
            }
        } catch (IOException | InterruptedException e) {
            salir= true;
            throw new RuntimeException(e);
        }
    }
}
