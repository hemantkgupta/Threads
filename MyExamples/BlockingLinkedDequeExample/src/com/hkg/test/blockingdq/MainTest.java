package com.hkg.test.blockingdq;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class MainTest {
	public static void main(String[] args) throws Exception {

		// Create a ConcurrentLinkedDeque to work with it in the example
		LinkedBlockingDeque<String> list=new LinkedBlockingDeque<>(3);
		
		Client client=new Client(list);
		Thread thread=new Thread(client);
		thread.start();
		
		for (int i=0; i<5 ; i++) {
			for (int j=0; j<3; j++) {
				String request=list.take();
				System.out.printf("Main: Request: %s at %s. Size: %d\n",request,new Date(),list.size());
			}
			TimeUnit.MILLISECONDS.sleep(300);
		}
		
		System.out.printf("Main: End of the program.\n");
	
	}

}
