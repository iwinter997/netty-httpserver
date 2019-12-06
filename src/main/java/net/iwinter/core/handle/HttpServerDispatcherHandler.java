package net.iwinter.core.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import net.iwinter.core.route.RouteProcess;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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

    private static final Map<String, InstanceParamMapping> requestUrlMap = HandlerRequestMapping.requestUrlMap;
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2, 1024 >> 1,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new HttpServerDispatcherHandlerReject()
    );

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DefaultHttpRequest defaultHttpRequest) {
        threadPoolExecutor.execute(new Thread(() -> {
            try {
                String uri = defaultHttpRequest.getUri();
                log.info("HttpServerDispatcherHandler -> channelRead0() -> param:[{}]", uri);
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(uri), CharsetUtil.UTF_8);
                String requestUrl = RouteProcess.validateRequestUrl(queryStringDecoder);
                if (StringUtils.isEmpty(requestUrl)) {
                    channelHandlerContext.close();
                    throw new NullPointerException(defaultHttpRequest.getUri());
                }
                InstanceParamMapping paramMapping = requestUrlMap.get(requestUrl);
                Object[] objects = RouteProcess.parseRouteParameter(paramMapping, queryStringDecoder);
                RouteProcess.excute(channelHandlerContext, paramMapping, objects);
            } catch (Exception e) {
                log.error("HttpServerDispatcherHandler -> channelRead0() -> error:[{}]", e);
            }
        }));
    }

}

/**
 * 自定义线程池拒绝策略
 */
class HttpServerDispatcherHandlerReject implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        r.run();
    }
}