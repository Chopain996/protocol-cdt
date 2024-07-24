package wei.yigulu.cdt.netty;


import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Getter;
import wei.yigulu.cdt.cdtframe.AbstractCDTDataHandler;
import wei.yigulu.jsc.JSerialCommChannel;
import wei.yigulu.netty.AbstractRtuModeBuilder;
import wei.yigulu.netty.ProtocolChannelInitializer;
import wei.yigulu.purejavacomm.PureJavaCommChannel;


/**
 * cdt读取端
 *
 * @author 修唯xiuwei
 **/
public class CDTMaster extends AbstractRtuModeBuilder {

	private static final int MAXLEN = 10240;



	@Getter
	private final AbstractCDTDataHandler dataHandler;

	public CDTMaster(String commPortId,int baudRate, AbstractCDTDataHandler dataHandler) {
		super(commPortId,baudRate);
		this.dataHandler = dataHandler;
	}


	@Override
	protected ProtocolChannelInitializer getOrCreateChannelInitializer() {
		return new ProtocolChannelInitializer<JSerialCommChannel>(this) {
			AllCustomDelimiterHandler myhandler=new AllCustomDelimiterHandler();

			@Override
			protected void initChannel(JSerialCommChannel ch) throws Exception {
				//ch.pipeline().addLast(new DelimiterBasedFrameDecoder(MAXLEN, Unpooled.copiedBuffer(HEAD)));
				ch.pipeline().addLast(myhandler);
				ch.pipeline().addLast(new MasterHandler((CDTMaster) builder));
			}
		};
	}
}
