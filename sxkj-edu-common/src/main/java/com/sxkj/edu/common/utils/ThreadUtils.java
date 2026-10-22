package com.sxkj.edu.common.utils;

public class ThreadUtils {

  private static ThreadLocal<String> threadHolder = new ThreadLocal<String>() {
    @Override
    protected String initialValue() {
      String no = UUIDUtils.generate(UUIDUtils.ALGORITHM_UUID);
//      System.out.println(no);
      return no;
    }
  };

  public static String getThreadNO() {
    String threadNO = threadHolder.get();
//    System.out.println("Thread NO:" + threadNO);
    return threadNO;
  }

  public static void destroy() {
    threadHolder.remove();
  }

  public static void main(String[] args) throws InterruptedException {

    System.out.println(Thread.currentThread().getName() + " , Current thread NO 1:" + ThreadUtils.getThreadNO());

/*    Thread thread1 = new Thread() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " , Current thread NO 2:" + ThreadUtils.getThreadNO());
      };
    };
    thread1.start();
    thread1.join();*/

    for (int i = 0; i < 100; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println(Thread.currentThread().getName() + " , Current thread NO 3:" + ThreadUtils.getThreadNO());
        }
      }).start();
    }

    System.out.println(Thread.currentThread().getName() + " , Current thread NO 4:" + ThreadUtils.getThreadNO());
  }
}
