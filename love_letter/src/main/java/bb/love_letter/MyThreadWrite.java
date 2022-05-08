package bb.love_letter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

class MyThreadWrite extends Thread{
    private DataOutputStream os;
    public BufferedReader br;
    public String clientName =  "@Client0";
    public MyThreadWrite(DataOutputStream o,String name){
        os=o;
        clientName = name;
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
                // RECEIVE MESSAGE FROM SERVER
                String msg = br.readLine();
                os.writeUTF(msg);
            }
        }
        catch(Exception e){

        }
    }
}