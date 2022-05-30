package wei.yigulu.iec104.asdudataframe.typemodel;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import wei.yigulu.iec104.exception.Iec104Exception;

import java.util.List;

/**
 * 对时帧的具体时标实体类 cp56
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IeProofreadTime {
	public static final int OCCUPYBYTES = 7;

	private DateTime time = new DateTime();

	private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss:SSS");

	/**
	 * Ie proofread time
	 *
	 * @param is is
	 */
	public IeProofreadTime(ByteBuf is) throws Iec104Exception {
		if (is.readableBytes() < OCCUPYBYTES) {
			throw new Iec104Exception(3301, "可用字节不足，不能进行读取");
		}
		byte[] btime = new byte[7];
		is.readBytes(btime);
		int milliSecond = (btime[0] & 0xff) + ((btime[1] & 0xff) << 8);
		int minute = btime[2] & 0x3f;
		int hour = btime[3] & 0x1f;
		int day = btime[4] & 0x1f;
		int month = btime[5] & 0x0f;
		int year = btime[6] & 0x7f;
		String s = "20" + String.format("%02d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " "
				+ String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", milliSecond / 1000) + ":" +
				String.format("%02d", milliSecond % 1000);
		time = FORMATTER.parseDateTime(s);
	}

	/**
	 * Encode *
	 *
	 * @param buffer buffer
	 */
	public void encode(List<Byte> buffer) {
		int year = time.getYear();
		int month = time.getMonthOfYear();
		int day = time.getDayOfMonth();
		int hour = time.getHourOfDay();
		int minute = time.getMinuteOfHour();
		int second = time.getSecondOfMinute();
		int milliSecond = time.getMillisOfSecond();
		int nums = second * 1000 + milliSecond;
		buffer.add((byte) (nums&0xff));
		buffer.add((byte) (nums&0xff00>>8));
		buffer.add((byte) (minute&0xff));
		buffer.add((byte) (hour&0xff));
		buffer.add((byte) (day&0xff));
		buffer.add((byte) (month&0xff));
		buffer.add((byte) ((year - 2000)&0xff));
	}

	@Override
	public String toString() {
		return FORMATTER.print(time.getMillis());
	}
}
