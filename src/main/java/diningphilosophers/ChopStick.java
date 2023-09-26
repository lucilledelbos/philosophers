package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
    private final Lock lock = new ReentrantLock();
    private final Condition condition1 = lock.newCondition();
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean tryTake(int delay) throws InterruptedException {
        lock.lock();
        try{
            while (!iAmFree) {
                condition1.await();
            }
            iAmFree = false;
            // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
            return true; // Succès
        }finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            iAmFree = true;
            condition1.signalAll();
            System.out.println("Stick " + myNumber + " Released");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}