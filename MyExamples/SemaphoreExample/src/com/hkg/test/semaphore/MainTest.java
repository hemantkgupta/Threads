package com.hkg.test.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				PrintQueue printQueue=new PrintQueue();
				Thread thread[]=new Thread[10];
				for (int i=0; i<10; i++){
					thread[i]=new Thread(new Job(printQueue),"Thread "+i);
				}
				for (int i=0; i<10; i++){
					thread[i].start();
				}
	}

}

class Job implements Runnable {

	/**
	 * Queue to print the documents
	 */
	private PrintQueue printQueue;
	
	/**
	 * Constructor of the class. Initializes the queue
	 * @param printQueue
	 */
	public Job(PrintQueue printQueue){
		this.printQueue=printQueue;
	}
	
	/**
	 * Core method of the Job. Sends the document to the print queue and waits
	 *  for its finalization
	 */
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job\n",Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());		
	}
}

class PrintQueue {
	
	/**
	 * Semaphore to control the access to the queue
	 */
	private final Semaphore semaphore;
	
	/**
	 * Constructor of the class. Initializes the semaphore
	 */
	public PrintQueue(){
		semaphore=new Semaphore(1);
	}
	
	/**
	 * Method that simulates printing a document
	 * @param document Document to print
	 */
	public void printJob (Object document){
		try {
			// Get the access to the semaphore. If other job is printing, this
			// thread sleep until get the access to the semaphore
			semaphore.acquire();
			
			Long duration=(long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),duration);
			Thread.sleep(duration);			
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Free the semaphore. If there are other threads waiting for this semaphore,
			// the JVM selects one of this threads and give it the access.
			semaphore.release();			
		}
	}

}

