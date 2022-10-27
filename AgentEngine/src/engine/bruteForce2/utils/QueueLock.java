package engine.bruteForce2.utils;

public class QueueLock {
    private Object mLock = new Object();
    private volatile boolean mLocked = false;
    private boolean isLocked=false;

    public void lock() {
        mLocked = true;
        isLocked=true;
    }

    public void unlock() {
        synchronized(this.mLock) {
            mLocked = false;
            mLock.notifyAll();
        }

    }
    public void checkIfLocked()
    {
        if(mLocked) {
            synchronized (mLock) {
                while (mLocked) {
                    try {
                        /*      System.out.println(Thread.currentThread().getName()+" in lock");*/
                        mLock.wait();
                        /*            System.out.println(Thread.currentThread().getName()+" passed lock");*/
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


    }


}
