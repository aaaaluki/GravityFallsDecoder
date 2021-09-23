package menu;

import java.util.ArrayList;
import java.util.List;
import utils.Colour;
import utils.IO;
import utils.TextHelper;

/**
 *
 * @author luki
 */
public class Menu {
    private static final String MAIN_COLOUR = Colour.GREEN_BOLD_BRIGHT;
    private static final String[] QUIT_COMMANDS = {"Q", "QUIT", "EXIT", "R", "RETURN"};

    private static int NEXT_ID = 0;
    
    private final String name_;
    private final int id_;
    private String info_;
    private List<MenuOption> options_;
    
    /**
     * Menu constructor
     * 
     * @param name name of the menu
     */
    public Menu(String name) {
        id_ = NEXT_ID;
        NEXT_ID++;
        
        name_ = name;
        options_ = new ArrayList<>();
    }
    
    /**
     * Setter for info_ (information text)
     * 
     * @param info info_
     * @return itself
     */
    public Menu setInfo(String info) {
        info_ = info;
        return this;
    }
    
    /**
     * Add a new option to the menu
     * 
     * @param option new option
     * @return itself
     */
    public Menu addOption(MenuOption option) {
        
        options_.add(option);
        return this;
    }

    /**
     * Getter for options_
     * 
     * @return options_
     */
    public List<MenuOption> getOptions() {
        return options_;
    }

    /**
     * Displays the options to the user
     */
    public void showOptions() {
        IO.print(info_ + "\n", Colour.BLUE_BOLD);
        
        int count = 1;
        for (MenuOption op : options_) {
            // Let's supose there won't be more than 99 options in a menu :)
            IO.print(String.format("\t%2d) ", count), MAIN_COLOUR);
            IO.print(op.toString() + "\n");
            count++;
        }

        IO.print(String.format("\t %s)", QUIT_COMMANDS[0]), MAIN_COLOUR);
        IO.print(" Quit/Return\n");
    }
    
    /**
     * Asks the user for an options and returns it
     * 
     * In case the option chosen is the {@code QUIT_COMMAND} returns {@code null}
     * 
     * @return MenuOption or null
     */
    public MenuOption askOption() {
        String userOption;
        Integer parsedOption;
        
        do {
            IO.print(String.format("%s > ", name_), MAIN_COLOUR);
            userOption = IO.readLine();
            
            if (simpleMatch(QUIT_COMMANDS, userOption.toUpperCase())) {
                return null;
            }
            
            parsedOption = validateOption(userOption);
            
        } while (parsedOption == null);
        
        return options_.get(parsedOption - 1);
    }
    
    /**
     * Returns {@code true} if both menus are the same, false otherwise
     * 
     * @param other menu to compare
     * @return {@code true} if the menus are the same
     */
    public boolean matches(Menu other) {
        return this.id_ == other.id_;
    }
    
    /**
     * Returns {@code true} if the option is valid {@code false} otherwise.
     * 
     * @param option option string to validate
     * @return 
     */
    private Integer validateOption(String option) {
        if (TextHelper.checkInteger(option)) {
            Integer cmd = Integer.parseInt(option);
            
            if (cmd >= 1 && cmd <= options_.size()) {
                return cmd;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if an array contains a value
     * 
     * @param arr Array were to search
     * @param val Value to fins
     * @return If value is found
     */
    private boolean simpleMatch(String[] arr, String val) {
        for (String s : arr) {
            if (val.equals(s)) {
                return true;
            }
        }
        
        return false;
    }
}
