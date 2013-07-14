package com.hkg.test.sync;

import java.util.concurrent.Semaphore;

public class SemaphoreToSyncTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Semaphore a = new Semaphore(1);  // first thread is allowed to run immediately
		Semaphore b = new Semaphore(0); // second thread has to wait
		ThreadPrinter tp1 = new ThreadPrinter(a, b);
		ThreadPrinter tp2 = new ThreadPrinter(b, a); 
		Thread t1 = new Thread(tp1);
		Thread t2 = new Thread(tp2);
		t1.start();
		t2.start();
		
	}

}
class ThreadPrinter implements Runnable {
    Semaphore ins, outs;

    ThreadPrinter(Semaphore ins, Semaphore outs) {
        this.ins = ins;
        this.outs = outs;
    }

    @Override
    public void run() {
    	while(true){
            try {
				ins.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // wait for permission to run
            System.out.println("Hi " + Thread.currentThread().getName());
            outs.release();  // allow another thread to run
        }
    }	
    }
