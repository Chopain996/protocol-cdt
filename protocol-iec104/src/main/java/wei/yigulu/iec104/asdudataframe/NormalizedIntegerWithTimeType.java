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
import wei.yigulu.iec104.asdudataframe.typemodel.IeShortInteger;
import wei.yigulu.iec104.asdudataframe.typemodel.InformationBodyAddress;
import wei.yigulu.iec104.exception.Iec104Exception;
import wei.yigulu.iec104.nettyconfig.TechnicalTerm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 104的归一化值数据帧数据帧
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@NoArgsConstructor
@Data
public class NormalizedIntegerWithTimeType extends AbstractDataFrameType {

    /**
     * TYPEID
     */
    public static final int TYPEID = TechnicalTerm.NORMALIZED_INTEGER_TIME_TYPE;

    private List<InformationBodyAddress> addresses = new ArrayList<>();

    private List<NormalizedIntegerAndQualityWithTime> datas = new ArrayList<>();



    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("带时长归一化值");
        if (addresses.size() == 1) {
            s.append("连续寻址\n");
            s.append(addresses.get(0).toString() + "\n");
            int i = 0;
            for (NormalizedIntegerAndQualityWithTime e : datas) {
                s.append("点位：" + (addresses.get(0).getAddress() + (i++)) + ",");
                s.append("时间为："+e.getTime());
                s.append("值为 ：" + e.getValue() + ";" + e.getQuality() + "\n");
            }
        } else {
            s.append("单一寻址\n");
            int f = 0;
            for (NormalizedIntegerAndQualityWithTime e : datas) {
                s.append(addresses.get(f++).toString());
                s.append("时间为："+e.getTime());
                s.append("值为 ：" + e.getValue() + ";" + e.getQuality() + "\n");
            }
        }
        return s.toString();
    }

    @Override
    public void loadByteBuf(ByteBuf is, Vsq vsq) {
        try {
            if (vsq.getSq() == 0) {
                for (int i = 0; i < vsq.getNum(); i++) {
                    addresses.add(new InformationBodyAddress(is));
                    datas.add(new NormalizedIntegerAndQualityWithTime(is));
                }
            } else {
                addresses.add(new InformationBodyAddress(is));
                for (int i = 0; i < vsq.getNum(); i++) {
                    datas.add(new NormalizedIntegerAndQualityWithTime(is));
                }
            }
        } catch (Iec104Exception e) {
            if (e.getCode() == 3301) {
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
        asdu.setTypeId(TYPEID);
        asdu.setDataFrame(this);
        asdu.getVsq().setSq(this.addresses.size() == 1 ? 1 : 0);
        asdu.getVsq().setNum(this.datas.size());
        asdu.setOriginatorAddress(0);
        asdu.setCommonAddress(1);
        return asdu;
    }

    @Override
    public byte[][] handleAndAnswer(Apdu apdu) throws Exception {
        return new byte[0][];
    }

    @AllArgsConstructor
    @Data
   public  class NormalizedIntegerAndQualityWithTime{

        Integer value;
        IeMeasuredQuality quality;
        IeProofreadTime time;

        public  NormalizedIntegerAndQualityWithTime(ByteBuf is) throws Iec104Exception {
            this.value=new IeShortInteger(is).getValue();
            this.quality=new IeMeasuredQuality(is);
            this.time=new IeProofreadTime(is);
        }
    }
}
