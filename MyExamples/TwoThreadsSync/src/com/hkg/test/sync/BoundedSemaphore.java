package com.hkg.test.sync;

public class BoundedSemaphore {
	private int signals = 0;
	  private int bound   = 0;

	  public BoundedSemaphore(int upperBound){
	    this.bound = upperBound;
	  }

	  public synchronized void waitForNotify() throws InterruptedException{
	    while(this.signals == bound) wait();
	    this.signals++;
	    this.notify();
	  }

	  public synchronized void notifyToWakeup() throws InterruptedException{
	    while(this.signals == 0) wait();
	    this.signals--;
	    this.notify();
	  }
}
