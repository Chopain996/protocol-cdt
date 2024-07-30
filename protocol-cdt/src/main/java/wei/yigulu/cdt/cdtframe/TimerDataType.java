package wei.yigulu.cdt.cdtframe;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wei.yigulu.utils.CrcUtils;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * 遥测数据
 *
 * @author 修唯xiuwei
 **/
@NoArgsConstructor
public class TimerDataType extends BaseDateType<Integer> {

	/**
	 * 质量位描述的map
	 */
	@Getter
	private Integer ms;

	@Getter
	private Integer s;

	@Getter
	private Integer m;

	@Getter
	private Integer h;

	@Getter
	private Integer d;

	@Getter
	private Integer targetNumber;

	@Getter
	private Integer isClosed;



	/**
	 * 整数数据类型
	 * 构造方法  map中的数据必须是2个以内 且必须是连续的  不足2个   其他位皆视为 false
	 * 如果数据不连续将可能在上送过程中改变被越过数据的原有值  可以不足2位 因为数据总量可能不足2的整数倍
	 * 由于cdt协议中一个功能码中有2个整数值  所有 最小的点位须为2的整数倍
	 *
	 * @param dates     数据
	 */

	public TimerDataType(Map<Integer, Integer> dates) {
		if (dates.size() == 0) {
			throw new RuntimeException("数据个数不能为0");
		}
		if (dates.size() > 2) {
			throw new RuntimeException("数据个数超过二个");
		}
		Set<Integer> set = dates.keySet();
		if (Collections.min(set) % 2 != 0) {
			throw new RuntimeException("数据点位不是以2的整数倍开头");
		}
		if ((Collections.max(set) - Collections.min(set)) != dates.size() - 1) {
			throw new RuntimeException("数据点位不连续");
		}

		this.dates = dates;
//		if (getFunctionNum() <= 0x7f) {
//			this.qualityDescriptionMap = qualities == null ? new HashMap<>() : qualities;
//		}else if(getFunctionNum() <= 0xdf&& getFunctionNum() >= 0xa0) {
//			this.YMqualityDescriptionMap = qualities == null ? new HashMap<>() : qualities;
//		}
		if (getFunctionNum() == 0x80 || getFunctionNum() == 0x81) {
			if(this.getMs()>0&&this.getS()>0&&this.getM()>0&&this.getH()>0&&this.getD()>0&&this.getTargetNumber()>0) {
				System.out.println("SOE数据数据都存在值");

			} else {
				System.out.println("SOE数据数据值都为空");
			}
		} else {
			System.out.println("SOE数据功能码非80或81H,异常，返回");
		}

	}

	public Map getDataJson() throws JsonProcessingException {
		HashMap<String, String> dataMap = new HashMap<>();

		for (Integer i : this.dates.keySet()) {

			dataMap.put(String.valueOf(i), this.dates.get(i).toString());

		}
		return dataMap;
	}

	public void loadBytes(ByteBuf byteBuf) throws InstantiationException, IllegalAccessException {
		this.dates=new HashMap<>();
		byte[] bs1 = new byte[5];
		byte[] bs2 = new byte[5];
		if (byteBuf.readableBytes() > 5) {
			byteBuf.readBytes(bs1);
			this.functionNum = bs1[0] & 0xff;
			this.crc = byteBuf.readByte();
			if ((this.crc & 0xff) == CrcUtils.generateCRC8(bs1)) {
				readDates(Arrays.copyOfRange(bs1, 1, bs1.length));
			}
		}
		if (byteBuf.readableBytes() > 5) {
			byteBuf.readBytes(bs2);
			this.functionNum = bs2[0] & 0xff;
			this.crc = byteBuf.readByte();
			if ((this.crc & 0xff) == CrcUtils.generateCRC8(bs2)) {
				readDates(Arrays.copyOfRange(bs2, 1, bs2.length));
			}
		}
	}

	@Override
	public void readDates(byte[] bs) {
		//SOE功能码处于80H和81H
		//毫秒，秒，分在80H
		//小时，日在81H，对象号和开关状态在81H
		if(getFunctionNum() == 0x80||getFunctionNum() == 0x81){
			if (getFunctionNum() == 0x80) {
				this.ms=decode2Int(bs[0],bs[1]);
				this.s=bs[2]&0x3f;
				this.m=bs[3]&0x3f;
				this.dates.put(0,this.ms);
				this.dates.put(1,this.s);
				this.dates.put(2,this.m);
			}else if(getFunctionNum() == 0x81) {
				this.h=bs[0]&0x3f;
				this.d=bs[1]&0x3f;
				this.targetNumber=decodeTarget2Int(bs[2],bs[3]);
				this.dates.put(3,this.h);
				this.dates.put(4,this.d);
				this.dates.put(5,this.targetNumber);
			}
			this.dates.put(6, this.isClosed);
		}


	}


	/**
	 * 转化成CDT的int型
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	private Integer decodeTarget2Int(Byte b1, Byte b2) {
		int i = (b1 & 0xff) | (b2 & 0x0f) << 8;
		if ((b2>>8 & 0x01) == 1) {
			this.isClosed=1;
		}
		return i;
	}

	/**
	 * 转化成CDT的int型
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	private Integer decode2Int(Byte b1, Byte b2) {
		int i = (b1 & 0xff) | (b2 & 0x03) << 8;
//		if ((b2 >> 3 & 0x01) == 1) {
//			i = (2048 - i) * -1;
//		}
		return i;
	}
	/**
	 * 遥脉转化成CDT的int型
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @return
	 */
	private Integer decode2Int(Byte b1, Byte b2, Byte b3, Byte b4) {
		// 检查b4的第3位 (bit 6)
		boolean isSpecialEncoding = (b4 & 0x40) != 0;
		if (!isSpecialEncoding) {
			// BCD编码处理
			// 假设只处理b1, b2, b3各表示一个BCD数字 (最简单的形式)
			int digit1 = (b1 & 0xFF);
			int digit2 = (b2 & 0xFF) << 8;
			int digit3 = (b3 & 0xFF) << 16;
			return digit1 + digit2 + digit3;
		} else {
			// 特殊编码处理，类似于decode2Int(Byte b1, Byte b2) 但基于b1, b2, b3
			int i = (b1 & 0xff) | (b2 & 0x07) << 8 | (b3 & 0x01) << 11;
			if ((b3 >> 1 & 0x01) == 1) {
				i = (2048 - i) * -1;
			}
			return i;
		}
	}



	@Override
	public Map<Integer, Integer> getDates() {
		return this.dates;
	}

	@Override
	protected void encode(ByteBuffer byteBuffer) {

		System.out.println("there is no implment for TimerDataType encode()");
	}


	@Override
	public String toString() {
		String s = "";
		if (this.dates != null) {
			if(this.getFunctionNum() == 0x80|| this.getFunctionNum() == 0x81){
				s += this.getD()+"-"+this.getH()+":"+this.getM()+":"+this.getS()+":"+this.getMs()+"==>  Target:"+this.getTargetNumber()+"\n";

			}
		}
		return s;
	}


}
