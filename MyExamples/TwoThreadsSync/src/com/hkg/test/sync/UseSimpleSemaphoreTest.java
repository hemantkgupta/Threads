package com.hkg.test.sync;

import java.util.concurrent.Semaphore;

public class UseSimpleSemaphoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(1);

		SendingThread sender = new SendingThread(semaphore);

		ReceivingThread receiver = new ReceivingThread(semaphore);
		Thread t1 = new Thread(sender);
		Thread t2 = new Thread(receiver);

		t1.start();
		t2.start();
	}

}

class SendingThread implements Runnable{
	  Semaphore semaphore = null;

	  public SendingThread(Semaphore semaphore){
	    this.semaphore = semaphore;
	  }
	  @Override
	  public void run(){
	    while(true){
	      try {
			this.semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	      System.out.println("Send");
	    }
	  }
}

class ReceivingThread implements Runnable{
	  Semaphore semaphore = null;

	  public ReceivingThread(Semaphore semaphore){
	    this.semaphore = semaphore;
	  }
	  @Override
	  public void run(){
	    while(true){
	      System.out.println("Recieve");
	      this.semaphore.release();
	    }
	  }
	}