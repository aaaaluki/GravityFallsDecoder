package menu;

/**
 * A Menu contains a list of this, each option has a text (message to show when
 * listing options), and an action which is executed when the option is chosen.
 *
 * @author luki
 */
public class MenuOption {
    private final Menu menu_;
    private final String text_;
    
    /**
     * MenuOption constructor
     * 
     * @param menu menu were the option directs to
     * @param text text of the option
     */
    public MenuOption(Menu menu, String text) {
        menu_ = menu;
        text_ = text;
    }
    
    /**
     * Method to run when the option is chosen.
     * 
     * This method returns the next menu to be executed
     * 
     * @return next menu
     */
    public final Menu run() {
        action();
        return menu_;
    }
    
    /**
     * Action to run when the option is chosen
     */
    public void action() {
        // Do nothing by default
    }
    
    @Override
    public String toString() {        
        return text_;
    }
}
