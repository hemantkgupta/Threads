package com.hkg.test.lockcondition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest {

	private static final String fileName = "LockExample.txt";
	private static final String EXIT_FLAG = "BYE";
	private static final int NO_OF_LINES = 10;
	private static final Lock fileLock = new ReentrantLock();
	private static final Condition condition = fileLock.newCondition();
	private static final ExecutorService executorPool = Executors
			.newFixedThreadPool(2);

	public static void main(String... args) {
		Runnable fileWriter = new FileWrite();
		Runnable fileReader = new FileRead();
		executorPool.submit(fileReader);
		executorPool.submit(fileWriter);
		executorPool.shutdown();
	}

	/**
	 * This thread will write on a file and inform the reader thread to read it.
	 * If it has not written the EXIT flag then it will go into wait stage and
	 * will wait for READER to signal that it safe to write now.
	 */
	static class FileWrite implements Runnable {

		public void run() {
			try {
				fileLock.lock();
				for (int i = 0; i < NO_OF_LINES; i++) {
					
					PrintWriter writer = new PrintWriter(new File(fileName));
					if (i != NO_OF_LINES - 1) {
						int random = new SecureRandom().nextInt();
						System.out.println("WRITER WRITING " + random);
						writer.println(random);
						writer.close();
						// signallng to READER that its safe to read now.
						condition.signal();
						System.out.println("Writer waiting");
						condition.await();
					} else {
						writer.println(EXIT_FLAG);
						System.out.println("WRITER EXITING ");
						writer.close();
						// AS it was an exit flag so no need to wait, just
						// signal the reader.
						condition.signal();
					}
					
				}
			} catch (Exception e) {
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!");
				e.printStackTrace();
			} finally {
				fileLock.unlock();
				// Delete the file, require in case if one wants to run demo
				// again.
				File file = new File(fileName);
				file.delete();
				try {
					file.createNewFile();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * This thread will read from the file and inform the writer thread to write
	 * again. If it has not read the EXIT flag then it will go into wait stage
	 * and will wait for WRITER to signal that it safe to read now.
	 */
	public static class FileRead implements Runnable {

		public void run() {
			String data = null;
			fileLock.lock();
			try {
				while (true) {
					BufferedReader reader = new BufferedReader(new FileReader(
							fileName));
					data = reader.readLine();
					System.out.println("READ DATA - " + data);
					reader.close();
					if (data == null || !data.equals(EXIT_FLAG)) {
						condition.signalAll();
						System.out.println("Reader Waiting");
						condition.await();
					} else {
						System.out.println("READER EXITING");
						condition.signal();
						break;
					}
				}
			} catch (Exception e) {
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!");
				e.printStackTrace();
			} finally {
				fileLock.unlock();
			}
		}
	}
}
