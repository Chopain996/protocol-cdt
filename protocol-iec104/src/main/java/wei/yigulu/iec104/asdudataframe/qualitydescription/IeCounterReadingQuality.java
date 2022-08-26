package wei.yigulu.iec104.asdudataframe.qualitydescription;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wei.yigulu.iec104.exception.Iec104Exception;


/**
 * 计数器读数品质描述 的抽象类
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IeCounterReadingQuality {


    @Override
    public boolean equals(Object o) {
        return false;
    }

    public static final int OCCUPYBYTES = 1;

    /**
     * 品质的值
     */
    protected int value;

    /**
     * 顺序号
     */
    protected int sequence;

    /**
     * 溢出标识  0-未溢出；1溢出
     */
    protected boolean overflow;

    /**
     * 调整标志 0未被调整；1被调整
     */
    protected boolean adjust;

    /**
     * 有效标志 0有效；1无效
     */
    protected boolean invalid;


    /**
     * Ie abstract quality
     *
     * @param is is
     */
    public IeCounterReadingQuality(ByteBuf is) throws Iec104Exception {
        if (is.readableBytes() < OCCUPYBYTES) {
            throw new Iec104Exception(3301, "可用字节不足，不能进行读取");
        }
        this.value = (is.readByte() & 0xff);
        this.sequence = value & 0x1f;
        this.overflow = (value & 0x20) == 0x20;
        this.adjust = (value & 0x40) == 0x40;
        this.invalid = (value & 0x80) == 0x80;
    }


    /**
     * 描述品质位 为1位
     *
     * @return int
     */
    public int encode() {
        int v = 0x00;
        v |= sequence;
        if (overflow) {
            v |= 0x20;
        }
        if (adjust) {
            v |= 0x40;
        }
        if (invalid) {
            v |= 0x80;
        }
        return v;
    }

    /**
     * 是否是良好值
     *
     * @return boolean
     */
    public boolean isGoodValue() {
        return !this.isOverflow() && !isInvalid() && !isAdjust();
    }

    @Override
    public String toString() {
        return "溢出: " + isOverflow() + ", 无效: " + isInvalid() + ", 被调整: " + isAdjust();
    }
}
