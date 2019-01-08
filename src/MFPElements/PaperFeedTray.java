package MFPElements;

import ObserverBasedDesignPattern.Listener;
import ObserverBasedDesignPattern.Subject;

public class PaperFeedTray implements Subject {
    private int maxCapacity;
    private int paperAmount;
    private Listener listener;

    public PaperFeedTray(int maxCapacity, int paperAmount) {
        this.maxCapacity = maxCapacity;
        this.paperAmount = paperAmount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getPaperAmount() {
        return paperAmount;
    }

    public void addPaper(int amount){
        if (paperAmount + amount <= maxCapacity) {
            paperAmount += amount;
            notifyListeners("Dodano " + amount + " kartek.");
        } else {
            notifyListeners("Nie udało się dodać kartek! Za duża ilość!");
        }
    }

    public boolean getSheetOfPaper(){
        if(paperAmount > 0){
            paperAmount--;
            return true;
        }
        notifyListeners("Brak papieru!");
        return false;
    }

    @Override
    public void addListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void deleteListener(Listener listener) {
        this.listener = null;
    }

    @Override
    public void notifyListeners(String message) {
        listener.notifyUser(message);
    }
}
