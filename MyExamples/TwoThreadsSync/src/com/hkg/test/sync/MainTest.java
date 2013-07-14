package com.hkg.test.sync;
public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Test test = new Test();
		MyThread m = new MyThread(test);

		Thread t1 = new Thread(m);
		Thread t2 = new Thread(m);
		t1.start();
		t2.start();

	}
}

class MyThread implements Runnable {

	private Test test;

	public MyThread(Test test) {
		super();
		this.test = test;
	}

	@Override
	public void run() {
		printMe();
	}

	public void printMe() {
		while (true) {
			synchronized (test) {
				try {
					System.out.println("Hi " + Thread.currentThread().getName());
					test.notify();
					test.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Test {
}
