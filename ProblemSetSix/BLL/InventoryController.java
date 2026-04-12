package ProblemSetSix.BLL;

import ProblemSetSix.DAL.InventoryDataProvider;

public class InventoryController {

    private InventoryDataProvider dataProvider = new InventoryDataProvider();

    public void addOrUpdateItem(String name, String category, int quantity, double price) throws Exception {
        dataProvider.addOrUpdateItem(name, category, quantity, price);
    }
}