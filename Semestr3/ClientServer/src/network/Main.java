package network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {

    private static String               getArg;

    //MAIN LAYOUT
    private VBox                        root;
    private final AtomicBoolean         isRunning = new AtomicBoolean(false);
    private TextArea                    logHistory = new TextArea("");
    private TextField                   userTxt = new TextField();
    private TextField                   fileTxt = new TextField();
    private Button                      ccBtn = new Button("Connect");
    private Button                      multiBtn = new Button("Get selected");
    private Server                      thisServer;
    private String                      name;
    private ObservableList<String>      serverBoxData = FXCollections.observableArrayList();
    private ComboBox<String>            serverComboBox;

    //CONNECTIONS TO OTHER SERVERS
    private Map<String, ConnectionHandler>  connectionHandlerMap;
    private volatile int                    multiDownload = 0;

    //=============================================
    public static void main(String[] args) {
        getArg = args[0];
        launch(args);
        Platform.exit();
    }
    //=============================================
    @Override
    public void start(Stage primaryStage) throws Exception {
        connectionHandlerMap =  Collections.synchronizedMap(new HashMap<>());
        this.name = getArg.toUpperCase();
        primaryStage.setTitle("< TORrent >---< "+name+" >");
        //--- root ---
        root = new VBox(10);
        root.setPrefSize(310, 600);
        primaryStage.setScene(new Scene(root, 310, 600));
        //--- add parts ---
        createContent();
        primaryStage.show();
        //--- SERVER ---
        Thread serverThread = new Thread(
                thisServer = new  Server(name)
        );
        serverThread.setDaemon(true);
        serverThread.start();
        //--- LISTENERS ---
        thisServer.addListener(() -> updateFX());
        thisServer.addLogListener( () -> readMessages());
    }
    //=============================================

    private void createContent(){
        HBox ccBox = setMainHBox("Server name:", userTxt, ccBtn);
        ccBox.setPadding(new Insets(5, 5, 0, 5));
        logHistory.setPrefSize(310, 150);
        logHistory.setMinSize(310, 100);
        logHistory.setEditable(false);
        logHistory.setWrapText(true);

        //--- CONNECT ---
        userTxt.setOnAction( e-> ccBtn.fire());

        ccBtn.setOnAction( e-> { //CREATE SERVER CHECK & !REPEAT
            updateLog("--- Trying to establish connection with " +getUserTxt().toUpperCase()+" ---");
            String connect = getUserTxt().toUpperCase();
            if (connect.equals("BOB") || connect.equals("MIKE")
                    || connect.equals("ALICE") || connect.equals("JOHN")) {

                if(!checkIfAlreadyConnected(connect)) {
                    addConnectionHandler();
                }
                else
                    updateLog("Already connected, check log or stop taking pills :)");
            }
            else
                updateLog("--- No such server is available ---\n"+
                        "--- Try: BOB, MIKE, ALICE, JOHN ---");

        });
        //--- DRAG & DROP (works) ---

        logHistory.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if(db.hasFiles()){
                e.acceptTransferModes(TransferMode.COPY);
                String dropFileName = db.getFiles().get(0).getName();
            }
            e.consume();
        });

        logHistory.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            //List<File> files = e.getDragboard().getFiles();  //FOR MULTI DROP, BUT NOT IMPLEMENTING NOW
            try {
                boolean success = false;
                if (db.hasFiles()) {
                    String dropFileName = db.getFiles().get(0).getName();
                    File dst = new File(thisServer.getFolderPath()+"/"+dropFileName);
                    copyFileUsingStream(db.getFiles().get(0), dst);
                    success = true;
                    updateLog("#> File added to server: "+ dropFileName);
                    thisServer.updateFileList(dropFileName);
                }
                e.setDropCompleted(success);
                e.consume();
            }

            catch (Exception exe){
                exe.printStackTrace();
            }
        });
        //--- add to root ---
        root.getChildren().addAll( ccBox, setServerComboBox(), logHistory);
        root.setPrefSize(310,600);
    }

    //--- SERVER file list box ---
    private HBox setServerComboBox(){
        serverComboBox = new ComboBox<>();
        serverComboBox.setPromptText("Server files");
        serverComboBox.setPrefSize(200, 15);
        serverComboBox.setVisibleRowCount(5);
        //ACTIONS
        serverComboBox.setOnMousePressed( event -> {
            //messy but run out of time :/
            serverBoxData = FXCollections.observableArrayList(thisServer.getFileNamesList());
            serverComboBox.setItems(serverBoxData);
        });
        //COMBOBOX ACTION
        serverComboBox.setOnAction( e -> {
                    String selectedItem = serverComboBox.getSelectionModel().getSelectedItem();
                    String test = thisServer.getFolderPath() +"/"+ selectedItem;
                    if (!test.equals(thisServer.getPushFile()) && selectedItem!=null) {
                        thisServer.setPushFile(selectedItem);
                        System.out.println(selectedItem);
                        updateLog("--- File prepared to push  --- \n" +  thisServer.getPushFile()
                                + ", \nMD5 ["+thisServer.getMyServerFiles().get(selectedItem) +"]");
                    }
        });

        //DOWNLOAD
        multiBtn.setOnAction(e->{
            //CHECK ALL BOXES = SAME START DOWNLOADING
            if ( isMultiDownloadRequest() ) {
                System.out.println("TOTAL SPLITS: "+ getMultiDownload() );
                List<String> checkClients = new ArrayList<>(connectionHandlerMap.keySet());
                int split = getMultiDownload();
                boolean ifSameFiles = true;
                String checkMD5 = "first";
                List<String> onlyActive = new ArrayList<>();

                for (String handlerName : checkClients) {
                    ConnectionHandler current = connectionHandlerMap.get(handlerName);

                    if (current.getMyComboBox().getValue() !=null && current.isCheckBoxSelected() && split >=1) {
                        if (checkMD5.equals("first")) {
                            checkMD5 = current.getMD5();
                            onlyActive.add(handlerName);
                        }
                        else {
                            if (!checkMD5.equals(current.getMD5())) {
                                ifSameFiles = false;
                                current.getClientBox().setStyle("-fx-border-color: red; -fx-border-width: 2");
                            }
                            else
                                onlyActive.add(handlerName);
                        }
                        current.setSplit(split--);
                    }
                }

                if (ifSameFiles){
                    updateLog("--- Multi download ---");
                    thisServer.setServerMaxSplits(onlyActive.size());
                    connectionHandlerMap.forEach((key, value) -> {
                        if (onlyActive.contains(key)) {
                            value.getDownloadBtn().fire();
                        }
                    });
                }
                else
                    updateLog("--- MD5 of selected files DO NOT match! ---");
            }
        });

        //--------------
        HBox combo = new HBox( 5, serverComboBox, multiBtn);
        combo.setPadding(new Insets(0, 10, 0, 10));
        return combo;
    }

    //==== HANDLE CONNECTIONS ===
    private void addConnectionHandler(){
        NameToPort tmp = NameToPort.valueOf(getUserTxt().toUpperCase());
        ConnectionHandler current = new ConnectionHandler(thisServer, tmp.name());

        if (current.getHost() != null ) {
            connectionHandlerMap.put(tmp.name(), current);
        }
    }

    private boolean checkIfAlreadyConnected(String checkName){
        return connectionHandlerMap.containsKey(checkName);
    }

    private synchronized void removeConnectionHandler(ConnectionHandler connector){
        connectionHandlerMap.remove(connector.connectionName);
    }

    public ConnectionHandler getConnectionHandler(String name){
        return connectionHandlerMap.get(name);
    }

    //--- Server Box with label, txt and button ---
    private HBox setMainHBox(String labelName, TextField txtType, Button btnName){
        Label ccLbl = new Label(labelName);
        ccLbl.setPadding(new Insets(5, 5, 0, 5));
        btnName.setPrefWidth(80);
        txtType.setPrefSize(115, 15);
        ccLbl.setPrefSize(100, 15);

        return new HBox(5, ccLbl, txtType, btnName);
    }

    private synchronized void updateFX(){
        List<String> connectionServerLeft = thisServer.getConnectNames();
        Map<String, ConnectionHandler> concurrentHashMap = new ConcurrentHashMap<>(connectionHandlerMap);

        System.out.println("SERVER: "+ connectionServerLeft.size());
        System.out.println("UI HANDLER: "+ concurrentHashMap.size());

        if (connectionServerLeft.size() < concurrentHashMap.size()) {
            concurrentHashMap.forEach((e, k) -> {
                if (!connectionServerLeft.contains(e)) {
                    updateRoot(k);
                }
            });
        }
    }

    private void updateRoot(ConnectionHandler curr){
        if (!isVisible(curr))
            this.root.getChildren().add(curr.getClientBox());
        else {
            Platform.runLater(() -> this.root.getChildren().remove(curr.getClientBox()) );
            curr.logoutHandler();
        }
    }

    private synchronized boolean isVisible(ConnectionHandler currentBox){
        return this.root.getChildren().contains(currentBox.getClientBox());
    }

    public void stop() {
        //connectionHandlerMap.forEach((e,k) -> k.logoutHandler());
        isRunning.set(false);
    }

    private synchronized void updateMultiDownloadChoice(boolean selected) {
        if (this.multiDownload < 0) //just if more than 2 selected
            return;
        else {
            if (selected)
                this.multiDownload++;
            else
                this.multiDownload--;
        }
    }

    private synchronized boolean isMultiDownloadRequest(){
        return multiDownload > 1;
    }

    private int getMultiDownload() {
        return multiDownload;
    }

    private void readMessages() {
        //FIFO reader - read from queue incoming messages
        String str;
        while ((str = thisServer.getMessagesQueue().poll()) != null){
            updateLog(str);
        }
    }

    private synchronized void updateLog(String messages) {
        Platform.runLater(()-> logHistory.appendText(messages + "\n"));
    }

    //--- FOR Drag & Drop ---
    private void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public String getUserTxt() {
        return this.userTxt.getText();
    }

    public String getFileTxt() {
        return fileTxt.getText();
    }

    public Button getCcBtn() {
        return ccBtn;
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    //=====================================================
    //====          CONNECTIONS LOCAL + REMOTE         ====
    //=====================================================
    private class ConnectionHandler {
        private ObservableList<String> myComboBoxData;
        private ComboBox<String>                myComboBox;
        private CheckBox                        optSelect;
        private Button                          downloadBtn = new Button("Download");
        private Button                          sendBtn = new Button("Send");
        private Button                          logBtn = new Button("Logout");
        private VBox                            clientBox;
        //-- --
        private String                  connectionName;
        private Host                    host;
        private volatile int            split = 1;

        public ConnectionHandler(Server server, String connectionName){
            this.connectionName = connectionName;
            this.host = server.createNewConnection(connectionName); //let server add new host

            //--- start host ---
            if (host != null ) new Thread(host).start();

            createClientBox(connectionName);
            updateRoot(this);
        }

        //--- Client Box with dropdown, checkbox and button ---
        private void createClientBox(String connectedTo){
            clientBox = new VBox(2);
            clientBox.setPrefSize(300, 110);
            clientBox.setStyle("-fx-border-style: solid inside; -fx-border-width: 1;");
            clientBox.setPadding(new Insets(5, 5, 10, 5));
            //ADD
            clientBox.getChildren().addAll( connectionInfo(connectedTo),
                        comboAndCheckBox(),setClientButtons() );
        }

        //--- DOWNLOAD, SEND AND LOGOUT ---
        private HBox setClientButtons(){
            downloadBtn.setAlignment(Pos.CENTER);
            downloadBtn.setPadding(new Insets(5, 5, 5, 5));
            downloadBtn.setPrefWidth(80);
            sendBtn.setAlignment(Pos.CENTER);
            sendBtn.setPadding(new Insets(5, 5, 5, 5));
            sendBtn.setPrefWidth(80);
            logBtn.setAlignment(Pos.CENTER);
            logBtn.setPadding(new Insets(5, 5, 5, 5));
            logBtn.setPrefWidth(60);

            //DOWNLOAD
            downloadBtn.setOnAction(e->{
                boolean isMyComboBoxEmpty = myComboBox.getSelectionModel().isEmpty();

                if (myComboBox.getValue()!=null && !isMyComboBoxEmpty && !checkIfMultiDownload()) {
                    updateLog("\n --- Downloading from "+ connectionName+" --- \n" + myComboBox.getValue()
                            +", MD5 ["+host.getConnectedToServerFiles().get(myComboBox.getValue()) +"]");
                    System.out.println("--- myComboBox.getValue() --" + myComboBox.getValue());
                    thisServer.setServerMaxSplits(1);   //In CASE: multiple was selected -> update
                    //1 - SEND request with file name to download
                    host.sendCommand(1, myComboBox.getValue());
                }
                else if (myComboBox.getValue()!=null && !isMyComboBoxEmpty){
                    System.out.println("--- btn (multi download) --");
                    System.out.println("getSplit() "+ getSplit());
                    System.out.println("getMultiDownload "+getMultiDownload());
                    //5 - SEND request for multi download
                    host.setSplitNumber(getSplit());
                    host.sendCommand(5, myComboBox.getValue());
                    host.sendCommand(getSplit(),String.valueOf(getMultiDownload()));
                    System.out.println("---------------------------------------");
                }
            });

            //SEND
            sendBtn.setOnAction(e->{
                boolean isServerBoxEmpty = serverComboBox.getSelectionModel().isEmpty();

                if (serverComboBox.getValue()!=null && !isServerBoxEmpty ) {
                    updateLog("\n --- Sending "+ serverComboBox.getValue()+" ---");
                    //3 - PUSH request with file name
                    host.sendCommand(3, serverComboBox.getValue());
                }
            });

            //LOGOUT
            logBtn.setOnAction( e-> {
                try {
                    host.getOut().writeUTF("logout");
                    thisServer.removeDisconnected(getHost());
                }
                catch (Exception eve){
                    updateLog(eve.getMessage());
                }
                //--- check if handler is correct --
                System.out.println("--- HOW MANY -> Multi Checked --- \n" + getMultiDownload());
            });

            return new HBox(40, sendBtn, downloadBtn, logBtn);
        }

        private HBox connectionInfo(String connectedTo){
            Label servNameLbl = new Label(connectedTo);
            servNameLbl.setPadding(new Insets(5, 5, 0, 5));
            servNameLbl.setPrefSize(100, 15);
            servNameLbl.setStyle("-fx-font-size: 12px;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-text-fill: #3b3a3e;\n" +
                    "    -fx-alignment: Center;\n" +
                    "    -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,2 );");
            //---
            Label downloadLbl = new Label("Select box to set multi download.");
            downloadLbl.setPadding(new Insets(5, 5, 0, 5));
            downloadLbl.setPrefSize(200, 15);

            return new HBox(5, servNameLbl, downloadLbl);
        }

        private HBox comboAndCheckBox(){
            myComboBox = new ComboBox<>();
            myComboBox.setPromptText("Check available files");
            myComboBox.setPrefSize(220, 15);
            optSelect = new CheckBox("Add");
            optSelect.setPadding(new Insets(5, 5, 0, 5));
            optSelect.setSelected(false);

            myComboBoxData = FXCollections.observableArrayList(host.getServerFileList());
            ListProperty<String> listProperty = new SimpleListProperty<>(myComboBoxData);
            myComboBox.itemsProperty().bindBidirectional(listProperty);
            listProperty.addListener((observable, oldList, newList) -> {
                System.out.println("List changed");
                System.out.println(newList);

            });

            setMyComboBox(myComboBox);
            myComboBox.setItems(myComboBoxData);
            myComboBox.setVisibleRowCount(6);

            //ACTIONS
            myComboBox.setOnMouseClicked( event -> {
                if (!(myComboBox.getItems().size() > 0))
                    myComboBoxData.addAll(host.getServerFileList());


            });

            myComboBox.setOnAction( e -> {
                String selectedItem = myComboBox.getSelectionModel().getSelectedItem();
                System.out.println(selectedItem);
                clientBox.setStyle("-fx-border-color: black;");
            });

            optSelect.selectedProperty().addListener( evt -> {
                        updateMultiDownloadChoice(optSelect.isSelected());

                        if (!optSelect.isSelected())
                            clientBox.setStyle("-fx-border-color: black");
                    }
            );
            //--------------
            HBox clientComboBox = new HBox(5, myComboBox, optSelect);
            clientComboBox.setPrefSize(310,600);
            return clientComboBox;
        }

        //--- have to ask server for all connected clients choice ---
        private synchronized boolean checkIfMultiDownload(){
            return isMultiDownloadRequest();
        }

        private void setMyComboBox(ComboBox myBox){
            // Define rendering of the list of values in ComboBox drop down.
            myBox.setCellFactory((comboBox) -> {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            //setTextFill(Color.GREEN);
                            setText(item);
                        }
                    }
                };
            });

            // Define rendering of selected value shown in ComboBox.
            myBox.setConverter(new StringConverter<String>() {
                @Override
                public String toString(String fileName) {
                    if (fileName == null) {
                        return null;
                    } else {
                        return fileName;
                    }
                }
                @Override
                public String fromString(String itemString) {
                    return null; // No conversion fromString needed.
                }
            });
        }

        //--- logout button ---
        synchronized void logoutHandler(){
            if (this.isCheckBoxSelected())
                updateMultiDownloadChoice(false); //set opt -1
            thisServer.setServerMaxSplits(getMultiDownload());
            updateLog("--- Disconnected from " + connectionName +" ---");
            removeConnectionHandler(this);
            if (host!=null)
                host.stopConnection();
        }

        //SETTERS GETTERS
        public String getMD5(){
            return host.getConnectedToServerFiles().get(myComboBox.getValue());
        }

        public void initMyComboBoxData() {
            this.myComboBoxData = FXCollections.observableArrayList(host.getServerFileList());
        }

        public String getConnectionName() {
            return connectionName;
        }

        public boolean isCheckBoxSelected(){
            return this.optSelect.isSelected();
        }

        public synchronized void setSplit(int split) {
            this.split = split;
        }

        public synchronized int getSplit() {
            return split;
        }

        public Button getDownloadBtn() {
            return downloadBtn;
        }

        public Button getLogBtn() {
            return logBtn;
        }

        public Host getHost() {
            return host;
        }

        public VBox getClientBox(){
            return clientBox;
        }

        public ComboBox<String> getMyComboBox() {
            return myComboBox;
        }
    }
}
