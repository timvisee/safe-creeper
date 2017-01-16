package com.timvisee.safecreeper.command.description;

import java.util.ArrayList;
import java.util.List;

public class CommandDescription {

    List<String> cmds = new ArrayList<String>();
    String desc = "";
    CommandDescription parent = null;
    List<CommandDescription> childs = new ArrayList<CommandDescription>();
    List<ArgumentDescription> args = new ArrayList<ArgumentDescription>();

    /**
     * Constructor
     *
     * @param cmd    Command
     * @param desc   Command description
     * @param parent Parent command
     */
    public CommandDescription(String cmd, String desc, CommandDescription parent) {
        this(cmd, desc, parent, null);
    }

    /**
     * Constructor
     *
     * @param cmds   List of commands
     * @param desc   Command description
     * @param parent Parent command
     */
    public CommandDescription(List<String> cmds, String desc, CommandDescription parent) {
        this(cmds, desc, parent, null);
    }

    /**
     * Constructor
     *
     * @param cmd    Command
     * @param desc   Command description
     * @param parent Parent command
     * @param args   Command arguments
     */
    public CommandDescription(String cmd, String desc, CommandDescription parent, List<ArgumentDescription> args) {
        // Create a command list
        List<String> cmds = new ArrayList<String>();

        // Add the command to the list
        cmds.add(cmd);
    }

    /**
     * Constructor
     *
     * @param cmds   List of commands
     * @param desc   Command description
     * @param parent Parent command
     * @param args   Command arguments
     */
    public CommandDescription(List<String> cmds, String desc, CommandDescription parent, List<ArgumentDescription> args) {
        setCommands(cmds);
        setDescription(desc);
        setParent(parent);
        setArguments(args);
    }

    /**
     * Check whether two commands equal to each other
     *
     * @param cmd1 Command 1
     * @param cmd2 Command 2
     * @return True if the commands are equal to each other
     */
    private static boolean equalsCommand(String cmd1, String cmd2) {
        // Trim the commands from unwanted whitespaces
        cmd1 = cmd1.trim();
        cmd2 = cmd2.trim();

        // Check whether the two commands are empty strings
        if(cmd1.length() == 0 && cmd2.length() == 0)
            return true;

        // Check whether the the two commands are equal (case insensitive)
        return (cmd1.equalsIgnoreCase(cmd2));
    }

    /**
     * Get the first relative command
     *
     * @return First relative command
     */
    public String getCommand() {
        // Ensure there's any item in the command list
        if(cmds.size() == 0)
            return "";

        // Return the first command on the list
        return cmds.get(0);
    }

    /**
     * Set the command (will append command to previous)
     *
     * @param cmd Command to set
     */
    public void setCommand(String cmd) {
        setCommand(cmd, false);
    }

    /**
     * Get all relative commands
     *
     * @return All relative commands
     */
    public List<String> getCommands() {
        return this.cmds;
    }

    /**
     * Set the list of commands
     *
     * @param cmds New list of commands. Null to reset the list of commands
     */
    public void setCommands(List<String> cmds) {
        if(cmds == null)
            this.cmds.clear();

        else
            this.cmds = cmds;
    }

    /**
     * Set the command
     *
     * @param cmd       Command to set
     * @param overwrite True to replace all old commands, false to append this command
     */
    public void setCommand(String cmd, boolean overwrite) {
        // Check whether this new command should overwrite the previous ones
        if(!overwrite) {
            addCommand(cmd);
            return;
        }

        // Replace all commands with this new one
        this.cmds.clear();
        this.cmds.add(cmd);
    }

    /**
     * Add a command to the list
     *
     * @param cmd Command to add
     */
    public void addCommand(String cmd) {
        // TODO: Verify command

        // Ensure this command isn't a duplicate
        if(hasCommand(cmd))
            return;

        // Add the command to the list
        this.cmds.add(cmd);
    }

    /**
     * Add a list of commands
     *
     * @param cmds List of commands to add
     */
    public void addCommands(List<String> cmds) {
        for(String cmd : cmds)
            addCommand(cmd);
    }

    /**
     * Check whether this command description has a specific command
     *
     * @param cmd Command to check for
     * @return True if this command equals to the param command
     */
    public boolean hasCommand(String cmd) {
        // Check whether any command matches with the argument
        for(String entry : this.cmds)
            if(equalsCommand(entry, cmd))
                return true;

        // No match found, return false
        return false;
    }

    /**
     * Check whether this command description has a list of commands
     *
     * @param cmds List of commands
     * @return True if all commands match, false otherwise
     */
    public boolean hasCommands(List<String> cmds) {
        // Check if there's a match for every command
        for(String cmd : cmds)
            if(!hasCommand(cmd))
                return false;

        // There seems to be a match for every command, return true
        return true;
    }

    /**
     * Get the absolute command
     *
     * @return Absolute command
     */
    public String getAbsoluteCommand() {
        // Create a string builder to shape the command in
        StringBuilder sb = new StringBuilder();

        // Check whether this command has a parent, if so, add the absolute parent command
        if(getParent() != null)
            sb.append(getParent().getAbsoluteCommand());

        // Add the command
        sb.append(" ").append(getCommand());

        // Return the build command
        return sb.toString();
    }

    /**
     * Get the parent command if this command description has a parent.
     *
     * @return Parent command, or null
     */
    public CommandDescription getParent() {
        return this.parent;
    }

    /**
     * Set the parent command
     *
     * @param parent Parent command
     */
    public void setParent(CommandDescription parent) {
        this.parent = parent;
    }

    /**
     * Get all command childs
     *
     * @return Command childs
     */
    public List<CommandDescription> getChilds() {
        return this.childs;
    }

    /**
     * Set the childs of this command
     *
     * @param childs New command childs. Null to remove all childs
     */
    public void setChilds(List<CommandDescription> childs) {
        if(childs == null)
            this.childs.clear();

        else
            this.childs = childs;
    }

    /**
     * Check whether this command has any child commands
     *
     * @return True if this command has any child commands
     */
    public boolean hasChilds() {
        return (this.childs.size() != 0);
    }

    /**
     * Get all command arguments
     *
     * @return Command arguments
     */
    public List<ArgumentDescription> getArguments() {
        return this.args;
    }

    /**
     * Set the arguments of this command
     *
     * @param args New command arguments. Null to clear the list of arguments
     */
    public void setArguments(List<ArgumentDescription> args) {
        // Convert null into an empty argument list
        if(args == null)
            this.args.clear();

        else
            this.args = args;
    }

    /**
     * Check whether this command has any arguments
     *
     * @return True if this command has any arguments
     */
    public boolean hasArguments() {
        return (this.args.size() != 0);
    }

    /**
     * Get the command description
     *
     * @return Command description
     */
    public String getDescription() {
        return this.desc;
    }

    /**
     * Set the command description
     *
     * @param desc New command description. Null to reset the description
     */
    public void setDescription(String desc) {
        if(desc == null)
            this.desc = "";

        else
            this.desc = desc;
    }

    /**
     * Check whether this command has any description
     *
     * @return
     */
    public boolean hasDescription() {
        return (this.desc.trim().length() != 0);
    }
}
