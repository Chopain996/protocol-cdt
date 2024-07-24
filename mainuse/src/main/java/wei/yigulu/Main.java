package wei.yigulu;

import wei.yigulu.cdt.netty.CDTMaster;
import wei.yigulu.utils.ConfigRead;

import static wei.yigulu.utils.ConfigRead.configMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        new ConfigRead();
        new Thread(() -> {
            new CDTMaster((String) configMap.get("COM"), (Integer) configMap.get("baudRate"),new LocalCDTDataHandler()).create();
        }).start();
//       for (;;){
//           System.out.println(DataContainer.getInstance().toString());
//           Thread.sleep(3000);
//       }
    }
}