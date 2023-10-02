package cliente;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ClienteHilo extends Thread {


    private DataInputStream in;
    private DataOutputStream out;
    private BufferedWriter bw;
    private String[] read = new String[5];
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    int intentos=0;

    public ClienteHilo(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;

    }

    @Override
    public void run() {

        //MasterMind masterMind= new MasterMind();

        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        File fileCliente = new File("cliente.json");
        jsonObject = new JSONObject();
        String mensaje = null;


        while (!salir) {


            try {
                bw = new BufferedWriter(new FileWriter(fileCliente));
                System.out.println("Juego iniciado");

                for (int i = 0; i < read.length; i++) {
                    boolean colorOK = true;
                    while (colorOK) {  System.out.println("Escribe un color para completar la secuencia: (azul, rojo, verde, rosa, amarillo)");

                      String color = sn.nextLine();

                      if(color.equals("azul") || color.equals("rojo") || color.equals("verde") || color.equals("amarillo") ||color.equals("rosa")) {
                          read[i] = color;
                          colorOK = false;
                      }else {
                          System.out.println("color mal escrito vuelva a escribirlo");
                      }

                  }


                }

                jsonArray = new JSONArray(Arrays.asList(read));
                jsonObject.put("color", jsonArray);
                out.writeUTF(jsonObject.toString());

                System.out.println();
                System.out.println("Comprobamos...");
                System.out.println("Su respuesta:");
                System.out.println(jsonArray);

                mensaje = in.readUTF();

                if (mensaje.equals("correcto") ) {
                    System.out.println("Ganaste!");
                    salir = true;
                }else if( intentos <= 5){
                    System.out.println("incorrecto");
                    System.out.println("Pistas:");
                    System.out.println(mensaje);
                    intentos++;
                    System.out.println();
                    System.out.println("sus intentos son: " + intentos);

                }

            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

    }


}
