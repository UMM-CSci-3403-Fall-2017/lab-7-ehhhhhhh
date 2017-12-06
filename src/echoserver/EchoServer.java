package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static final int PORT_NUMBER = 6013;

	

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private static class clientThread extends Thread {
		
		Socket clientSocket;

		public clientThread(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		

		public void run() {
			try {
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = clientSocket.getOutputStream();
				int b;
				while ((b = inputStream.read()) != -1) {
					outputStream.write(b);
				}
	
			clientSocket.close();
			} catch (IOException e) {
				System.out.println("IOException when running the thread");
			}
		}
	}


	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		while (true) {
			Socket socket = serverSocket.accept();
			clientThread thread = new clientThread(socket);
			thread.run();
		}

	}
}
