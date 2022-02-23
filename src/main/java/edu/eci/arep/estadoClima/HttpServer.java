package main.java.edu.eci.arep.estadoClima;

import java.net.*;
import java.io.*;

public class HttpServer {

	public static void main(String[] args) throws IOException {
       
		boolean running = true;
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(getPort());
		} catch (IOException e) {
			System.err.println("Could not listen on port: 35000.");
			System.exit(1);
		}
		
		while (running) {
		Socket clientSocket = null;
		try {
			System.out.println("Listo para recibir ...");
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(
		new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;
		
		boolean primeraLinea = true;
		String file = "";
		while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (primeraLinea) {
                file = inputLine.split(" ")[1];
                System.out.println("File: " + file);
                primeraLinea = false;
            }
            if (!in.ready()) {
                break;
            }
        }
		if (file.startsWith("/Clima")) {
        	outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Estado del Clima</title>\n"
                    + "</head>"
                    + "<body>"
                    + "<h1>Estado del clima</h1>"
                    + "<form class='Form'>"
                    + "<label for='Get-Clima'>Lugar a consultar: </label>"
                    + "<input id='Get-Clima' type='text'>"
                    + "<input type='button' value='Consultar' class='Boton' onclick=clickF()>"
                    + "</body>"
                    + "<script>"
                    + "var boton = document.getElementById('Boton');"
                    + "var clickF = function() {console.log('Click en el boton de consulta');}"
                    + "</script>"
                    + "</html>";
        
        } else if (file.startsWith("/Consulta")) {
        	outputLine = "No implementado aun";
        	// Realizar la consulta al API
        	
    	}else {
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Consulta erronea</title>\n"
                    + "</head>"
                    + "<body>"
                    + "<h2>La URL ingresada no es correcta</h2>"
                    + "</body>"
                    + "</html>";
        }
			out.println(outputLine);
			out.close();
			in.close();
			clientSocket.close();
		}
		serverSocket.close();
	}
	
	static int getPort() {
		 if (System.getenv("PORT") != null) {
			 return Integer.parseInt(System.getenv("PORT"));
		 }
		 return 35000;
	}
}