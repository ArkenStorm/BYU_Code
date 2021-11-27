import java.util.Scanner;

public class ReplaceCommand extends Command {
    private IDocument _document;
    private int replaceIndex;
    private int replaceDistance;
    private String replacementString;
    private String previousString;

    public ReplaceCommand(IDocument _document) {
        this._document = _document;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String replaceIndexInput = scanner.next();
        replaceIndex = validateNumberInput(replaceIndexInput);
        if (replaceIndex != -1) {
            System.out.print("Number of characters to replace: ");
            String replaceDistanceInput = scanner.next();
            replaceDistance = validateNumberInput(replaceDistanceInput);
            if (replaceDistance != -1) {
                System.out.print("Replacement string: ");
                replacementString = scanner.next();
                this.execute();
            }
        }
    }

    @Override
    void execute() {
        previousString = _document.delete(replaceIndex, replaceDistance);
        _document.insert(replaceIndex, replacementString);
    }

    @Override
    void undo() {
        _document.delete(replaceIndex, replacementString.length());
        _document.insert(replaceIndex, previousString);
    }
}