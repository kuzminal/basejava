public class DeadLock {
    public static final Object Lock1 = new Object();
    public static final Object Lock2 = new Object();


    public static void main(String[] args) {
        DeadThreadOne threadOne = new DeadThreadOne();
        DeadThreadTwo threadTwo = new DeadThreadTwo();

        threadOne.start();
        threadTwo.start();
    }

    private static class DeadThreadOne extends Thread {
        public void run() {
            synchronized (Lock1) {
                System.out.println("DeadThreadOne захватил объект Lock1");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("DeadThreadOne ждет разблокировки Lock2");
                synchronized (Lock2) {
                    System.out.println("DeadThreadOne захватил объекты Lock1 и Lock2");
                }
            }
        }
    }

    private static class DeadThreadTwo extends Thread {
        public void run() {
            synchronized (Lock2) {
                System.out.println("DeadThreadTwo захватил объект Lock2");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("DeadThreadOne ждет освобождения Lock1");
                synchronized (Lock1) {
                    System.out.println("DeadThreadOne захватил объекты Lock1 и Lock2");
                }
            }
        }
    }
}
