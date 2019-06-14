package com.xcoder.iotserver.handler;

/**
 * Io handler
 *
 * @param <I> input
 * @param <O> output
 * @author Chuck Lee
 * @date 2019-06-14
 */
public interface IIoHandler<I, O> {
    /**
     * Handle http request
     *
     * @param i i
     * @param o o
     */
    void handle(I i, O o);
}