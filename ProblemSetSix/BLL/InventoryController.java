package ProblemSetSix.BLL;

import ProblemSetSix.DAL.InventoryDataProvider;

public class InventoryController {

    private InventoryDataProvider dataProvider = new InventoryDataProvider();

    public void addOrUpdateItem(String name, int quantity, double price) throws Exception {
        dataProvider.addOrUpdateItem(name, quantity, price);
    }
}