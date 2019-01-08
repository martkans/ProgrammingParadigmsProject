package MFPElements;

import ObserverBasedDesignPattern.Listener;
import ObserverBasedDesignPattern.Subject;

public class Port implements Subject {
    private String name;
    private boolean connected;
    private Listener listener;

    public Port(String name) {
        this.name = name;
        this.connected = false;
    }

    public String getName() {
        return name;
    }

    public void plugIn(){
        if(!connected){
            connected = true;
            listener.notifyUser("Podpięto " + name + ".\n");
        } else {
            listener.notifyUser("Port " + name + " jest już podłączony.");
        }
    }

    public void disconnect(){
        if (connected){
            connected = false;
            listener.notifyUser("Odpięto " + name + ".\n");
        } else {
            listener.notifyUser("Port " + name + " jest już wolny.");
        }
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
