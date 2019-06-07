package com.xcoder.iotserver.server;

/**
 * Http request handler.
 *
 * @param <T> any
 * @param <R> any
 * @author Chuckm Lee
 * @date 2019-06-03
 */
public interface IHandler<T, R> {
    /**
     * Handle http request
     *
     * @param t t
     * @return object
     */
    R handle(T t);
}
