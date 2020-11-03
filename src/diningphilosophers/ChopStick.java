package diningphilosophers;

public class ChopStick {

    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    synchronized public boolean tryTake(int delay) throws InterruptedException {
        if (!iAmFree) {
            wait(delay);
            if (!iAmFree) // Toujours pas libre, on abandonne
            {
                return false; // Echec
            }
        }
        iAmFree = false;
        // Pas utile de faire notifyAll ici, personne n'attend qu'elle soit occupée
        return true; // Succès
    }

    synchronized public void release() {
        iAmFree = true;
        notifyAll();
        System.out.println("Stick " + myNumber + " Released");
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
