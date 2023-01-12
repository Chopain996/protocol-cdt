import wei.yigulu.modbus.domain.ModbusSlaveDataContainer;
import wei.yigulu.modbus.domain.command.AbstractModbusCommand;
import wei.yigulu.modbus.domain.datatype.numeric.PM_AB;
import wei.yigulu.modbus.netty.ModbusRtuSlaverBuilder;

import java.math.BigDecimal;

import static wei.yigulu.modbus.domain.FunctionCode.WRITE_REGISTER;

public class RtuSlaveTest3 {

    public static BigDecimal commandValue = BigDecimal.valueOf(10000);

    public static final void setCommandValue(BigDecimal cv) {
        commandValue = cv;
    }

    public static void main(String[] args) throws InterruptedException {
        MyModbusRtuSlaverBuilder modbusRtuSlaverBuilder = new MyModbusRtuSlaverBuilder("COM2");
        ModbusSlaveDataContainer modbusSlaveDataContainer = modbusRtuSlaverBuilder.getModbusSlaveDataContainer();
        BigDecimal x;
        modbusRtuSlaverBuilder.createByUnBlock();
        for (; ; ) {
            x = commandValue.divide(BigDecimal.valueOf(100));
            System.out.println(x);
            modbusSlaveDataContainer.setRegister(3, 1627, new PM_AB(BigDecimal.valueOf(2300).subtract(x.multiply(BigDecimal.valueOf(10)))));
            modbusSlaveDataContainer.setRegister(3, 1628, new PM_AB(BigDecimal.valueOf(2300).add(x.multiply(BigDecimal.valueOf(10)))));
            modbusSlaveDataContainer.setRegister(3, 1632, new PM_AB(x.multiply(BigDecimal.valueOf(100))));
            Thread.sleep(3000L);
        }

    }

    static class MyModbusRtuSlaverBuilder extends ModbusRtuSlaverBuilder {

        public MyModbusRtuSlaverBuilder(String commPortId) {
            super(commPortId);
        }


        public boolean receiveCommand(AbstractModbusCommand command) {
            if (command.getFunctionCode() == WRITE_REGISTER) {
                if (command.getStartAddress() == 2180) {
                    if (command.getDataBytes().length == 2) {
                        PM_AB decode = new PM_AB().decode(command.getDataBytes(), 0);
                        System.out.println("接收到命令:" + command.getStartAddress() + "——————" + decode.getValue());
                        setCommandValue(decode.getValue());
                    }
                }
            }
            return true;
        }
    }
}
