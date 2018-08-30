package ir.waspar.resturanttestapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataFakeGenerator {

    public List<Food> foods(){
        List<Food> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Food food = food();
            list.add(new Food(i+1 , food.getProvider() , food.getName() , food.getCalory() , food.getWeight() , food.getCookTime() , food.getPrice() , food.getMaterials() ));
        }
        return list;
    }

    private Food food(){
        Food food = new Food(0 , foodProvider()  , foodName() , getRandomNumberInRange(1 , 200) , getRandomNumberInRange(100 , 800) , getRandomNumberInRange(10 , 30) , getRandomNumberInRange(5 , 50) , foodMaterials());
        return food;
    }

    private String foodProvider(){
        String name ;
        List<String> list = new ArrayList<>();
        list.add("Waspar");
        list.add("GreenMind");
        list.add("Shygan");
        list.add("Sharjbook");
        list.add("Amiran");
        list.add("FelFel");

        Random rand = new Random();
        name = list.get(rand.nextInt(list.size()));

        return name;
    }

    private List<String> foodMaterials(){
        List<String> list = new ArrayList<>();
        list.add("material 1");
        list.add("material 2");
        list.add("material 3");
        list.add("material 4");
        list.add("material 5");
        list.add("material 6");
        list.add("material 7");
        list.add("material 8");
        list.add("material 9");
        list.add("material 10");
        list.add("material 11");

        Random rand = new Random();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(list.size()) ; i++) {
            list1.add(list.get(rand.nextInt(list.size())));
        }
        return list1;
    }

    private int getRandomNumberInRange(int min, int max) {
        return (min + (int) (Math.random() * ((max - min) + 1)));
    }

    private String foodName(){
        String name ;
        List<String> list = new ArrayList<>();
        list.add("salad");
        list.add("sandwich");
        list.add("bread");
        list.add("steak");
        list.add("tuna steak");
        list.add("fish");
        list.add("shrimp");
        list.add("rice");
        list.add("spaghetti");
        list.add("pizza");
        list.add("hamburger");
        list.add("eggs");
        list.add("cheese");
        list.add("sausages");
        list.add("apple juice");
        list.add("grape juice");
        list.add("milk");
        list.add("candy");
        list.add("cookie");
        list.add("pie");
        list.add("cake");
        list.add("cupcake");

        Random rand = new Random();
        name = list.get(rand.nextInt(list.size()));

        return name;
    }


}
