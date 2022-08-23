import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.apdumodel.Asdu;
import wei.yigulu.iec104.asdudataframe.PulseTotalSummonType;
import wei.yigulu.iec104.nettyconfig.Iec104MasterBuilder;

public class TestClient {
    public static void main(String[] args) throws Exception {
        Iec104MasterBuilder i=new Iec104MasterBuilder("127.0.0.1",2404);
        i.createByUnBlock();
        Thread.sleep(3000L);
        PulseTotalSummonType pulseTotalSummonType =new PulseTotalSummonType();
        Asdu asdu = pulseTotalSummonType.generateBack();
        Apdu apdu=new Apdu();
        apdu.setAsdu(asdu);
        i.sendFrameToOpposite(apdu.encode());
    }
}
