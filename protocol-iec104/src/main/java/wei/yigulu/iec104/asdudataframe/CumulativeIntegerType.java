package wei.yigulu.iec104.asdudataframe;


import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.apdumodel.Asdu;
import wei.yigulu.iec104.apdumodel.Vsq;
import wei.yigulu.iec104.asdudataframe.qualitydescription.IeCounterReadingQuality;
import wei.yigulu.iec104.asdudataframe.qualitydescription.IeMeasuredQuality;
import wei.yigulu.iec104.asdudataframe.typemodel.IeFourByteInteger;
import wei.yigulu.iec104.asdudataframe.typemodel.IeShortFloat;
import wei.yigulu.iec104.asdudataframe.typemodel.InformationBodyAddress;
import wei.yigulu.iec104.exception.Iec104Exception;
import wei.yigulu.iec104.nettyconfig.TechnicalTerm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 电能累计值报文
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@NoArgsConstructor
@Data
public class CumulativeIntegerType extends AbstractDataFrameType {

	/**
	 * TYPEID
	 */
	public static final int TYPEID = TechnicalTerm.CUMULATIVE_ELECTRIC_ENERGY_MEASUREMENT;

	private List<InformationBodyAddress> addresses = new ArrayList<>();

	private Map<IeCounterReadingQuality, IeFourByteInteger> datas = new LinkedHashMap<>();





	@Override
	public void encode(List<Byte> buffer) {
		if (addresses.size() == 1) {
			addresses.get(0).encode(buffer);
			for (Map.Entry<IeCounterReadingQuality, IeFourByteInteger> i : datas.entrySet()) {
				i.getValue().encode(buffer);
				buffer.add((byte) i.getKey().encode());
			}
		} else {
			int s = 0;
			for (Map.Entry<IeCounterReadingQuality, IeFourByteInteger> i : datas.entrySet()) {
				addresses.get(s++).encode(buffer);
				i.getValue().encode(buffer);
				buffer.add((byte) i.getKey().encode());
			}
		}

	}

	@Override
	public Asdu generateBack() {
		Asdu asdu = new Asdu();
		asdu.setTypeId(TYPEID);
		asdu.setDataFrame(this);
		asdu.getVsq().setSq(this.addresses.size() == 1 ? 1 : 0);
		asdu.getVsq().setNum(this.datas.size());
		asdu.setOriginatorAddress(0);
		asdu.setCommonAddress(1);
		return asdu;
	}


	/**
	 * Validate len *
	 *
	 * @param increase increase
	 * @throws Iec104Exception iec exception
	 */
	protected void validateLen(int increase) throws Iec104Exception {
		if ((this.datas.size() * (IeShortFloat.OCCUPYBYTES+IeMeasuredQuality.OCCUPYBYTES) + this.addresses.size() * InformationBodyAddress.OCCUPYBYTES + increase) > 240) {
			throw new Iec104Exception("长度超长，不能再向此对象中添加元素");
		}
	}

	@Override
	public void loadByteBuf(ByteBuf is, Vsq vsq) {
		IeFourByteInteger value;
		try {
			if (vsq.getSq() == 0) {
				for (int i = 0; i < vsq.getNum(); i++) {
					addresses.add(new InformationBodyAddress(is));
					value=new IeFourByteInteger(is);
					datas.put(new IeCounterReadingQuality(is), value);

				}
			} else {
				addresses.add(new InformationBodyAddress(is));
				for (int i = 0; i < vsq.getNum(); i++) {
					value=new IeFourByteInteger(is);
					datas.put(new IeCounterReadingQuality(is), value);
				}
			}
		}catch (Iec104Exception e){
			if(e.getCode()==3301){
				return;
			}
		}
	}

	@Override
	public byte[][] handleAndAnswer(Apdu apdu) throws Exception {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("电度累计值");
		if (addresses.size() == 1) {
			s.append( "连续寻址\n");
			s.append(addresses.get(0).toString() + "\n");
			int i=0;
			for (Map.Entry<IeCounterReadingQuality, IeFourByteInteger> e : datas.entrySet()) {
				s.append("点位："+(addresses.get(0).getAddress()+(i++))+",");
				s.append( "值为 ：" + e.getValue() + ";" + e.getKey().toString() + "\n");
			}
		} else {
			s.append("单一寻址\n");
			int f = 0;
			for (Map.Entry<IeCounterReadingQuality, IeFourByteInteger> i : datas.entrySet()) {
				s.append( addresses.get(f++).toString());
				s.append( i.getValue().toString());
				s.append( i.getKey().toString() + "\n");
			}
		}
		return s.toString();
	}





}
