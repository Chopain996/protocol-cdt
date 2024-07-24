package wei.yigulu.cdt.cdtframe;

/**
 * CDT 数据类型
 *
 * @author xiuwei
 */
public enum CDTType {


	IMPORTANTYC("重要遥测(A帧)", 0x61, IntegerDataType.class),
	SECONDYC("次要遥测(B帧)", 0xc2, IntegerDataType.class),
	COMMONYC("一般遥测(C帧)", 0xb3, IntegerDataType.class),
	YX("遥信状态(D1帧)", 0xf4, BooleanDataType.class),
	YM("遥脉(D2帧)",0x85,IntegerDataType.class);

	String name;
	int no;
	Class typeClass;

	CDTType(String name, int no, Class typeClass) {
		this.name = name;
		this.no = no;
		this.typeClass = typeClass;
	}

	public static CDTType getByNo(int no) {
		switch (no & 0xff) {
			case 0x61:
				return IMPORTANTYC;
			case 0xc2:
				return SECONDYC;
			case 0xb3:
				return COMMONYC;
			case 0xf4:
				return YX;
			case 0x85:
				return YM;
			default:
				return null;
		}
	}
}
