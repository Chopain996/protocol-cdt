package wei.yigulu.iec104.asdudataframe;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wei.yigulu.iec104.apdumodel.Apdu;
import wei.yigulu.iec104.apdumodel.Asdu;
import wei.yigulu.iec104.apdumodel.Vsq;
import wei.yigulu.iec104.asdudataframe.qualitydescription.IeMeasuredQuality;
import wei.yigulu.iec104.asdudataframe.typemodel.IeBoolean;
import wei.yigulu.iec104.asdudataframe.typemodel.IeProofreadTime;
import wei.yigulu.iec104.asdudataframe.typemodel.InformationBodyAddress;
import wei.yigulu.iec104.exception.Iec104Exception;
import wei.yigulu.iec104.nettyconfig.TechnicalTerm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单点长时标
 *
 * @author xiuwei
 * @date 2022/03/17
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class BooleanWithTimeType  extends  AbstractDataFrameType{

    private List<InformationBodyAddress> addresses = new ArrayList<>();

    private LinkedHashMap<IeBoolean, IeProofreadTime> datas = new LinkedHashMap<>();
    /**
     * TYPEID
     */
    public static final int TYPEID = TechnicalTerm.SINGEL_POINT_TIME_TYPE;


    @Override
    public void loadByteBuf(ByteBuf is, Vsq vsq) {
        try {
            if (vsq.getSq() == 0) {
                for (int i = 0; i < vsq.getNum(); i++) {
                    addresses.add(new InformationBodyAddress(is));
                    datas.put(new IeBoolean(is),new IeProofreadTime(is));
                }
            } else {
                addresses.add(new InformationBodyAddress(is));
                for (int i = 0; i < vsq.getNum(); i++) {
                    datas.put(new IeBoolean(is),new IeProofreadTime(is));
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


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("单点带时标\n");
        if (addresses.size() == 1) {
            s.append("连续寻址\n");
            s.append(addresses.get(0).toString()).append("\n");
            int d = 0;
            for (Map.Entry<IeBoolean,IeProofreadTime> i : datas.entrySet()) {
                s.append("点位：" + (addresses.get(0).getAddress() + (d++)) + ",");
                s.append("时间："+i.getValue()+"值，:"+i.getKey());

            }
        } else {
            int f = 0;
            for (Map.Entry<IeBoolean,IeProofreadTime> i : datas.entrySet()) {
                s.append(addresses.get(f++).toString());
                s.append("时间："+i.getValue()+"值，:"+i.getKey());
            }
        }
        return s.toString();
    }
}
