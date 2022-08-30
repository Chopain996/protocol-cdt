import com.alibaba.fastjson.JSON;
import wei.yigulu.modbus.domain.FunctionCode;
import wei.yigulu.modbus.domain.Obj4RequestRegister;
import wei.yigulu.modbus.domain.datatype.*;
import wei.yigulu.modbus.domain.synchronouswaitingroom.TcpSynchronousWaitingRoom;
import wei.yigulu.modbus.exceptiom.ModbusException;
import wei.yigulu.modbus.netty.ModbusTcpMasterBuilder;
import wei.yigulu.modbus.utils.ModbusRequestDataUtils;

import java.util.*;

public class Test2 {

    public static void main(String[] args) throws ModbusException, InterruptedException {

        ModbusTcpMasterBuilder master = new ModbusTcpMasterBuilder("127.0.0.1", 506);
        master.createByUnBlock();
        TcpSynchronousWaitingRoom.waitTime = 5000L;
        Thread.sleep(3000L);
        Map<Integer, ModbusDataTypeEnum> map = new HashMap<>();
//构建一个请求数据的列表  该map代表 取0-120 地址位 ， 数据类型为P_AB的 数据 这个根据实际需要进行修改 占用两个寄存器的数据 地址位该数据起始寄存器地址
        for (int i = 0; i <= 9; i++) {
            map.put(i, ModbusDataTypeEnum.UNKNOW);
        }
        List<Obj4RequestRegister> ll = ModbusRequestDataUtils.splitModbusRequest(map, 1, FunctionCode.READ_HOLDING_REGISTERS);

        for (; ; ) {
            try {
                Map<Integer, IModbusDataType> map1 = ModbusRequestDataUtils.getRegisterData(master, ll);
                ArrayList<Integer> lll = new ArrayList<Integer>(map1.keySet());
                Collections.sort(lll);
                for (Integer i : lll) {
                    if (map1.get(i) instanceof NumericModbusData) {
                        ((NumericModbusData) map1.get(i)).getValue();
                        System.out.println(i + " ============ " + ((NumericModbusData) map1.get(i)).getValue());
                    } else {
                        if (map1.get(i) instanceof UnknownTypeRegisterValue) {
                            System.out.println(i + "-1============ " + JSON.toJSONString(((UnknownTypeRegisterValue) map1.get(i)).getB1()));
                            System.out.println(i + "-2============ " + JSON.toJSONString(((UnknownTypeRegisterValue) map1.get(i)).getB2()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(3000L);
        }
    }
}
