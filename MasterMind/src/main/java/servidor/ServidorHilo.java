package servidor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class ServidorHilo extends Thread {
    private ServerSocket server;
    private DataOutputStream out;
    private DataInputStream in;

    private static final int CODE_LENGTH = 5;
    String[] codigoRandom = new String[CODE_LENGTH];
    JSONObject jsonCodigo;

    String respuesta = "";

    File ficheroJson = new File("codigo.json");



    public ServidorHilo(DataInputStream in, DataOutputStream out, ServerSocket server) {
        this.out = out;
        this.in = in;
        this.server = server;

    }

    @Override
    public void run() {

        try {
            generateSecretCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                respuesta = in.readUTF();
                if (adivinar(respuesta).equals("correcto")) {
                    out.writeUTF("correcto");
                    break;
                } else {
                    out.writeUTF(adivinar(respuesta));

                }


            } catch (IOException e) {

                e.printStackTrace();
                break;
            }

        }
        System.out.println("[Fin de la entrada]");
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public String adivinar(String numerosResult) throws IOException {
        leerFichero(ficheroJson);

        JSONObject jsonObjectCliente = new JSONObject(numerosResult);
        JSONArray jsonArraySolucion = jsonCodigo.getJSONArray("color");
        JSONArray jsonArrayRespuesta = jsonObjectCliente.getJSONArray("color");




        ArrayList<String> pista = new ArrayList<>();

        for (int i = 0; i < jsonArrayRespuesta.length(); i++) {
            if (jsonArraySolucion.get(i).equals(jsonArrayRespuesta.get(i))) {
                pista.add(jsonArraySolucion.getString(i));
                continue;

            }
            int id = jsonArraySolucion.toList().indexOf(jsonArrayRespuesta.get(i));
            if (id != -1) {
                pista.add("Mala Posicion");
                continue;
            }
            pista.add("Incorrecto");


        }

        if (pista.equals(jsonArraySolucion.toList())) {

            out.writeUTF("correcto");
            return "correcto";

        } else {
            return pista.toString();
        }


    }


    private void leerFichero(File ficheroJson) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(ficheroJson));
        jsonCodigo = new JSONObject(reader.readLine());

        reader.close();

    }

    private void generateSecretCode() throws IOException {
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int num = random.nextInt(5) + 1;
            switch (num) {
                case 1:
                    codigoRandom[i] = "azul";
                    break;
                case 2:
                    codigoRandom[i] = "verde";
                    break;
                case 3:
                    codigoRandom[i] = "rojo";
                    break;
                case 4:
                    codigoRandom[i] = "amarillo";
                    break;
                case 5:
                    codigoRandom[i] = "rosa";
                    break;
            }


        }
        escribirCodigoEnJson();
    }

    private void escribirCodigoEnJson() throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < CODE_LENGTH; i++) {
            jsonArray.put(codigoRandom[i]);
        }
        jsonObject.put("color", jsonArray);

        BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroJson));
        bw.write(jsonObject.toString());
        bw.flush();
        bw.close();

    }


}
