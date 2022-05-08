package bb.love_letter;

import java.io.DataInputStream;

class MyThreadRead extends Thread{
    DataInputStream is;
    public MyThreadRead(DataInputStream i){
        is=i;
    }
    public void run()
    {
        try{
            String msg=null;
            while(true){
                // RECEIVE MESSAGE FROM SERVER
                msg = is.readUTF();
                if(msg != null)
                    System.out.println(msg);
                msg = null;
            }
        }
        catch(Exception e){

        }
    }
}