package bb.love_letter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

class MyThreadWrite extends Thread{
    private DataOutputStream os;
    public BufferedReader br;
    public User clientName;
    public MyThreadWrite(DataOutputStream o,User user){
        os=o;
        clientName = user;
        try{
            InputStreamReader isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
        }
        catch(Exception e)
        {

        }
    }
    public void run()
    {
        try{
            while(true){
                // SEND MESSAGE TO SERVER
                String msg = br.readLine();
                os.writeUTF(msg);
            }
        }
        catch(Exception e){

        }
    }
}