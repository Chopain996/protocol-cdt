import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.nettyconfig.Iec104HSMasterBuilder;
import wei.yigulu.iec104.util.SendDataFrameHelper;


/**
 * 客户端测试
 *
 * @author 修唯xiuwei
 * @create 2019-01-22 16:05
 * @Email 524710549@qq.com
 **/
public class ClientTest {

	public static void main(String[] args) throws Exception {
		Iec104HSMasterBuilder iec104HSMasterBuilder = new Iec104HSMasterBuilder("192.168.1.161", 2434);
				iec104HSMasterBuilder.createByUnBlock();
		/*Apdu apdu=new Apdu();
		iec104HSMasterBuilder.sendFrameToOpposite(apdu.encode());
		SendDataFrameHelper.sendTotalSummonFrame(iec104HSMasterBuilder.getFuture().channel(),1,6,iec104HSMasterBuilder.getLog());
		System.out.println(123);*/
	}

}
