package com.hkg.test.sync;

import java.util.concurrent.Semaphore;

public class UseOwnSemaphoreTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BinarySemaphore a = new BinarySemaphore(1);  // first thread is allowed to run immediately
	//	BinarySemaphore b = new BinarySemaphore(0); // second thread has to wait
		MyThreadPrint tp1 = new MyThreadPrint(a);
		MyThreadPrint tp2 = new MyThreadPrint(a); 
		Thread t1 = new Thread(tp1);
		Thread t2 = new Thread(tp2);
		t1.start();
		t2.start();
		

	}

}

class MyThreadPrint implements Runnable {
	BinarySemaphore ins;

    MyThreadPrint(BinarySemaphore ins) {
        this.ins = ins;
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
            ins.notifyToWakeup();  // allow another thread to run
        }
    }	
    }

