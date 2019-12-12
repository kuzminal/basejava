public class Concurrency {

    public static void main(String[] args) throws InterruptedException {
        deadlock();
    }

    public synchronized static void deadlock() {
        try {
            Thread t = new Thread(Concurrency::deadlock);
            t.start();
            t.join();
        } catch (Exception ex) {
        }
    }
}
