package menu;

import analysis.DecryptGuess;
import analysis.Decrypter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import main.Controller;
import utils.Config;
import utils.IO;

/**
 *
 * @author luki
 */
public class MenuController {
    private final Config conf_;
    private Menu menu_;
    private List<Menu> prevMenus_;
    
    public MenuController(Config conf) {
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
        MenuOption decryptText = new MenuOption(decryptMenu, "Decrypt the entered text") {
            @Override
            public void action() {
                IO.print("Enter text to decrypt: ");
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
                IO.todo("Encrypt text!");
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
}
