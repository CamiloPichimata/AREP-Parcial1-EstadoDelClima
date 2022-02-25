package main.java.edu.eci.arep.estadoClima;

import java.net.*;
import java.io.*;

public class HttpServer {

	public static void main(String[] args) throws IOException {
       
		String URLConsulta = "";
		String consultaLugar = "";
		
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
		String lugar = "";

		while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (primeraLinea) {
                file = inputLine.split(" ")[1];
                System.out.println("File: " + file);
                primeraLinea = false;
                
                if (inputLine.contains("lugar")) {
                	lugar = inputLine.split("=")[1];
                	lugar = lugar.split(" ")[0];
                	URLConsulta = "http://api.openweathermap.org/data/2.5/weather?q=" + lugar + "&appid=294125cf28db42f2079fe33557675f29";
                }
                
            }
            if (!in.ready()) {
                break;
            }
        }
		if (file.startsWith("/clima")) {
        	outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Estado del Clima</title>\n"
                    + "</head>"
                    + "<style>body {text-align: center; font-family: 'Akaya Telivigala', cursive;}</style>"
                    + "<body>"
                    + "<h1>Estado del clima</h1>"
                    + "<form class='Form'>"
                    + "<label for='Get-Clima'>Lugar a consultar: </label>"
                    + "<input id='Get-Clima' type='text'>"
                    + "<input type='button' value='Consultar' class='Boton' onclick=clickF()>"
                    + "<div><br></br></div>"
                    + "<div><label id='rtaConsulta'></label></div>"
                    + "</body>"
                    + "<script src=\"https://unpkg.com/axios/dist/axios.min.js\"></script>"
                    + "<script src=\"https://code.jquery.com/jquery-3.4.1.min.js\"></script>"
                    + "<script>"
                    + "var boton = document.getElementById('Boton');"
                    + "var clickF = function() {"
                    + "    console.log('Click en el boton de consulta');"
                    + "    var label = document.getElementById('rtaConsulta');"
                    + "    var lugar = document.getElementById('Get-Clima');"
                    + "    axios.get('/consulta?lugar=' + lugar.value).then(response => {"
                    + "        var rta = response.data;"
                    + "        label.innerHTML = rta;"
                    + "    });"
                    + "}"
                    + "</script>"
                    + "</html>";
        
        } else if (file.startsWith("/consulta")) {
        	consultaLugar = ConsultaReader.consultaURL(URLConsulta);
        	outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + consultaLugar;
        	
    	} else {
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
