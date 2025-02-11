package wei.yigulu.cdt.cdtframe;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import wei.yigulu.utils.CrcUtils;
import wei.yigulu.utils.JsonBuilder;

import java.nio.ByteBuffer;
import java.util.*;


/**
 * 遥测数据
 *
 * @author 修唯xiuwei
 **/
@NoArgsConstructor
public class IntegerDataType extends BaseDateType<Integer> {

	/**
	 * 质量位描述的map
	 */
	@Getter
	private Map<Integer, QualityDescription> qualityDescriptionMap;

	@Getter
	private Map<Integer, YMQualityDescription> YMqualityDescriptionMap;

	private Logger log;



	/**
	 * 整数数据类型
	 * 构造方法  map中的数据必须是2个以内 且必须是连续的  不足2个   其他位皆视为 false
	 * 如果数据不连续将可能在上送过程中改变被越过数据的原有值  可以不足2位 因为数据总量可能不足2的整数倍
	 * 由于cdt协议中一个功能码中有2个整数值  所有 最小的点位须为2的整数倍
	 *
	 * @param dates     数据
	 * @param qualities 品质
	 */

	public IntegerDataType(Map<Integer, Integer> dates, Map<Integer,? extends Description> qualities) {
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
		if (getFunctionNum() <= 0x7f) {
			if(qualities != null) {
				this.qualityDescriptionMap = new HashMap<>();
				qualities.forEach((key, value) -> {
					if (value instanceof QualityDescription) {
						this.qualityDescriptionMap.put(key, (QualityDescription) value);
					} else {
						throw new IllegalArgumentException("Quality map contains an incorrect type");
					}
				});
			} else {
				this.qualityDescriptionMap = new HashMap<>();
			}
		} else if (getFunctionNum() <= 0xdf && getFunctionNum() >= 0xa0) {
			if(qualities != null) {
				this.YMqualityDescriptionMap = new HashMap<>();
				qualities.forEach((key, value) -> {
					if (value instanceof YMQualityDescription) {
						this.YMqualityDescriptionMap.put(key, (YMQualityDescription) value);
					} else {
						throw new IllegalArgumentException("Quality map contains an incorrect type");
					}
				});
			} else {
				this.YMqualityDescriptionMap = new HashMap<>();
			}
		}

	}

	public Map getDataJson() throws JsonProcessingException {
		HashMap<String, String> dataMap = new HashMap<>();
		ArrayList<String> dataDetail = new ArrayList<>(2);

		for (Integer i : this.dates.keySet()) {
			dataDetail.add(this.dates.get(i).toString());
			if (getFunctionNum() <= 0x7f){
				dataDetail.add(this.getQualityDescriptionMap().get(i).toString());
			}else if(getFunctionNum() <= 0xdf && getFunctionNum() >= 0xa0){
				dataDetail.add(this.getYMqualityDescriptionMap().get(i).toString());
			}
			dataMap.put(String.valueOf(i), dataDetail.toString());
			dataDetail.clear();
		}
		return dataMap;


	}


	@Override
	public void readDates(byte[] bs) throws InstantiationException, IllegalAccessException {
		//功能码处于00H-7FH之间的是遥测  86H-89H是总加遥测 忽略总加遥测
		//遥测二进制  b14位为1代表溢出,b15位为1 代表无效  有效数据位 为 b0-b10  b11为1时代表负数  以2的补码表述
		if (getFunctionNum() <= 0x7f) {
			this.dates = new HashMap<>(2);
			this.qualityDescriptionMap = new HashMap<>(2);
			this.dates.put(super.getFunctionNum() * 2, decode2Int(bs[0], bs[1]));
			this.qualityDescriptionMap.put(super.getFunctionNum() * 2, new QualityDescription(bs[1]));
			this.dates.put(super.getFunctionNum() * 2 + 1, decode2Int(bs[2], bs[3]));
			this.qualityDescriptionMap.put(super.getFunctionNum() * 2 + 1, new QualityDescription(bs[3]));
		}else if(getFunctionNum() <= 0xdf&& getFunctionNum() >= 0xa0) {
			this.dates = new HashMap<>(1);
			this.YMqualityDescriptionMap = new HashMap<>(1);
			this.dates.put(super.getFunctionNum(), decode2Int(bs[0], bs[1],bs[2],bs[3]));
			this.YMqualityDescriptionMap.put(super.getFunctionNum(), new YMQualityDescription(bs[3]));
		}else if(getFunctionNum() >= 0xf0 && getFunctionNum() <= 0xff) {
			this.dates = new HashMap<>(32);
			System.out.println("===================出现变位遥信=======================");
			BaseDateType dateType = (BooleanDataType) CDTType.YX.typeClass.newInstance();
			dateType.readYBDates(bs,this.getFunctionNum());
			Map dates1 = dateType.getDates();
			this.dates = dates1;
		}
	}

