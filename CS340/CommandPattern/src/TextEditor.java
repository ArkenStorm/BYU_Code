import java.util.Scanner;

class TextEditor {

    private IDocument _document;
    private UndoRedoManager manager;

    TextEditor(IDocument document) {
        _document = document;
        manager  = UndoRedoManager.getInstance();
    }

    void run() {
        while (true) {
            printOptions();

            Scanner scanner = new Scanner(System.in);
            String optionInput = scanner.next();
            int option = Command.validateNumberInput(optionInput);

            if (option != -1) {
                switch (option) {
                    case 1:
                        manager.execute(new InsertCommand(_document));
                        break;
                    case 2:
                        manager.execute(new DeleteCommand(_document));
                        break;
                    case 3:
                        manager.execute(new ReplaceCommand(_document));
                        break;
                    case 4:
                        _document.display();
                        break;
                    case 5:
                        save();
                        break;
                    case 6:
                        manager.execute(new OpenCommand(_document));
                        break;
                    case 7:
                        manager.execute(new StartCommand(_document));
                        break;
                    case 8:
                        manager.undo();
                        break;
                    case 9:
                        manager.redo();
                        break;
                    case 10:
                        return;
                }
            }

            System.out.println();
        }
    }

    private void printOptions() {
        System.out.println("SELECT AN OPTION (1 - 10):");
        System.out.println("1. Insert a string at a specified index in the document");
        System.out.println("2. Delete a sequence of characters at a specified index");
        System.out.println("3. Replace a sequence of characters at a specified index with a new string");
        System.out.println("4. Display the current contents of the document");
        System.out.println("5. Save the document to a file");
        System.out.println("6. Open a document from a file");
        System.out.println("7. Start a new, empty document");
        System.out.println("8. Undo");
        System.out.println("9. Redo");
        System.out.println("10. Quit");

        System.out.println();
        System.out.print("Your selection: ");
    }

    private void save() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Name of file: ");
        String saveFileName = scanner.next();
        _document.save(saveFileName);
    }
}
