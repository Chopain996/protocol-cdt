package wei.yigulu.jsc;

public class JSerialCommReadTimeoutException extends RuntimeException{
    public JSerialCommReadTimeoutException(){

    }
    public JSerialCommReadTimeoutException(String msg){
        super(msg);
    }
}
