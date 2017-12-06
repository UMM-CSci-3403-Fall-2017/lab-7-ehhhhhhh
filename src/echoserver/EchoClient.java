package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	//thread for processing system input
	private static class inputThread {

		Socket socket;

		public inputThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				//stores System.in and the socket's outputStream for ease of use
				InputStream systemIn = System.in;
				OutputStream socketOutputStream = socket.getOutputStream();

				//writes bytes from System input to the socket
				int readByte;
				while ((readByte = systemIn.read()) != -1) {
					socketOutputStream.write(readByte);
				}

				//closes the socket's ouputStream without blowing everything up
				socket.shutdownOutput();
			} catch (IOException e) {
				System.out.println("Error while gathering user input");
			}
		}

	}

	//thread for processing server output
	private static class outputThread {

		Socket socket;

		public outputThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {

			try {
				//stores System.out and the socket's inputStream for ease of use
				OutputStream systemOut = System.out;
				InputStream socketInputStream = socket.getInputStream();

				//writes bytes from socket's inputStream to system output
				int readByte;
				while ((readByte = socketInputStream.read()) != -1) {
					systemOut.write(readByte);
				}

				//not entirely clear on why this matters but it does, clears out system output
				System.out.flush();

			} catch (IOException e) {
				System.out.println("Error when processing server output");
			}

		}

	}

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {

		//creates and runs the threads
		Socket socket = new Socket("localhost", PORT_NUMBER);
		outputThread outThread = new outputThread(socket);
		inputThread inThread = new inputThread(socket);

		inThread.run();
		outThread.run();
	}
}
