package com.hkg.test.sync;

public class CountingSemaphore {
	private int locked = 0;
	 
	 
	  public synchronized void waitForNotify() throws InterruptedException {
		  this.locked++;
		    this.notify();
	  }
	 
	  public synchronized void notifyToWakeup() throws InterruptedException {
		  while(this.locked == 0) wait();
		    this.locked--;
	  }
}
