package wei.yigulu.iec104.asdudataframe;


import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.apdumodel.Asdu;
import wei.yigulu.iec104.apdumodel.Vsq;
import wei.yigulu.iec104.asdudataframe.qualitydescription.IeMeasuredQuality;
import wei.yigulu.iec104.asdudataframe.typemodel.IeProofreadTime;
import wei.yigulu.iec104.asdudataframe.typemodel.IeShortFloat;
import wei.yigulu.iec104.asdudataframe.typemodel.IeShortInteger;
import wei.yigulu.iec104.asdudataframe.typemodel.InformationBodyAddress;
import wei.yigulu.iec104.exception.Iec104Exception;
import wei.yigulu.iec104.nettyconfig.TechnicalTerm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 104的标度化值数据帧
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@NoArgsConstructor
@Data
public class ShortIntegerWithTimeType extends AbstractDataFrameType {

	/**
	 * TYPEID
	 */
	public static final int TYPEID = TechnicalTerm.SHORT_INTEGER_TYPE_TIME;

	private List<InformationBodyAddress> addresses = new ArrayList<>();

	private List<ShortIntegerAndQualityWithTime> datas = new ArrayList<>();


	@Override
	public void loadByteBuf(ByteBuf is, Vsq vsq) {
		try {
			if (vsq.getSq() == 0) {
				for (int i = 0; i < vsq.getNum(); i++) {
					addresses.add(new InformationBodyAddress(is));
					datas.add(new ShortIntegerAndQualityWithTime(is));
				}
			} else {
				addresses.add(new InformationBodyAddress(is));
				for (int i = 0; i < vsq.getNum(); i++) {
					datas.add(new ShortIntegerAndQualityWithTime(is));
				}
			}
		}catch (Iec104Exception e){
			if(e.getCode()==3301){
				return;
			}
		}
	}


	@Override
	public void encode(List<Byte> buffer) {

	}

	@Override
	public Asdu generateBack() {
		Asdu asdu = new Asdu();
		asdu.setTypeId(35);
		asdu.setDataFrame(this);
		asdu.getVsq().setSq(this.addresses.size() == 1 ? 1 : 0);
		asdu.getVsq().setNum(this.datas.size());
		asdu.setOriginatorAddress(0);
		asdu.setCommonAddress(1);
		return asdu;
	}



	@Override
	public byte[][] handleAndAnswer(Apdu apdu) throws Exception {
		return null;
	}


	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("带时长标度化值");
		if (addresses.size() == 1) {
			s.append("连续寻址\n");
			s.append(addresses.get(0).toString() + "\n");
			int i=0;
			for (ShortIntegerAndQualityWithTime e : datas) {
				s.append("点位："+(addresses.get(0).getAddress()+(i++))+",");
				s.append("值为 ：" + e.getValue() + ";" + e.getQuality() + "; 时间："+e.getTime()+"\n");
			}
		} else {
			s.append("单一寻址\n");
			int f = 0;
			for (ShortIntegerAndQualityWithTime i : datas) {
				s.append("点位："+(addresses.get(f++).getAddress())+",");
				s.append("值为 ：" + i.getValue() + ";" + i.getQuality() + "; 时间："+i.getTime()+"\n");
			}
		}
		return s.toString();
	}


	@AllArgsConstructor
	@Data
	class ShortIntegerAndQualityWithTime {

		Integer value;
		IeMeasuredQuality quality;
		IeProofreadTime time;

		public ShortIntegerAndQualityWithTime(ByteBuf is) throws Iec104Exception {
			this.value=new IeShortInteger(is).getValue();
			this.quality=new IeMeasuredQuality(is);
			this.time=new IeProofreadTime(is);
		}
	}
}
