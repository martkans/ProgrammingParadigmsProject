package MFP;

import MFPElements.PaintContainers.*;
import MFPServices.*;
import MFPElements.*;
import ObserverBasedDesignPattern.Listener;
import WiFi.WiFiHandler;
import WiFi.WiFiNetwork;

import java.util.ArrayList;
import java.util.Scanner;


public class MFP implements Printing, Scanning, Photocopying, WiFiCommunication, Listener {

    private MFPType mfpType;
    private PaintContainerType paintContainerType;

    private ArrayList<PaintContainer> paintContainers;

    private PaperFeedTray paperFeedTray;
//    private ArrayList<Port> ports;

    private WiFiHandler wiFiHandler;
    private WiFiNetwork connectedWiFiNetwork;

    private Scanner scanner;

    public MFP(MFPType mfpType, PaperFeedTray paperFeedTray/*, ArrayList<Port> ports*/, WiFiHandler wiFiHandler, Scanner scanner) {
        this.mfpType = mfpType;
        this.paperFeedTray = paperFeedTray;
//        this.ports = ports;
        this.wiFiHandler = wiFiHandler;
        this.connectedWiFiNetwork = null;
        this.scanner = scanner;

        paperFeedTray.addListener(this);

//        for (Port port : ports) {
//            port.addListener(this);
//        }

        if (mfpType.equals(MFPType.LASER)){
            paintContainerType = PaintContainerType.TONER;
        } else {
            paintContainerType = PaintContainerType.CARTRIDGE;
        }

        this.paintContainers = new ArrayList<>();
    }

    public String getConnectedWiFiNetwork() {
        if (connectedWiFiNetwork == null)
            return "Brak połączenia";
        else return connectedWiFiNetwork.getName();
    }

    public WiFiHandler getWiFiHandler() {
        return wiFiHandler;
    }

    public PaintContainerType getPaintContainerType() {
        return paintContainerType;
    }

    public String getPaintContainersCondition(){
        StringBuilder buffer = new StringBuilder("Stan tuszów:\n");
        for (PaintContainer paintContainer: paintContainers) {
            buffer.append(paintContainer.getColor() + "\t" + paintContainer.getPaintLevel() + "\n");
        }

        return buffer.toString();
    }

    public void addPaperToFeedTray(int amountOfPaperSheet){
        paperFeedTray.addPaper(amountOfPaperSheet);
    }

    public void addPaintContainer(PaintContainer paintContainer){

        if (verifyPaintContainerType(paintContainer)){
            for (PaintContainer installedPaintContainer:paintContainers) {
                if (installedPaintContainer.getColor().equals(paintContainer.getColor())){
                    notifyUser("Najpierw wyjmij pusty pojemnik z farbą.");
                    return;
                }
            }

            paintContainers.add(paintContainer);
            notifyUser("Dodano kolor: " + paintContainer.getColor() + ".");
        } else {
            notifyUser("Nieprawidłowy typ pojemnika na farbę.");
        }

    }

    public void removePaintContainer(Color color){
        if(!paintContainers.removeIf(installedPaintContainer -> (installedPaintContainer.getColor().equals(color)))){
          notifyUser("Nie ma pojemnika z podaną farbą.");
        } else {
            notifyUser("Usunięto kolor: " + color.toString() + ".");
        }
    }

//    public void plugInPort(String name){
//        for (Port port : ports) {
//            if (port.getName().equals(name)){
//                port.plugIn();
//                return;
//            }
//        }
//    }
//
//    public void disconnectPort(String name){
//        for (Port port : ports) {
//            if (port.getName().equals(name)){
//                port.disconnect();
//                return;
//            }
//        }
//    }

    @Override
    public void photocopyInBlack(String content) {
        if(checkPaintLevel(Color.BLACK)){
            if(paperFeedTray.getSheetOfPaper()){
                paintContainers.forEach(paintContainer -> {if (paintContainer.getColor().toString().equals("BLACK")) paintContainer.chargePaint();});
                notifyUser("Ksero biało-czarne\n" + content);

            }
        }
    }

