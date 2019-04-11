package net.iwinter.core.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 17:33
 */
@ChannelHandler.Sharable
public class HttpServerDispatcherHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {
    private static final Logger log = LoggerFactory.getLogger(HttpServerDispatcherHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DefaultHttpRequest defaultHttpRequest) throws Exception {
        log.info("request url:{}", defaultHttpRequest.getUri());

        // TODO 进行中
    }
}
