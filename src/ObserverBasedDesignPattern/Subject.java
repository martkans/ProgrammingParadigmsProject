package ObserverBasedDesignPattern;

public interface Subject {
    void addListener(Listener listener);
    void deleteListener(Listener listener);
    void notifyListeners(String message);
}
