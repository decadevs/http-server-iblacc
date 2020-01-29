package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
  public static void main(String args[]) {
    MyServer ws = new MyServer();
    ws.start();
  }
}

class MyServer {
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 8080");
    Path inputFile = Paths.get("input.json");
    
    StringBuffer response = new StringBuffer();

    try {
      s = new ServerSocket(8080);
      List<String> lines = Files.readAllLines(inputFile, StandardCharsets.UTF_8);
      for (String string : lines) {
        response.append(string);
      }
      
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    while (true) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
            remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());

        System.out.println();
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        if(in.readLine().contains("/json")){
          out.println(response);
        }  else {
          out.println("<H1>Welcome to the Homepage</H2>");
        }
        
        out.flush();
        remote.close();
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

}