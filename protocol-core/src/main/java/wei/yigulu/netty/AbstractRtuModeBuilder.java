package wei.yigulu.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import wei.yigulu.jsc.JSerialCommChannel;
import wei.yigulu.jsc.JSerialCommChannelConfig;
import wei.yigulu.jsc.JSerialCommChannelOption;
import wei.yigulu.jsc.JSerialCommDeviceAddress;


/**
 * 使用rtu的客户端
 *
 * @author: xiuwei
 * @version:
 */
@Accessors(chain = true)
@Slf4j
public abstract class AbstractRtuModeBuilder extends AbstractMasterBuilder {


	/**
	 * com口名称
	 */
	@Getter
	@Setter
	private String commPortId;
	/**
	 * 波特率
	 */
	@Getter
	@Setter
	private int baudRate = 9600;


	/**
	 * 串口读取时间间隔  单位 ms
	 */
	@Getter
	@Setter
	private int readTimeOut = 1000;
	/**
	 * 数据位
	 */
	@Getter
	@Setter
	private JSerialCommChannelConfig.Databits dataBits = JSerialCommChannelConfig.Databits.DATABITS_8;
	/**
	 * 停止位
	 */
	@Getter
	@Setter
	private JSerialCommChannelConfig.Stopbits stopBits = JSerialCommChannelConfig.Stopbits.STOPBITS_1;
	/**
	 * 校验位
	 */
	@Getter
	@Setter
	private JSerialCommChannelConfig.Paritybit parity = JSerialCommChannelConfig.Paritybit.NONE;


	public AbstractRtuModeBuilder(String commPortId) {
		this.commPortId = commPortId;
	}


	@Override
	public void create() {
		try {
			this.future = getOrCreateBootstrap().connect(new JSerialCommDeviceAddress(this.commPortId));
			future.addListener(getOrCreateConnectionListener());
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public EventLoopGroup getOrCreateWorkGroup() {
		if (this.workGroup == null) {
			this.workGroup = new OioEventLoopGroup();
		}
		return this.workGroup;
	}

	@Override
	public Bootstrap getOrCreateBootstrap() {
		if (this.bootstrap == null) {
			this.bootstrap = new Bootstrap();
			bootstrap.group(getOrCreateWorkGroup());
			bootstrap.channel(JSerialCommChannel.class);
			bootstrap.handler(getOrCreateChannelInitializer());
			bootstrap.option(JSerialCommChannelOption.BAUD_RATE, baudRate);
			bootstrap.option(JSerialCommChannelOption.DATA_BITS, dataBits);
			bootstrap.option(JSerialCommChannelOption.STOP_BITS, stopBits);
			bootstrap.option(JSerialCommChannelOption.PARITY_BIT, parity);
			bootstrap.option(JSerialCommChannelOption.READ_TIMEOUT, readTimeOut);
		}

		return this.bootstrap;
	}

	@Override
	public ProtocolConnectionListener getOrCreateConnectionListener() {
		if (this.connectionListener == null) {
			this.connectionListener = new RtuModeConnectionListener(this);
		}
		return this.connectionListener;
	}

	@Override
	protected abstract ProtocolChannelInitializer getOrCreateChannelInitializer();
}
