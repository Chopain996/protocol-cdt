package wei.yigulu.cdt.netty;

import lombok.Getter;
import wei.yigulu.cdt.cdtframe.AbstractCDTDataTransmitter;
import wei.yigulu.jsc.JSerialCommChannel;
import wei.yigulu.netty.AbstractRtuModeBuilder;
import wei.yigulu.netty.ProtocolChannelInitializer;
import wei.yigulu.purejavacomm.PureJavaCommChannel;


/**
 * cdt
 *
 * @author: xiuwei
 * @version:
 */
public class CDTSlaver extends AbstractRtuModeBuilder {

	@Getter
	private final AbstractCDTDataTransmitter dataTransmitter;

	public CDTSlaver(String commPortId, int baudRate,AbstractCDTDataTransmitter dataTransmitter) {
		super(commPortId,baudRate);
		this.dataTransmitter = dataTransmitter;
	}


	@Override
	protected ProtocolChannelInitializer getOrCreateChannelInitializer() {
		return new ProtocolChannelInitializer<JSerialCommChannel>(this) {

			@Override
			protected void initChannel(JSerialCommChannel ch) throws Exception {
				ch.pipeline().addLast(new SlaverHandler((CDTSlaver) builder));
			}
		};
	}
}
