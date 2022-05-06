package bb.love_letter;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private DataOutputStream clientOutput;
    private DataInputStream clientInput;

    public ClientHandler(Socket socket, DataOutputStream output, DataInputStream input)
    {
        clientSocket = socket;
        clientOutput = output;
        clientInput = input;
    }

    @Override
    public void run() {
        System.out.println("Thread started running");
        try {
            clientOutput.writeUTF("Hi there, I am the server! beep boop");
            clientOutput.flush();

            //Receive message from client
            clientInput = (DataInputStream) clientSocket.getInputStream();
            //create a read-buffer for message from client
            byte[] readBuffer = new byte[64];
            clientInput.read(readBuffer);
            String msgFromClient = new String(readBuffer);
            System.out.println("The message from client: "+ msgFromClient);

            //Deal with the message from client
            clientOutput = (DataOutputStream) clientSocket.getOutputStream();
            //create a buffer to write message for replying client
            byte[] writeBuffer = new byte[64];
            /*if(nameList.contains(msgFromClient)){
                writeBuffer = "The entered nickname exists already".getBytes();
                clientOutput.write(writeBuffer);
            }*/

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
