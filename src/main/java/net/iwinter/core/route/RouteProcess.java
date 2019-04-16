package net.iwinter.core.route;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.iwinter.core.annotation.HttpResponseBody;
import net.iwinter.core.bean.HandleRequestURL;
import net.iwinter.core.constant.HttpServerConstant;
import net.iwinter.core.handle.HandlerRequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 13:46
 */
public class RouteProcess {
    private static final Map<String, HandleRequestURL> requestUrlMap = HandlerRequestMapping.requestUrlMap;

    public static void excute(ChannelHandlerContext channelHandlerContext, HandleRequestURL requestURL, Object... param) throws InvocationTargetException, IllegalAccessException {
        Method method = requestURL.getMethod();
        Object clazz = requestURL.getClazz();

        method.setAccessible(true);
        Object invoke = method.invoke(clazz, param);

        Object contentType = HttpServerConstant.ContentType.TEXT;
        if (method.isAnnotationPresent(HttpResponseBody.class)) {
            contentType = method.getAnnotation(HttpResponseBody.class).value();
            invoke = JSONObject.toJSON(invoke);
        }
        RouteProcess.responsContent(channelHandlerContext, contentType, invoke);
    }


    public static String validateRequestUrl(QueryStringDecoder queryStringDecoder) {
        String uri = queryStringDecoder.uri();
        if (queryStringDecoder.uri().contains("?")) {
            uri = queryStringDecoder.uri().split("\\?")[0];
        }

        if (requestUrlMap.containsKey(uri)) {
            return uri;
        }
        return "";
    }

    public static Object[] parseRouteParameter(HandleRequestURL requestURL, QueryStringDecoder queryStringDecoder) {
        Class<?>[] parameterTypes = requestURL.getMethod().getParameterTypes();

        if (parameterTypes.length == 0) {
            return null;
        }

        Object[] instances = new Object[parameterTypes.length];
        for (int i = 0; i < instances.length; i++) {
            Object instance = null;
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            for (Map.Entry<String, List<String>> param : parameters.entrySet()) {
                instance = param.getValue().get(0);
            }
            instances[i] = instance;
        }
        return instances;
    }

    private static void responsContent(ChannelHandlerContext channelHandlerContext, Object contentType, Object msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSONObject.toJSONString(msg), CharsetUtil.UTF_8)
        );

        response.headers().set("Content-Type", contentType);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
