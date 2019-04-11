package net.iwinter.example;

import net.iwinter.core.bootstrap.HttpServerBootstrap;

/**
 * @author: luogang
 * @email: iwinter997@gmail.com
 * @createDate: 2019/04/10 16:13
 */
public class HttpServerApplication {

    public static void main(String[] args) {
        HttpServerBootstrap.run(HttpServerApplication.class);
    }
}
