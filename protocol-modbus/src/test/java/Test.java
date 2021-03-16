import wei.yigulu.modbus.domain.datatype.numeric.*;
import wei.yigulu.modbus.netty.ModbusTcpMasterBuilder;

/**
 * @author: xiuwei
 * @version:
 */
public class Test {
	public static void main(String[] args) throws InterruptedException {
	/*	ModbusTcpMasterBuilder master = new ModbusTcpMasterBuilder("127.0.0.1", 5002);
		master.createByUnBlock();
		Thread.sleep(30000L);
		System.out.println("重启");
		master.stop();
		master.createByUnBlock();*/
	//43 b0 ca ed
		byte[] bs=new byte[]{(byte)0x43,(byte)0xb0,(byte)0xca,(byte)0xed};
		System.out.println("ABCD:"+new ABCD().decode(bs,0).getValue());
		System.out.println("CDAB:"+new CDAB().decode(bs,0).getValue());
		System.out.println("DCBA:"+new DCBA().decode(bs,0).getValue());
		System.out.println("BADC:"+new BADC().decode(bs,0).getValue());
		System.out.println("P_ABCD:"+new P_ABCD().decode(bs,0).getValue());
		System.out.println("P_CDAB:"+new P_CDAB().decode(bs,0).getValue());
		System.out.println("AB:"+new PM_AB().decode(bs,1).getValue());
		System.out.println("BA:"+new PM_BA().decode(bs,1).getValue());


	}
}
