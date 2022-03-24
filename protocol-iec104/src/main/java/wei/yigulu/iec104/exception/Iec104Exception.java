package wei.yigulu.iec104.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IEC104的异常类
 *
 * @author 修唯xiuwei
 * @version 3.0
 */
@Data
public class Iec104Exception extends Exception {


	/**
	 * Iec exception
	 *
	 * @param msg msg
	 */
	public Iec104Exception(int code,String msg) {
		super(msg);
		this.code=code;
		this.msg = msg;
	}

	/**
	 * Iec exception
	 *
	 * @param msg msg
	 */
	public Iec104Exception(String msg) {
		super(msg);
		this.msg = msg;
	}


	private int code;
	private String msg;

}
