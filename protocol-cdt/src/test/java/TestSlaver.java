import wei.yigulu.cdt.netty.CDTSlaver;


/**
 * 测试slaver
 *
 * @author: xiuwei
 * @version:
 */
public class TestSlaver {

	public static void main(String[] args) throws InterruptedException {
		new Thread(() -> {
			new CDTSlaver("COM3", 9600,new LocalCDTDataTransmitter()).create();
		}).start();
       /*for (;;){
           System.out.println(DataContainer.getInstance().toString());
           Thread.sleep(3000);
       }*/
	}
}
