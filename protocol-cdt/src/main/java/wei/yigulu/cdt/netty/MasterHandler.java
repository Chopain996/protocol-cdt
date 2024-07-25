package wei.yigulu.cdt.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import org.slf4j.Logger;
import wei.yigulu.cdt.cdtframe.BaseDateType;
import wei.yigulu.cdt.cdtframe.CDTFrameBean;
import wei.yigulu.cdt.cdtframe.IntegerDataType;
import wei.yigulu.utils.DataConvertor;
import wei.yigulu.utils.JedisUtil;
import wei.yigulu.utils.JsonBuilder;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 16进制报文解析工具
 *
 * @author xiuwei
 */
@Data
public class MasterHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private static final int MINLEN = 6;
	private Logger log;
	private CDTMaster cdtMaster;

	private final byte[] HEAD = new byte[]{(byte) 0xEB, (byte) 0x90, (byte) 0xEB, (byte) 0x90, (byte) 0xEB, (byte) 0x90};

	public MasterHandler(CDTMaster cdtMaster) {
		this.cdtMaster = cdtMaster;
		this.log = cdtMaster.getLog();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("-----连接串口{}成功-----", this.cdtMaster.getCommPortId());
		this.cdtMaster.getDataHandler().connected();
	}



	/**
	 * channel断连及不稳定时调用的方法
	 *
	 * @param ctx 通道对象
	 * @throws Exception 异常
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.error("串口{}连接中断,正在启动重连机制... ", this.cdtMaster.getCommPortId());
		//在客户端与服务端连接过程中如果断连，就会调用的方法
		this.cdtMaster.getDataHandler().disconnected();
		final EventLoop eventLoop = ctx.channel().eventLoop();
		eventLoop.schedule((Callable) () -> {
			log.info("正在重连串口{}", cdtMaster.getCommPortId());
			cdtMaster.create();
			return null;
		}, 3L, TimeUnit.SECONDS);
	}

	/**
	 * channel连接及传输报错时调用的方法
	 *
	 * @param ctx   通道上下文
	 * @param cause 异常对象
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("串口异常消息:", cause.getMessage());
	}



	JedisUtil jedis = new JedisUtil();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		log.info("接收到串口{}发来数据帧:" + DataConvertor.ByteBuf2String(msg), this.cdtMaster.getCommPortId());

		if (msg.readableBytes() > MINLEN) {
			CDTFrameBean cdtFrameBean = new CDTFrameBean(msg);
			this.cdtMaster.getDataHandler().processFrame(cdtFrameBean);
			log.info(cdtFrameBean.toString());

			//构建Json
			Map<String, Object> jsonMap = new LinkedHashMap<>();
			jsonMap.put("消息类型",cdtFrameBean.getCdtType().toString());
			jsonMap.put("信息字数",cdtFrameBean.getNum());
			jsonMap.put("源站址",cdtFrameBean.getSourceAddress());
			jsonMap.put("目的站址",cdtFrameBean.getDestinationAddress());
			// 获取DataJsonString
			List dataList = new ArrayList<>();
			List<BaseDateType> dates = cdtFrameBean.getDates();
			int i=0;
			for (BaseDateType date : dates) {
				if (date instanceof IntegerDataType){
//					datasMap.put(("信息字"+i),date.getDataJson());
					dataList.add(date.getDataJson());
				}else {
//					datasMap.put(("data"),date.getDataJson());
					dataList.add(date.getDataJson());
				}
				i++;
			}
			jsonMap.put("Datas",dataList);
			String res = JsonBuilder.JsonToString(jsonMap);
			//Jedis存储
			jedis.setValue(res);
		}
	}


}
