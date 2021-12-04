package cz.pk.adventofcode.y2020.day21;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.pk.adventofcode.y2020.day1.Day1;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cz.pk.adventofcode.util.DataCollector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Day21 {

    private final boolean debug;

    @Data
    @AllArgsConstructor
    class Food {
        private final Set<String> ingredients;
        private final Set<String> allergens;

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("ingredients", ingredients)
                    .append("allergens", allergens)
                    .toString();
        }
    }

    class TypeCollector extends DataCollector<Food> {

        public TypeCollector(String file) {
            super(file);
        }

        @Override
        protected Food processLine(String line) {
            String[] parts = line.split("\\(");
            assert parts.length == 2;
            String[] ingredients = parts[0].trim().split(" ");
            assert parts[1].startsWith("contains ");
            String allergensLine = parts[1].substring("contains ".length()).split("\\)")[0];
            String[] allergens = allergensLine.split(", ");
            return new Food(
                    new HashSet<>(Arrays.asList(ingredients)),
                    new HashSet<>(Arrays.asList(allergens)));
        }
    }

    public String solve(String file) {
        List<Food> foods = new TypeCollector(file).process();
        System.out.println(foods);

        // prepare data
        Set<String> allergens = new HashSet<>();
        Set<String> ingredients = new HashSet<>();
        Map<Integer, Food> foodById = new HashMap<>();
        Map<String, List<Integer>> foodByAllergen = new HashMap<>();
        //
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            allergens.addAll(food.getAllergens());
            ingredients.addAll(food.getIngredients());
            foodById.put(i, food);
            for (String allergen : food.getAllergens()) {
                foodByAllergen.computeIfAbsent(allergen, k -> new ArrayList<>()).add(i);
            }
        }

        Set<String> ingredientsWithAllergen = new HashSet<>();
        Map<String, Set<String>> ingredientsByAllergen = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : foodByAllergen.entrySet()) {
            System.out.println("Checking allergen: " + entry.getKey());
            Set<String> ingredientsIntersect = foodById.get(entry.getValue().get(0)).getIngredients();
            for (int i = 1; i < entry.getValue().size(); i++) {
                Integer foodId = entry.getValue().get(i);
                ingredientsIntersect = intersect(ingredientsIntersect, foodById.get(foodId).getIngredients());
            }
            System.out.println(ingredientsIntersect);
            ingredientsByAllergen.put(entry.getKey(), ingredientsIntersect);
            ingredientsWithAllergen.addAll(ingredientsIntersect);
        }

        Set<String> ingredientsWithoutAllergen = new HashSet<>();
        for (String i : ingredients) {
            if (!ingredientsWithAllergen.contains(i)) {
                ingredientsWithoutAllergen.add(i);
            }
        }
        System.out.println(ingredientsWithoutAllergen);

//        int sum = 0;
//        for (String ingredient : ingredientsWithoutAllergen) {
//            for (Food food : foods) {
//                if (food.getIngredients().contains(ingredient)) {
//                    sum++;
//                }
//            }
//        }

        Map<String, String> allergenByIngredients = new HashMap<>();
        while (true) {
            String matchedIngredient = null;
            for (Map.Entry<String, Set<String>> entry : ingredientsByAllergen.entrySet()) {
                // allergen, ingredients
                if (entry.getValue().size() == 1) {
                    matchedIngredient = entry.getValue().iterator().next();
                    allergenByIngredients.put(matchedIngredient, entry.getKey());
                    System.out.println(matchedIngredient + "\t" + entry.getKey());
                }
            }
            if (matchedIngredient == null) {
                break;
            }
            for (Set<String> ingredientList : ingredientsByAllergen.values()) {
                ingredientList.remove(matchedIngredient);
            }
        }
        assert allergenByIngredients.size() == ingredientsByAllergen.size();

        StringBuilder sb = new StringBuilder();
        for (String ingredient : allergenByIngredients.keySet()) {
            sb.append(ingredient).append(",");
        }

        return sb.toString();
    }

    private Set<String> intersect(Set<String> s1, Set<String> s2) {
        Set<String> out = new HashSet<>(s1);
        for (String s : s1) {
            if (!s2.contains(s)) {
                out.remove(s);
            }
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println(Day21.class);
        String count;
        //*
        count = new Day21(true).solve("2020/day21_test.txt");
        System.out.println("Result: " + count);
        assert count.equals("mxmxvkd,sqjhc,fvjkl,");

        count = new Day21(true).solve("2020/day21.txt");
        System.out.println("Result: " + count);
        //assert count == 1930;

        /*/

        count = new Day13(true).solve2("2020/day12_test.txt");
        System.out.println("Result: " + count);
        assert count == 25;

        count = new Day13(true).solve2("2020/day12.txt");
        System.out.println("Result: " + count);
        //assert count == 27016;

         */
        //*/
    }
}
