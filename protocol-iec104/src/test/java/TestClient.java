import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.apdumodel.Asdu;
import wei.yigulu.iec104.asdudataframe.PulseTotalSummonType;
import wei.yigulu.iec104.nettyconfig.Iec104MasterBuilder;
import wei.yigulu.iec104.util.SendDataFrameHelper;

/**
 * 测试遥脉总招
 *
 * @author 3377
 * @date 2022/11/01
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        Iec104MasterBuilder i=new Iec104MasterBuilder("127.0.0.1",2404);
        i.createByUnBlock();
        Thread.sleep(3000L);
        SendDataFrameHelper.sendTotalSummonFrame(i.getFuture().channel(),1,6,i.getLog());
        PulseTotalSummonType pulseTotalSummonType =new PulseTotalSummonType();
        Asdu asdu = pulseTotalSummonType.generateBack();
        Apdu apdu=new Apdu();
        apdu.setAsdu(asdu);
        i.sendFrameToOpposite(apdu.encode());
    }
}
