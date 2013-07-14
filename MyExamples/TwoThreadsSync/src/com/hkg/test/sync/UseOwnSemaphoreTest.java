package com.hkg.test.sync;

import java.util.concurrent.Semaphore;

public class UseOwnSemaphoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinarySemaphore a = new BinarySemaphore(1);  // first thread is allowed to run immediately
		BinarySemaphore b = new BinarySemaphore(0); // second thread has to wait
		ThreadPrint tp1 = new ThreadPrint(a, b);
		ThreadPrint tp2 = new ThreadPrint(b, a); 
		Thread t1 = new Thread(tp1);
		Thread t2 = new Thread(tp2);
		t1.start();
		t2.start();
		

	}

}

class ThreadPrint implements Runnable {
	BinarySemaphore ins, outs;

    ThreadPrint(BinarySemaphore ins, BinarySemaphore outs) {
        this.ins = ins;
        this.outs = outs;
    }

    @Override
    public void run() {
    	while(true){
            try {
				ins.waitForNotify();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // wait for permission to run
            System.out.println("Hi " + Thread.currentThread().getName());
            outs.notifyToWakeup();  // allow another thread to run
        }
    }	
    }


class BinarySemaphore {
	  private boolean locked = false;
	 
	  BinarySemaphore(int initial) {
	    locked = (initial == 0);
	  }
	 
	  public synchronized void waitForNotify() throws InterruptedException {
	    while (locked) {
	      wait();
	    }
	    locked = true;
	  }
	 
	  public synchronized void notifyToWakeup() {
	    if (locked) {
	      notify();
	    }
	    locked = false;
	  }
	}