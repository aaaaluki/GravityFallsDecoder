package menu;

import analysis.DecryptGuess;
import analysis.Decrypter;
import ciphers.Cipher;
import ciphers.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import main.Controller;
import utils.Colour;
import utils.Config;
import utils.IO;
import utils.TextHelper;

/**
 *
 * @author luki
 */
public class MenuController {
    private final Controller controller_;
    private final Config conf_;
    private Menu menu_;
    private List<Menu> prevMenus_;
    
    public MenuController(Config conf, Controller controller) {
        controller_ = controller;
        conf_ = conf;
        menu_ = createMenusSystem();
        prevMenus_ = new LinkedList<>();
    }
    
    /**
     * Main loop for the menus, this method shows the available options, then
     * asks the user for an option and executes that option.
     * 
     * In case the option is not a valid one warns the user.
     * 
     * @return {@code true} if the loop should continue, {@code false} otherwise
     */
    public boolean loop() {
        menu_.showOptions();
        MenuOption option = menu_.askOption();

        if (option == null) {
            // QUIT_COMMAND
            if (prevMenus_.isEmpty()) {return false;}
            
            menu_ = prevMenus_.remove(prevMenus_.size() - 1);
        } else {
            // Some option given, save manu and execute option
            Menu actualMenu = menu_;
            Menu newMenu = option.run();
            
            if (!actualMenu.matches(newMenu)) {
                prevMenus_.add(actualMenu);
                menu_ = newMenu;
            }
        }
        
        return true;
    }
    
    /**
     * Creates all the menu system and returns the main/initial menu
     * 
     * @return main/initial menu
     */
    private Menu createMenusSystem() {
        // Main menu creation
        Menu mainMenu = new Menu("Main").setInfo("Welcome to the Gravity Falls Decoder!");
        
        Menu decryptMenu = new Menu("Decrypt");
        MenuOption decryptOp = new MenuOption(decryptMenu, "Decrypt");
        mainMenu.addOption(decryptOp);
                
        Menu encryptMenu = new Menu("Encrypt");
        MenuOption encryptOp = new MenuOption(encryptMenu, "Encrypt");
        mainMenu.addOption(encryptOp);
        
        Menu configMenu = new Menu("Config");
        MenuOption configOp = new MenuOption(configMenu, "Config");
        mainMenu.addOption(configOp);
                
        // Decrypt Menu ********************************************************
        decryptMenu.setInfo("Decryption menu, decrypt text or files here!");
        MenuOption decryptText = new MenuOption(decryptMenu, "Decrypt the entered text (Automatic)") {
            @Override
            public void action() {
                IO.print("Enter text to decrypt: ", Menu.MAIN_COLOUR);
                String toDecrypt = IO.readLine();
                
                List<DecryptGuess> guesses = Decrypter.decrypt(toDecrypt);
                int toShow = Math.min(conf_.get("decrypt.guesses"), guesses.size());
                
                String userIn;
                boolean exit = false;
                int loop = 0;
                do {
                    for (int i = 0; i < toShow; i++) {
                        if (loop*toShow + i == guesses.size()) {
                            // If there are no more guesses to show return
                            IO.print("No more guesses remaining!\n");
                            return;
                        }
                        
                        IO.print(guesses.get(loop*toShow + i).toString() + "\n");
                    }
                    IO.print("Show more? [Y/n]: ");
                    userIn = IO.readLine();
                    
                    if (userIn.equals("") || userIn.toUpperCase().equals("Y")) {
                        IO.clearLines(1);
                        loop++;
                    } else {
                        exit = true;
                    }
                } while (!exit);
            }
        };
        decryptMenu.addOption(decryptText);
        
        // Encrypt Menu ********************************************************
        encryptMenu.setInfo("Encryption menu, encrypt text or files here!");
        MenuOption encryptText = new MenuOption(encryptMenu, "Encrypt the entered text") {
            @Override
            public void action() {
                IO.print("Enter text to cipher: ", Menu.MAIN_COLOUR);
                String userIn = IO.readLine();

                boolean exit = false;
                Cipher cipher;
                Key key;
                while (!exit) {
                    cipher = askCipher();
                    key = askKey(cipher);
                    
                    String ciphedText = cipher.encrypt(userIn, key);
                    
                    IO.print("Ciphed text:\n", Menu.MAIN_COLOUR);
                    IO.print(String.format("%s\n", ciphedText));

                    // Ask the user if it wants to continue ciphing the text
                    IO.print("Continue ciphing? [y/N]: ");
                    userIn = IO.readLine();
                    
                    if (userIn.toUpperCase().equals("Y")) {
                        userIn = ciphedText;
                        IO.clearLines(1);
                    } else {
                        exit = true;
                    }

                }
            }
        };
        encryptMenu.addOption(encryptText);

        // Config Menu *********************************************************
        configMenu.setInfo("Configuration menu, change or view the actual config!");
        MenuOption configDisplayAll = new MenuOption(configMenu, "Display all configuration values") {
            @Override
            public void action() {
                List<String> keysList = new ArrayList<>(conf_.getKeys());
                Collections.sort(keysList);
                
                for (String key : keysList) {
                    IO.print(String.format("%s -> %s\n", key, conf_.get(key)));
                }
            }
        };
        configMenu.addOption(configDisplayAll);
        
        MenuOption configDisplayOne = new MenuOption(configMenu, "Display one configuration value") {
            @Override
            public void action() {
                IO.print("Key: ");
                String key = IO.readLine();
                
                if(!conf_.getKeys().contains(key)) {
                    IO.warn(String.format("The key \"%s\" does not exist!", key));
                } else {
                    IO.print(String.format("%s -> %s\n", key, conf_.get(key)));
                }
            }
        };
        configMenu.addOption(configDisplayOne);
        
        MenuOption configChange = new MenuOption(configMenu, "Change configuration") {
            @Override
            public void action() {IO.todo("Change config");}
        };
        configMenu.addOption(configChange);

        return mainMenu;
    }
    
