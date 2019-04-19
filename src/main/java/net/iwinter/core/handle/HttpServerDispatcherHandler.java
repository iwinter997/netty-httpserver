package net.iwinter.core.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import net.iwinter.core.bean.HandleRequestURL;
import net.iwinter.core.route.RouteProcess;
import net.iwinter.core.thread.HandleRequestThread;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 17:33
 */
@ChannelHandler.Sharable
public class HttpServerDispatcherHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {
    private static final Logger log = LoggerFactory.getLogger(HttpServerDispatcherHandler.class);

    private static final Map<String, HandleRequestURL> requestUrlMap = HandlerRequestMapping.requestUrlMap;
    private static final ThreadPoolExecutor threadPoolExecutor =
                                            new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(),new HandleRequestThread());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DefaultHttpRequest defaultHttpRequest){
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("request url:{}", defaultHttpRequest.getUri());
                    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(defaultHttpRequest.getUri()), CharsetUtil.UTF_8);

                    String requestUrl = RouteProcess.validateRequestUrl(queryStringDecoder);
                    if (StringUtils.isEmpty(requestUrl)) {
                        channelHandlerContext.close();
                        throw new NullPointerException(defaultHttpRequest.getUri());
                    }

                    HandleRequestURL requestURL = requestUrlMap.get(requestUrl);
                    Object[] objects = RouteProcess.parseRouteParameter(requestURL, queryStringDecoder);
                    RouteProcess.excute(channelHandlerContext, requestURL, objects);
                } catch (Exception e) {
                    log.warn("handle request erro:{}",e);
                }
            }
        });
    }

}
