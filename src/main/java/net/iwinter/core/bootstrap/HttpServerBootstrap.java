package net.iwinter.core.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.iwinter.core.config.HttpServerConfig;
import net.iwinter.core.handle.HandlerRequestMapping;
import net.iwinter.core.handle.HttpServerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:58
 */
public class HttpServerBootstrap {
    private final static Logger log = LoggerFactory.getLogger(HttpServerBootstrap.class);

    private final static EventLoopGroup boss = new NioEventLoopGroup();
    private final static EventLoopGroup work = new NioEventLoopGroup();
    private static Channel channel;

    public static void run(Class clazz) {
        try {
            initRequestMapping(clazz);
            initServer();
            stopServer();
        } catch (InterruptedException e) {
            log.error("http server start erro", e);
        } catch (Exception e) {
            log.error("http server start erro", e);
        }
    }

    private static void initServer() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());

        Integer serverPort = HttpServerConfig.getServerPort();
        ChannelFuture channelFuture = bindServerPort(serverBootstrap, serverPort);
        channel = channelFuture.channel();
    }

    private static ChannelFuture bindServerPort(ServerBootstrap bootstrap, Integer port) throws InterruptedException {
        ChannelFuture bind = bootstrap.bind(port).sync();
        if (!bind.isSuccess()) {
            HttpServerConfig.setServerPort(port + 1);
            bindServerPort(bootstrap, HttpServerConfig.getServerPort());
        }
        log.error("server started port:{}", HttpServerConfig.getServerPort());
        return bind;
    }

    private static void stopServer() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                channel.close();
                boss.shutdownGracefully();
                work.shutdownGracefully();

                log.error("server stop port:{}", HttpServerConfig.getServerPort());
            }
        });
    }

    private static void initRequestMapping(Class clazz) throws Exception {
        log.info("init request mapping package:{}", clazz.getPackage());
        HttpServerConfig.setScanPath(clazz.getPackage().getName());

        HandlerRequestMapping.handelRequestUrl(clazz);
    }
}
