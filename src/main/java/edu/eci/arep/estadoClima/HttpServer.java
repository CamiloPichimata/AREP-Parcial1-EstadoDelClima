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
		
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Received: " + inputLine);
			if (!in.ready()) {
				break;
			}
		}
			outputLine = "HTTP/1.1 200 OK\n\r"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Title of the document</title>\n"
                    + "</head>"
                    + "<body>"
                    + "My Web Site"
                    + "</body>"
                    + "</html>";
			out.println(outputLine);
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		}
	}
	
	static int getPort() {
		 if (System.getenv("PORT") != null) {
			 return Integer.parseInt(System.getenv("PORT"));
		 }
		 return 35000;
	}
}