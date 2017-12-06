package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	private static class inputThread {

		Socket socket;

		public inputThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				InputStream systemIn = System.in;
				OutputStream socketOutputStream = socket.getOutputStream();

				int readByte;
				while ((readByte = systemIn.read()) != -1) {
					socketOutputStream.write(readByte);
				}

				socket.shutdownOutput();
			} catch (IOException e) {
				System.out.println("Error while gathering user input");
			}
		}

	}

	private static class outputThread {

		Socket socket;

		public outputThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {

			try {
				OutputStream systemOut = System.out;
				InputStream socketInputStream = socket.getInputStream();

				int readByte;
				while ((readByte = socketInputStream.read()) != -1) {
					systemOut.write(readByte);
				}

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
		Socket socket = new Socket("localhost", PORT_NUMBER);
		outputThread outThread = new outputThread(socket);
		inputThread inThread = new inputThread(socket);

		inThread.run();
		outThread.run();
	}
}