    /**
     * Asks the user to choose one of the available ciphers and returns it
     * 
     * @return cipher chosen by user
     */
    private Cipher askCipher() {
        List<Cipher> ciphers = new ArrayList<>();
        int i = 1;
        for (Cipher ciph : controller_.getCiphers()) {
            IO.print(String.format("%s%2d)%s %s\n", Menu.MAIN_COLOUR, i, Colour.RESET, ciph.getName()));
            ciphers.add(ciph);
            
            i++;
        }
        
        boolean validInput = false;
        int idx = -1;
        while (!validInput) {
            IO.print("Select cipher: ", Menu.MAIN_COLOUR);
            String userIn = IO.readLine();
            
            if (TextHelper.checkInteger(userIn)) {
                idx = Integer.valueOf(userIn);

                if (idx > 0 && idx < i) {
                    validInput = true;
                }
            }
        }
        
        return ciphers.get(idx - 1);
    }

    /**
     * Asks the user for a ciphering key, if needed
     * 
     * @param cipher cipher where the key will be used
     * @return 
     */
    private Key askKey(Cipher cipher) {
        if (cipher.getKeyClass() == null) {
            return new Key();
        }
        
        Key key = null;
        String userIn;
        boolean validKey = false;

        while (!validKey) {
            IO.print("Enter key: ", Menu.MAIN_COLOUR);
            userIn = IO.readLine();
            
            if (cipher.getKeyClass() == Integer.class) {
                if (TextHelper.checkInteger(userIn)) {
                    key = new Key(Integer.valueOf(userIn));
                } else {
                    IO.warn("The key must be an Integer!");
                    continue;
                }
                
                String errKey = cipher.validateKey(key);
                if (errKey == null) {
                    validKey = true;
                } else {
                    IO.warn(errKey);
                }
            }
        }
        return key;
    }

}
