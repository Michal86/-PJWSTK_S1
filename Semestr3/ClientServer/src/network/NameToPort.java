package network;

public enum NameToPort {
    BOB (1, 40000),
    MIKE (2, 40100),
    ALICE (3, 40200),
    JOHN (4, 40300);

    private int folderCode;
    private int portCode;

    NameToPort(int folderCode, int portCode){
        this.folderCode = folderCode;
        this.portCode = portCode;
    }

    public int getFolderCode(){
        return this.folderCode;
    }

    int getPort () {
        switch (this) {
            case BOB:
                return 40000;
            case MIKE:
                return 40100;
            case ALICE:
                return 40200;
            case JOHN:
                return 40300;
            default:
                throw new AssertionError("Unknown port! " + this);
        }
    }
}