    @Override
    public void photocopyInColor(String content) {
        if(checkPaintLevel(Color.BLACK) && checkPaintLevel(Color.BLUE) && checkPaintLevel(Color.GREEN) && checkPaintLevel(Color.RED)){
            if(paperFeedTray.getSheetOfPaper()){
                paintContainers.forEach(paintContainer -> paintContainer.chargePaint());
                notifyUser("Ksero kolorowe\n" + content);
            }
        };
    }

    @Override
    public void printInBlack(String content) {
        if(checkPaintLevel(Color.BLACK)){
            if(paperFeedTray.getSheetOfPaper()){
                paintContainers.forEach(paintContainer -> {if (paintContainer.getColor().toString().equals("BLACK")) paintContainer.chargePaint();});
                notifyUser("Druk biało-czarny\n" + content);
            }
        }
    }

    @Override
    public void printInColor(String content) {
        if(checkPaintLevel(Color.BLACK) && checkPaintLevel(Color.BLUE) && checkPaintLevel(Color.GREEN) && checkPaintLevel(Color.RED)){
            if(paperFeedTray.getSheetOfPaper()){
                paintContainers.forEach(paintContainer -> paintContainer.chargePaint());
                notifyUser("Druk kolorowy\n" + content);
            }
        }
    }

    @Override
    public String scan(String content) {
        notifyUser("Skanowanie...\n" + content);
        return content;
    }


    @Override
    public void connect() {
        notifyUser(wiFiHandler.toString() + "Wpisz nazwę sieci z którą chcesz się połączyć lub wpisz \'back\' aby powrócić.");
        String choice = getString();

        if (!choice.equals("back")) {

            WiFiNetwork tempNodeToWiFiNetwork = wiFiHandler.searchByName(choice);
            if (tempNodeToWiFiNetwork != null) {
                notifyUser("Wpisz hasło do sieci " + choice + ".");
                String password = getString();
                if (tempNodeToWiFiNetwork.validatePassword(password)) {
                    connectedWiFiNetwork = tempNodeToWiFiNetwork;
                    notifyUser("Połączono z siecią " + choice + ".");
                } else {
                    notifyUser("Błędne hasło.");
                }
            } else {
                notifyUser("Nie wykryto sieci o nazwie " + choice + ".");
            }
        }
    }

    @Override
    public void disconnect() {
        if(connectedWiFiNetwork == null){
            notifyUser("Nie jesteś połączony z żadną siecią");
        } else {
            boolean answerAccepted = false;
            do {
                notifyUser("Czy na pewno chcesz się rozłączyć z siecią " + connectedWiFiNetwork.getName() + "? (Tak/Nie)");
                String choice = scanner.nextLine();
                if (choice.equals("Tak")) {
                    notifyUser("Rozłączono się z " + connectedWiFiNetwork.getName());
                    connectedWiFiNetwork = null;
                    answerAccepted = true;
                } else if (choice.equals("Nie")) {
                    notifyUser("Anulowano");
                    answerAccepted = true;
                } else {
                    notifyUser("Nie rozpoznano");
                }
            }while (!answerAccepted);
        }
    }

    @Override
    public void notifyUser(String message) {
        System.out.println("\n" + message + "\n");
    }

    private boolean checkPaintLevel(Color color){
        for (PaintContainer installedPaintContainer: paintContainers) {
            if (installedPaintContainer.getColor().equals(color)){
                if (installedPaintContainer.getPaintLevel() <= 0){
                    notifyUser("Wymień kolor: " + color);
                    return false;
                } else {
                    return true;
                }
            }
        }

        notifyUser("Brak pojemnika z kolorem: " + color);
        return false;
    }

    private boolean verifyPaintContainerType(PaintContainer paintContainer){
        if (paintContainerType.equals(PaintContainerType.CARTRIDGE)){
            return paintContainer.getClass().equals(Cartridge.class);
        } else {
            return paintContainer.getClass().equals(Toner.class);
        }
    }

    private String getString(){
        return scanner.nextLine();
    }

}
