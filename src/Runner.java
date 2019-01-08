import MFP.*;
import MFPElements.PaintContainers.*;
import MFPElements.PaperFeedTray;
import WiFi.*;

import java.util.Scanner;

public class Runner {
    private static Scanner scanner = new Scanner(System.in);
    private static WiFiHandler wiFiHandler;
    private static PaperFeedTray paperFeedTray;
    private static MFP mfp;

    public static void buildMFP() {
        wiFiHandler = new WiFiHandler();
        wiFiHandler.createNetwork(new WiFiNetwork("ala", "admin"));
        wiFiHandler.createNetwork(new WiFiNetwork("lala", "qwerty"));

        paperFeedTray = new PaperFeedTray(100, 100);

        mfp = new MFP(MFPType.INKJET, paperFeedTray, wiFiHandler, scanner);

    }


    public static void start(){
        String menu = "\n\nMenu drukarki\n" +
                "1. Drukuj\n" +
                "2. Skopiuj\n" +
                "3. Skanuj\n" +
                "4. Podajnik na papier\n" +
                "5. Pojemniki na tusz\n" +
                "6. Sieć WiFi\n" +
                "7. Koniec";



        boolean flag = false;
        do {
            System.out.println(menu);

            String choice = getString();
            switch (choice){

                case "1":
                    printMenu();
                    break;

                case "2":
                    photocopyMenu();
                    break;

                case "3":
                    System.out.println("Co chcesz zeskanować?\n");
                    String somethingToScan = getString();
                    mfp.scan(somethingToScan);
                    break;

                case "4":
                    paperFeedTrayMenu();
                    break;

                case "5":
                    paintContainerMenu();
                    break;

                case "6":
                    WiFiMenu();
                    break;

                case "7":
                    System.out.println(showPikachuMeme());
                    scanner.close();
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);

    }

    private static void printMenu(){

        String menu = "\n\nMenu drukowania - kolor: \n" +
                "1. Czarny\n" +
                "2. Kolor\n" +
                "3. Wróć\n";

        boolean flag = false;
        do {
            System.out.println(menu);
            String choice = getString();
            switch (choice){
                case "1":
                    System.out.println("Co chcesz wydrukować?\n");
                    mfp.printInBlack(getString());
                    break;

                case "2":
                    System.out.println("Co chcesz wydrukować?\n");
                    mfp.printInColor(getString());
                    break;

                case "3":
                    System.out.println("...\n\n");
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);
    }


    private static void photocopyMenu(){
        String menu = "\n\nMenu kopiowania - kolor: \n" +
                "1. Czarny\n" +
                "2. Kolor\n" +
                "3. Wróć\n";

        boolean flag = false;
        do {
            System.out.println(menu);
            String choice = getString();
            switch (choice){
                case "1":
                    System.out.println("Co chcesz skserować?\n");
                    mfp.photocopyInBlack(getString());
                    break;

                case "2":
                    System.out.println("Co chcesz skserować?\n");
                    mfp.photocopyInColor(getString());
                    break;

                case "3":
                    System.out.println("...\n\n");
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);
    }

    private static void paperFeedTrayMenu(){
        String menu = "\n\nMenu drukowania - kolor: \n" +
                "1. Sprawdź stan papieru\n" +
                "2. Dołóż papier\n" +
                "3. Wróć\n";

        boolean flag = false;
        do {
            System.out.println(menu);
            String choice = getString();
            switch (choice){
                case "1":
                    System.out.println("Stan wynosi: " + paperFeedTray.getPaperAmount() + "/" + paperFeedTray.getMaxCapacity());
                    break;

                case "2":
                    System.out.println("Ile chcesz dołożyć arkuszy papieru?");
                    mfp.addPaperToFeedTray(getIntBetween(0, Integer.MAX_VALUE));
                    break;

                case "3":
                    System.out.println("...\n\n");
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);
    }

    private static void paintContainerMenu(){
        String menu = "\n\nMenu drukowania - kolor: \n" +
                "1. Sprawdź stan tuszu\n" +
                "2. Włóż pojemnik z tuszem\n" +
                "3. Wyjmij pojemnik z tuszem\n" +
                "4. Wróć\n";

        boolean flag = false;
        do {
            System.out.println(menu);
            String choice = getString();
            switch (choice){
                case "1":
                    System.out.println(mfp.getPaintContainersCondition());
                    break;

                case "2":
                    addPaintContainerMenu();
                    break;

                case "3":
                    removePaintContainerMenu();
                    break;

                case "4":
                    System.out.println("...\n\n");
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);
    }

    private static void addPaintContainerMenu(){
        System.out.println("Jaki chcesz dołożyć typ pojemnika? (TONER|CARTRIDGE)");
        String containerType = getStringFromTable(new String[]{PaintContainerType.TONER.toString(), PaintContainerType.CARTRIDGE.toString()});

        System.out.println("Podaj kolor (RED|BLACK|GREEN|BLUE): ");
        String colorChoice = getStringFromTable(new String[]{Color.BLACK.toString(), Color.BLUE.toString(), Color.GREEN.toString(), Color.RED.toString()});

        System.out.println("Podaj pojemność: ");
        int capacityChoice = getIntBetween(0, Integer.MAX_VALUE);
        if (containerType.equals("TONER")){
            mfp.addPaintContainer(new Toner(Color.valueOf(colorChoice), capacityChoice));
        } else if(containerType.equals("CARTRIDGE")){
            mfp.addPaintContainer(new Cartridge(Color.valueOf(colorChoice), capacityChoice));
        }
    }

    private static void removePaintContainerMenu(){
        System.out.println("Jaki chcesz wyjąć " + mfp.getPaintContainerType() + "?\n");

        System.out.println("Podaj kolor (RED|BLACK|GREEN|BLUE): ");
        String colorChoice = getStringFromTable(new String[]{Color.BLACK.toString(), Color.BLUE.toString(), Color.GREEN.toString(), Color.RED.toString()});

        mfp.removePaintContainer(Color.valueOf(colorChoice));

    }

    private static void WiFiMenu(){
        String menu = "\n\nMenu WiFi: \n" +
                "1. Pokaż połączoną sieć\n" +
                "2. Pokaż dostępne sieci\n" +
                "3. Połącz\n" +
                "4. Rozłącz\n" +
                "5. Wróć\n";

        boolean flag = false;
        do {
            System.out.println(menu);
            String choice = getString();
            switch (choice){
                case "1":
                    System.out.println("Połączono z: " + mfp.getConnectedWiFiNetwork());
                    break;

                case "2":
                    System.out.println(mfp.getWiFiHandler().toString());
                    break;

                case "3":
                    mfp.connect();
                    break;

                case "4":
                    mfp.disconnect();
                    break;

                case "5":
                    System.out.println("...\n\n");
                    flag = true;
                    break;

                default:
                    System.out.println("Wybierz poprawnie.");

            }
        } while (!flag);
    }


    private static String getString(){
        return scanner.nextLine();
    }

    private static int getIntBetween(int from, int to){
        int value;
        do{
            try{
                String temp = scanner.nextLine();
                value = Integer.parseInt(temp);
            }catch (NumberFormatException ex){
                value = -1;
            }

        }while (value < from || value > to || value == -1);
        return value;
    }

    private static String getStringFromTable(String[] tableOfAcceptedStrings){
        String value;
        boolean valueIsAccepted = false;
        do{
            value = scanner.nextLine();
            for (String elem : tableOfAcceptedStrings) {
                if(elem.equals(value)){
                    valueIsAccepted = true;
                }
            }
        }while (!valueIsAccepted);
        return value;
    }

    private static String showPikachuMeme(){
        return  "         @@@@@@@@@@%.#&@@@&&.  ,@@@@@@,  @@@@@@@@@@%       %@%@@@@%.   *&@&(     ,@@@@@@%   ,@@@@@@%  .#&@@@&&.  %@@@@@.     ,@@,          \n" +
                "            ,@@,    ,@@,      ,@@/   ./.    ,@@,           %@%   ,&@# .&@&@@(   .@@/   ./. .@@/   ./. ,@@,       %@%  *&@&.  ,@@,          \n" +
                "            ,@@,    ,@@&%%%%. .&@@/,        ,@@,           %@%   ,&@# %@& /@@,  .&@@/,     .&@@/,     ,@@&%%%%.  %@%    &@#  ,@@,          \n" +
                "            ,@@,    ,@@/,,,,    ./#&@@@(    ,@@,           %@@@@@@%* /@@,  #@&.   ./#&@@@(   ./#&@@@( ,@@/,,,,   %@%    &@#  .&&.          \n" +
                "            ,@@,    ,@@,      ./,   .&@#    ,@@,           %@%      ,@@@@@@@@@% ./.   .&@# ./.   .&@# ,@@,       %@%  *&@&.   ,,           \n" +
                "            ,@@,    ,@@@@@@@% .#&@@@@&/     ,@@,           %@%     .&@#     ,@@/.#&@@@@&/  .#&@@@@&/  ,@@@@@@@%  %@@@@@.     ,@@,          \n" +
                ",*************,,*/(((((//,,*(#%%%%%%%%%%%%%%%#(*,,,****************************************************,*/(((((((((/((((////****/((##%%%%%%\n" +
                ",*************,,//((((((//,,*(%%%%%%%%%%%%%%%%%##/*****************************************************,,*/(///(//////****//((##%%%%%%%%%%%\n" +
                ",************,,*/(((((((//***/#%%%%%%%%%%%%%%%%%%%#(/***************************************************,*//////////*//((#%%%%%%%%%%%%%%%%%\n" +
                ",***********,,*////////////***/##%%%%%%%%%%%%%%%%%%%##(*,***********************************************,,*////////(###%%%%%%%%%%%%%%%%%%%%\n" +
                ",**********,,,*/*******//////**/(#%%%%%%%%%%%%%%%%%%%%%#(/**********************************************,,,***/(##%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                ",*********,,,,*************///***/(#%%%%%%%%%%%%%%%%%%%%%%#(/***********************************,****,****/((#%%%%%%%%%%%%%%%%%%%%%%%%%%%%#\n" +
                ",*********,,,***************//****/(##%%%%%%%%%%%%%%%%%%%%%%##//**************//////////////////////((#####%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(\n" +
                ",********,,,,***********************/(#%%%%%%%%%%%%%%%%%%%%%%%##################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(/\n" +
                ",*******,..,***********************,,*/##%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%###((//\n" +
                ",*******,.,,***********************,,,,*(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(//**//\n" +
                ",******,.,,,************************,,,,*/(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(//*******\n" +
                ",*****,,,,,********,***,,,,,,,,,,,,*,,,,,,*/(######%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(/**********\n" +
                ",*****,..,*******,,,,,,,,,,,,,,,,,,,,,,*,,,,*///((#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%###(/************\n" +
                ",*****,,,*******,,,,,*,,,,,,,,,,,,,,,,,****,,,*/(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#######(//**************\n" +
                ",****,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,**,,,/(%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#((//******************\n" +
                ",***,..,,,,,,,,,,,,,,,,,,,,,,,,,,,,,..,,,,,,,*(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(/*******************\n" +
                ",**,,.,,,,,,,,,,,,,,,,,,,,,,,,,,.......,,,,,,/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#####%%%%%%%%%%%%%%%%#(/******************\n" +
                ",**,..,,,,,,,,,,,,,,,,,,,,,,,,,......,,,*,,,*(#%%%%%%%%##(((/(##%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(((/*/((#%%%%%%%%%%%%%%#(/*****************\n" +
                ",*,..,,,,,,,,,,,,,,,,,,,,,,,,,,,.....,,**,,*/#%%%%%%%##((((*,**/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%##((##/,,,*(#%%%%%%%%%%%%%%#(*****************\n" +
                ".*,.,,,**,,,,,,,,,,,,,,,,,,,,,,,,,,*****,,,/(%%%%%%%%#(//(#/,..*/#%%%%%%%%%%%%%%%%%%%%%%%%%%%#(//(#/,..,/(#%%%%%%%%%%%%%%#/*****///////////\n" +
                ".,..,,,,,,,,,,,,,,,,,,,,,,,,,,*,,*******,,,(#%%%%%%%%#(*,,,....,/#%%%%%%%%%%%%%%%%%%%%%%%%%%%#(*,,,....,/(#%%%%%%%%%%%%%%#(*,**////////////\n" +
                ".,..,,,,,,,,,...........,,,,,,*,********,,*(#%%%%%%%%%#(/*,,...,/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(/*,,..,*/##%%%%%%%%%%%%%%%#(***////////////\n" +
                "...,,,,,,,................,,*,**********,,/#%%%%%%%%%%%%#((////((#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##((///(#%%%%%%%%%%%%%%%%%%(/**////////////\n" +
                " ..,,,,,,.................,,,**********,,*(#%%%%%%%%%%%%%%%%%%#%%%%%%%%#((///((#%%%%%%%%%%%%%%%%%%%%%#%%%%%%%%%%%%%%%%%%%%%#/**////////////\n" +
                ".,,,,,,,,.................,,***********,,/(####%%%%%%%%%%%%%%%%%%%%%%%%#(/*,,,*(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(/*////////////\n" +
                ".,***,,,,,,..............,,,**********,..,***//((##%%%%%%%%%%%%%%%%%%%%%%%##((##%%%%%%%%%%%%%%%%%%%%%%%%%##(((((((((###%%%%%#/**///////////\n" +
                ".*****,,,,,,,,,,,,,,,,,,,*************,..,*******/(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##///*//////((#%%%%%#(**///////////\n" +
                ".****************/******/***////*****,.,*///////**/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(////////////(#%%%%%#/**//////////\n" +
                ".***********************/////*******,..,*//////////(#%%%%%%%%%%%%%%%%%%%%##########%%%%%%%%%%%%%%%%%%%%#(///////////*/(#%%%%%#(***/////////\n" +
                ".************************///********,..,*//////////#%%%%%%%%%%%%%%%%%%#(//*****///(((##%%%%%%%%%%%%%%%%#(///////////**/##%%%%##/***////////\n" +
                ".***********************************,.,,***///////(#%%%%%%%%%%%%%%%%#(/*,,,*//((((////(#%%%%%%%%%%%%%%%#((////////////(#%%%%%%#(*********//\n" +
                ",***********,,,*,,*,,**************,,,*//******//(#%%%%%%%%%%%%%%%%%#(*,,*/(((#####(((((#%%%%%%%%%%%%%%%##///////////(#%%%%%%%%#(***///////\n" +
                ",*************,,**,,,************,,,,,/(##((((####%%%%%%%%%%%%%%%%%%%(/**/(((#((((#((//(#%%%%%%%%%%%%%%%%%#(((((((((##%%%%%%%%%%#/**///////\n" +
                ",******************************,,,,,,,*(#%#%%%%%%%%%%%%%%%%%%%%%%%%%%#(**/((#(#(((#((//(#%%%%%%%%%%%%%%%%%%%%%%%#%#%%%%%%%%%%%%%#(**///////\n" +
                ",*************,**************,****,,,,,/(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(/*/((((#((((///(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%(/*///////\n" +
                ",*************************************,*/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(////////////(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#/**/////*\n" +
                ",******////****///////////////////////***/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%####(((((((###%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(********\n" +
                ".,*,****///////////////////////////////***/#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%#(/*******\n" +
                ".,,,,*****//////////////////////////*******(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%##(*******\n" +
                ".,,,,,,***********/////////////////********/(#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%(*******";
    }
}
