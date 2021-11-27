import java.util.Scanner;

public class DeleteCommand extends Command {
    private IDocument _document;
    private String deletionSequence;
    private int deletionIndex;
    private int deletionDistance;

    public DeleteCommand(IDocument _document) {
        this._document = _document;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Start index: ");
        String deletionIndexInput = scanner.next();
        deletionIndex = validateNumberInput(deletionIndexInput);
        if (deletionIndex != -1) {
            System.out.print("Number of characters to delete: ");
            String deletionDistanceInput = scanner.next();
            deletionDistance = validateNumberInput(deletionDistanceInput);
            if (deletionDistance != -1) {
                this.execute();
                if (deletionSequence == null) {
                    System.out.println("Deletion unsuccessful");
                }
            }
        }
    }

    @Override
    void execute() {
        deletionSequence = _document.delete(deletionIndex, deletionDistance);
    }

    @Override
    void undo() {
        _document.insert(deletionIndex, deletionSequence);
    }
}
