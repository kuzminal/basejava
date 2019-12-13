public class DeadLock {
    public static final Object Lock1 = new Object();
    public static final Object Lock2 = new Object();


    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            lock(Lock1, Lock2);
        });
        Thread threadTwo = new Thread(() -> {
            lock(Lock2, Lock1);
        });

        threadOne.start();
        threadTwo.start();
    }

    private static void lock(Object firstObjectForLock, Object secondObjectForLock){
        synchronized (firstObjectForLock) {
            System.out.println(Thread.currentThread().getName() + " захватил объект Lock2");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ждет освобождения Lock1");
            synchronized (secondObjectForLock) {
                System.out.println(Thread.currentThread().getName() + " захватил объекты Lock1 и Lock2");
            }
        }
    }
}
