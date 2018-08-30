package ir.waspar.resturanttestapp;

import java.util.List;

class Food {

    private int ID;
    private String Provider;
    private String Name;
    private int Calory;
    private int Weight;
    private int CookTime;
    private List<String> Materials;

    Food(int id, String provider, String name, int calory, int weight, int cookTime, List<String> materials) {
        ID = id;
        Provider = provider;
        Name = name;
        Calory = calory;
        Weight = weight;
        CookTime = cookTime;
        Materials = materials;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCalory() {
        return Calory;
    }

    public void setCalory(int calory) {
        Calory = calory;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getCookTime() {
        return CookTime;
    }

    public void setCookTime(int cookTime) {
        CookTime = cookTime;
    }

    public List<String> getMaterials() {
        return Materials;
    }

    public void setMaterials(List<String> materials) {
        Materials = materials;
    }
}
