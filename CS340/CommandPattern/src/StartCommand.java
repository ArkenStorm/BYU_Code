public class StartCommand extends Command {
    private IDocument _document;
    private String previousDocument;

    public StartCommand(IDocument _document) {
        this._document = _document;
        this.execute();
    }

    @Override
    void execute() {
        previousDocument = _document.sequence().toString();
        _document.clear();
    }

    @Override
    void undo() {
        _document.clear();
        _document.insert(0, previousDocument);
    }
}
