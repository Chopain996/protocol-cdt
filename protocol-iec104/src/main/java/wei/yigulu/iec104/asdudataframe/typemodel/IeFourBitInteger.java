package wei.yigulu.iec104.asdudataframe.typemodel;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 四字节补位int
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IeFourBitInteger {

	private Integer value;

	/**
	 * Ie four bit integer
	 *
	 * @param is is
	 */
	public IeFourBitInteger(ByteBuf is) {
		value = ((is.readByte() & 0xff) | ((is.readByte() & 0xff) << 8) | ((is.readByte() & 0xff) << 16) | ((is.readByte() & 0xff) << 24));
	}

	/**
	 * Encode *
	 *
	 * @param buffer buffer
	 */
	public void encode(List<Byte> buffer) {
		int tempVal = value & 0xff;
		buffer.add((byte) tempVal);
		buffer.add((byte) (tempVal >> 8));
		buffer.add((byte) (tempVal >> 16));
		buffer.add((byte) (tempVal >> 24));
	}

}
