package WiFi;

import java.util.ArrayList;

public class WiFiHandler {
    private ArrayList<WiFiNetwork> wiFiNetworks;

    public WiFiHandler() {
        this.wiFiNetworks = new ArrayList<>();
    }

    public void createNetwork(WiFiNetwork network){
        for (WiFiNetwork existingNetwork: wiFiNetworks) {
            if(existingNetwork.getName().equals(network.getName()))
                return;
        }
        wiFiNetworks.add(network);
    }

    public void deleteNetwork(String networkName){
        for (WiFiNetwork network : wiFiNetworks) {
            if (network.getName().equals(networkName)) {
                wiFiNetworks.remove(network);
                return;
            }
        }
    }

    public WiFiNetwork searchByName(String networkName){
        for (WiFiNetwork network : wiFiNetworks) {
            if (network.getName().equals(networkName)) {
                return network;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder wiFiStringList = new StringBuilder("Dostępne sieci WiFi: \n");
        for (WiFiNetwork network:wiFiNetworks) {
            wiFiStringList.append(network.getName() + "\n");
        }

        return String.valueOf(wiFiStringList);
    }
}
