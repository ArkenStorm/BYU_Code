import java.util.Scanner;

public class InsertCommand extends Command {
    private IDocument _document;
    private String sequenceInput;
    private int insertionIndex;

    public InsertCommand(IDocument _document) {
        this._document = _document;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String insertionIndexInput = scanner.next();
        insertionIndex = validateNumberInput(insertionIndexInput);
        if (insertionIndex != -1) {
            System.out.print("Sequence to insert: ");
            sequenceInput = scanner.next();
            this.execute();
        }
    }

    @Override
    void execute() {
        _document.insert(insertionIndex, sequenceInput);
    }

    @Override
    void undo() {
        _document.delete(insertionIndex, sequenceInput.length());
    }
}
