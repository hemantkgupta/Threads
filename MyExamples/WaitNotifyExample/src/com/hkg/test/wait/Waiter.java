package com.hkg.test.wait;

public class Waiter implements Runnable{
	private Message msg;
    
    public Waiter(Message m){
        this.msg=m;
    }
 
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        synchronized (msg) {
            try{
                System.out.println(name+" waiting to get notified at time:"+System.currentTimeMillis());
                msg.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(name+" waiter thread got notified at time:"+System.currentTimeMillis());
            System.out.println(name+" processed: "+msg.getMsg());
        }
    }
}
