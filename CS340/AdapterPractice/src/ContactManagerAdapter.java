public class ContactManagerAdapter implements TableData {
    private ContactManager contactManager;

    public ContactManagerAdapter(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    public ContactManager getContactManager() {
        return contactManager;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public int getRowCount() {
        return contactManager.getContactCount();
    }

    @Override
    public int getColumnSpacing() {
        return 2;
    }

    @Override
    public int getRowSpacing() {
        return 0;
    }

    @Override
    public char getHeaderUnderline() {
        return '_';
    }

    @Override
    public String getColumnHeader(int col) {
        switch (col) {
            case 0:
                return "First Name";
            case 1:
                return "Last Name";
            case 2:
                return "Phone";
            case 3:
                return "Email";
            default:
                return "None";
        }
    }

    @Override
    public int getColumnWidth(int col) {
        switch (col) {
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 25;
            default:
                return 0;
        }
    }

    @Override
    public Justification getColumnJustification(int col) {
        return Justification.Center;
    }

    @Override
    public String getCellValue(int row, int col) {
        switch (col) {
            case 0:
                return contactManager.getContact(row).getFirstName();
            case 1:
                return contactManager.getContact(row).getLastName();
            case 2:
                return contactManager.getContact(row).getPhone();
            case 3:
                return contactManager.getContact(row).getEmail();
            default:
                return "None";
        }
    }
}
