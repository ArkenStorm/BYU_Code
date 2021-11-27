import java.util.ArrayList;
import java.util.List;

public class UndoRedoManager {
    private List<Command> commands;
    private int commandPointer;
    private static UndoRedoManager instance;

    public static UndoRedoManager getInstance() {
        if (instance == null) {
            instance = new UndoRedoManager();
        }
        return instance;
    }

    public UndoRedoManager() {
        commands = new ArrayList<>();
        commandPointer = -1;
    }

    public int getCommandPointer() {
        return commandPointer;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void execute(Command command) {
        //command.execute();
        commands.add(command);
        commandPointer++;
    }

    private boolean canUndo() {
        return commands.size() > 0 && commandPointer >= 0;
    }

    private boolean canRedo() {
        return commands.size() > 0 && commandPointer < commands.size() - 1;
    }

    public void undo() {
        if (canUndo()) {
            commands.get(commandPointer).undo();
            commandPointer--;
        }
        else {
            System.out.println("Cannot undo.");
        }
    }

    public void redo() {
        if (canRedo()) {
            commandPointer++;
            commands.get(commandPointer).execute();
        }
        else {
            System.out.println("Cannot redo.");
        }
    }
}