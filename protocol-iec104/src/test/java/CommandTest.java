import wei.yigulu.iec104.nettyconfig.Iec104MasterBuilder;
import wei.yigulu.iec104.util.SendCommandHelper;

public class CommandTest {
    public static void main(String[] args) throws Exception {
        Iec104MasterBuilder i=new Iec104MasterBuilder("127.0.0.1",2404);
        i.createByUnBlock();
        Thread.sleep(3000L);
        SendCommandHelper.sendShortCommand(i,1,1,30,10700f);
    }
}
