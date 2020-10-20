package wei.yigulu.modbus.domain.datatype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 数字的类型
 *
 * @author: xiuwei
 * @version:
 */
@NoArgsConstructor
public abstract class NumericModbusData extends RegisterValue {

	public NumericModbusData(BigDecimal value) {
		this.value = value;
	}

	@Setter
	@Getter
	@Accessors(chain = true)
	protected BigDecimal value;

	/**
	 * 编码
	 *
	 * @param bytes 字节
	 */
	public abstract IModbusDataType encode(List<Byte> bytes);

}