	@Override
	public Map getYBDataJson() {
		HashMap<String, Integer> dataMap = new HashMap<>();
		for (Integer i : this.dates.keySet()) {
			dataMap.put(String.valueOf(i), this.dates.get(i));
		}

		return dataMap;
	}

	/**
	 * 转化成CDT的int型
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	private Integer decode2Int(Byte b1, Byte b2) {
		int i = (b1 & 0xff) | (b2 & 0x07) << 8;
		if ((b2 >> 3 & 0x01) == 1) {
			i = (2048 - i) * -1;
		}
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
		int min = Collections.min(getDates().keySet());
		int val;
		int iVal;
		this.functionNum = min / 2;
		byte[] bytes = new byte[]{(byte) this.functionNum, 0, 0, 0, 0};
		for (int i = 0; i < 2; i++) {
			if (getDates().containsKey(min + i)) {
				val = getDates().get(min + i);
				if (val >= 00) {
					//正数
					bytes[2 * i + 1] = (byte) val;
					bytes[2 * i + 2] = (byte) (((byte) (val >> 8)) & 07);
				} else {
					//负数
					//清零前五位的数值
					iVal = (val * -1) & 0x07ff;
					//先减一再反转
					iVal = 2048 - iVal;
					bytes[2 * i + 1] = (byte) iVal;
					bytes[2 * i + 2] = (byte) (iVal >> 8);
					bytes[2 * i + 2] = (byte) (bytes[2 * i + 2] | (1 << 3));
				}
				if (val > 2047 || val < -2048) {
					//溢出
					bytes[2 * i + 2] = (byte) (bytes[2 * i + 2] | (1 << 6));
				}
				if (getQualityDescriptionMap().containsKey(min + i) &&
						!getQualityDescriptionMap().get(min + i).getInvalid()) {
					//无效
					bytes[2 * i + 2] = (byte) (bytes[2 * i + 2] | (1 << 7));
				}
			}
		}
		byteBuffer.put(bytes);
		byteBuffer.put((byte) CrcUtils.generateCRC8(bytes));
	}


	@Override
	public String toString() {

		String s = "";
		if (this.dates != null) {
			if(this.getFunctionNum() <= 0xdf&& this.getFunctionNum() >= 0xa0){
				for (Integer i : this.getDates().keySet()) {
					s += "第" + i + "路脉冲;值：" + this.getDates().get(i) + " " + getYMqualityDescriptionMap().get(i) + "\n";
				}
			}else if(getFunctionNum() <= 0x7f){
				for (Integer i : this.getDates().keySet()) {
					s += "遥测点位：" + i + ";值：" + this.getDates().get(i) + " " + getQualityDescriptionMap().get(i) + "\n";
				}
			}else if(getFunctionNum() >= 0xf0 && getFunctionNum() <= 0xff){
				for (Integer i : this.getDates().keySet()) {
					s += "遥信点位：" + i + ";值：" + this.getDates().get(i) + " " + "\n";
				}
			}

		}
		return s;
	}


	@Data
	class YMQualityDescription extends Description {
		/**
		 * 是否溢出 false 即为不溢出
		 */
		Boolean uesbcd = false;
		/**
		 * 是否无效 false 即为有效
		 */
		Boolean invalid = false;

		public YMQualityDescription(Byte b) {
			if ((b >> 5 & 0x01) == 1) {
				this.uesbcd = true;
			}
			if ((b >> 7 & 0x01) == 1) {
				this.invalid = true;
			}
		}

		@Override
		public String toString() {
			return (uesbcd ? "BCD表示" : "") + " " + (invalid ? "无效" : "有效");
		}

	}

	@Data
	class QualityDescription extends Description {
		/**
		 * 是否溢出 false 即为不溢出
		 */
		Boolean overflow = false;
		/**
		 * 是否无效 false 即为有效
		 */
		Boolean invalid = false;

		public QualityDescription(Byte b) {
			if ((b >> 6 & 0x01) == 1) {
				this.overflow = true;
			}
			if ((b >> 7 & 0x01) == 1) {
				this.invalid = true;
			}
		}

		@Override
		public String toString() {
			return (overflow ? "溢出" : "") + " " + (invalid ? "无效" : "有效");
		}

	}
}
