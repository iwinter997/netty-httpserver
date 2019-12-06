package net.iwinter.core.route;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.iwinter.core.annotation.RequestMapping;
import net.iwinter.core.constant.HttpServerConstant;
import net.iwinter.core.handle.HandlerRequestMapping;
import net.iwinter.core.handle.InstanceParamMapping;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/16 13:46
 */
public class RouteProcess {
    private static final Map<String, InstanceParamMapping> requestUrlMap = HandlerRequestMapping.requestUrlMap;

    /**
     * 处理请求
     *
     * @param channelHandlerContext
     * @param paramMapping          执行实例
     * @param param                 请求参数
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void excute(ChannelHandlerContext channelHandlerContext, InstanceParamMapping paramMapping, Object... param) throws InvocationTargetException, IllegalAccessException {
        Method method = paramMapping.getMethod();
        method.setAccessible(true);
        Object invoke = method.invoke(paramMapping.getClazz(), param);

        Object responseType = HttpServerConstant.ContentType.TEXT;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            responseType = method.getAnnotation(RequestMapping.class).responseType();
            invoke = JSONObject.toJSON(invoke);
        }
        RouteProcess.responsContent(channelHandlerContext, responseType, invoke);
    }

    /**
     * 验证请求地址
     *
     * @param queryStringDecoder 解码器
     * @return
     */
    public static String validateRequestUrl(QueryStringDecoder queryStringDecoder) {
        String uri = queryStringDecoder.uri();
        if (queryStringDecoder.uri().contains("?")) {
            uri = queryStringDecoder.uri().split("\\?")[0];
        }
        if (requestUrlMap.containsKey(uri)) {
            return uri;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解析路由参数
     *
     * @param paramMapping       执行实例
     * @param queryStringDecoder 解码器
     * @return
     */
    public static Object[] parseRouteParameter(InstanceParamMapping paramMapping, QueryStringDecoder queryStringDecoder) {
        Class<?>[] parameterTypes = paramMapping.getMethod().getParameterTypes();
        if (Objects.isNull(parameterTypes) || parameterTypes.length == 0) {
            return new Object[0];
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

    /**
     * 响应执行结果
     *
     * @param channelHandlerContext
     * @param responseType          响应类型
     * @param msg                   响应文本
     */
    private static void responsContent(ChannelHandlerContext channelHandlerContext, Object responseType, Object msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSONObject.toJSONString(msg), CharsetUtil.UTF_8)
        );
        response.headers().set("Content-Type", responseType);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
