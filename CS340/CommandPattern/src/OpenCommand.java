import java.util.Scanner;

public class OpenCommand extends Command {
    private IDocument _document;
    private String openFileName;
    private String deletedString;

    public OpenCommand(IDocument _document) {
        this._document = _document;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Name of file to open: ");
        openFileName = scanner.next();
        this.execute();
    }

    @Override
    void execute() {
        deletedString = _document.sequence().toString();
        _document.open(openFileName);
    }

    @Override
    void undo() {
        _document.clear();
        _document.insert(0, deletedString);
    }
}
