package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
    private final Lock verrou = new ReentrantLock();
    private final Condition condVerrou = verrou.newCondition();
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean tryTake(int delay) throws InterruptedException {
        verrou.lock();
        try {
            if (!iAmFree) {
                condVerrou.awaitNanos(delay * 1000000L); // Convert delay to nanoseconds
                if (!iAmFree) { 
                    return false; // Echec
                }
            }
            iAmFree = false;
            return true; // Succès
        } finally {
            verrou.unlock();
        }
    }

    public void release() {
        verrou.lock();
        try {
            iAmFree = true;
            synchronized (this) {
                notifyAll();
            }
            condVerrou.signalAll();
            System.out.println("Stick " + myNumber + " Released");
        } finally {
            verrou.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + this.myNumber;
    }
}