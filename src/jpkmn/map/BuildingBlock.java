package jpkmn.map;

public class BuildingBlock {
  public BuildingBlock(Area a) {
    _area = a;
  }

  public void addCenter() {
    // TODO : stuff...
  }

  public void addShop(int shopID) {
    // TODO : stuff...
  }

  // TODO : add misc buildings :(

  private class ShopStock {
    public ShopStock(int itemID, int price) {
      _itemID = itemID;
      _price = price;
    }

    private int _itemID, _price;
  }

  private Area _area;
}