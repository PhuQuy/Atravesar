package com.example.npquy.service;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 
 * @author npquy
 *
 */
public enum StopHandler implements Serializable {

	IS_RUNNING(0);
	private volatile int isRunning;

	private StopHandler(int isRunning) {
		this.isRunning=isRunning;
	}

	public int isRunning() {
		return isRunning;
	}

	public void setRunning(int isRunning) {
		this.isRunning = isRunning;
	}

	private static class LazyInit {
		private static StopHandler SINGLETON = StopHandler.IS_RUNNING;

	}

	private Object writeReplace() throws ObjectStreamException {
		return createInstance();
	}

	public static StopHandler createInstance() {
		return LazyInit.SINGLETON;
	}
}
