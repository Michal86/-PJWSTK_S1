package main.controller;

import main.model.TheModel;
import main.view.InputView;
import main.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class TheController {

    private List<InputView> viewList;
    private MainView mainView;
    private TheModel myModel;

    public TheController(MainView mainView, TheModel myModel, List<InputView> viewList){
        this.viewList = viewList;
        this.mainView = mainView;
        this.myModel = myModel;

        this.mainView.addViewListener(e -> {
            this.viewList = checkDisposedWindows(viewList);

            //--- add new window and update display area ---
            InputView newInView = new InputView("Wiadomości", "DODAJ", "Historia wpisów", "Po wprowadzeniu tekstu kliknij DODAJ");
            this.viewList.add(newInView);
            newInView.setMsgArea(this.myModel.read().toString());

            newInView.addViewListener(
                p -> {
                   if(!newInView.getInputField().equals("")) {
                       sendInputToModel(newInView.getInputField());
                       newInView.clearInput();
                       updateInputViews();
                   }
               }
            );
        });
    }

    //--- sends input to my model ---
    private void sendInputToModel(String addString){
        myModel.storeMsg(addString);
    }

    //--- update text in all opened windows --
    private synchronized void updateInputViews() {
        this.viewList.forEach(
                view -> view.setMsgArea(myModel.read().toString())
        );
        this.viewList = checkDisposedWindows(viewList);
    }

    //--- remove disposed windows, get cleared list ---
    private synchronized List<InputView> checkDisposedWindows(List<InputView> listToUpdate) {
        if (!listToUpdate.isEmpty()) {
            List<InputView> updatedList = new ArrayList<>();

            listToUpdate.forEach(
                view -> {
                    if (view.isVisible())
                        updatedList.add(view);
                }
            );
            listToUpdate.clear();
            return updatedList;
        }
        return listToUpdate;
    }
}
