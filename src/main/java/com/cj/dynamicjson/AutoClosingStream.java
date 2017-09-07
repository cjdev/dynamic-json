package com.cj.dynamicjson;

import java.util.stream.Stream;

/**
 * Automatically closes streams when they have been fully consumed or when close() is called on the outer stream.  Works with try-with-resources.
 * Crazy flatmap hack taken from:
 *     https://stackoverflow.com/questions/43609062/using-autoclosable-interfaces-inside-stream-api
 * Don't forget to attach an onClose method to the stream you pass in.
 * @author dron
 *
 */
public interface AutoClosingStream {
	public static <T> Stream<T> autoClosingStream(Runnable closeMethod, Stream<T> innerStream){
		return Stream.of(1).flatMap(i->innerStream.onClose(closeMethod)).onClose(closeMethod);
	}

}
