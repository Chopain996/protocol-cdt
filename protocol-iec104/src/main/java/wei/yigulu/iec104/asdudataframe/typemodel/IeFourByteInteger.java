package wei.yigulu.iec104.asdudataframe.typemodel;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wei.yigulu.iec104.exception.Iec104Exception;

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
public class IeFourByteInteger implements IecDataInterface{

	public static final int  OCCUPYBYTES=4;

	private Long value;

	/**
	 * Ie four bit integer
	 *
	 * @param is is
	 */
	public IeFourByteInteger(ByteBuf is) throws Iec104Exception {
		if(is.readableBytes()<OCCUPYBYTES){
			throw new Iec104Exception(3301,"可用字节不足，不能进行读取");
		}
		value =(((long)is.readByte() & 0xff)|((long)(is.readByte() & 0xff) << 8) | ((long)(is.readByte() & 0xff) << 16) |  ((long)(is.readByte() & 0xff) << 24) );
	}

	/**
	 * Encode *
	 *
	 * @param buffer buffer
	 */
	public void encode(List<Byte> buffer) {
		long tempVal = value & 0xffff;
		buffer.add((byte) tempVal);
		buffer.add((byte) (tempVal >> 8));
		buffer.add((byte) (tempVal >> 16));
		buffer.add((byte) (tempVal >> 24));
	}

	@Override
	public String toString() {
			return "长整型数值: " + value;

	}

	@Override
	public Object getIecValue() {
		return this.value;
	}
}
