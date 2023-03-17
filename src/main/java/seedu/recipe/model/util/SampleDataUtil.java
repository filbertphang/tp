package seedu.recipe.model.util;

import seedu.recipe.model.ReadOnlyRecipeBook;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.recipe.Ingredient;
import seedu.recipe.model.recipe.Name;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.RecipeDuration;
import seedu.recipe.model.recipe.RecipePortion;
import seedu.recipe.model.recipe.Step;
import seedu.recipe.model.tag.Tag;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains utility methods for populating {@code RecipeBook} with sample data.
 */
public class SampleDataUtil {
    public static Recipe[] getSampleRecipes() {
        Recipe sample1 = new Recipe(new Name("Chicken and Broccoli Stir Fry"));
        sample1.setDuration(RecipeDuration.of("15 minutes"));
        sample1.setTags(new Tag("Chinese"),
                        new Tag("Healthy"));
        sample1.setIngredients(new Ingredient("Chicken Breast"),
                               new Ingredient("Broccoli"),
                               new Ingredient("Soy Sauce"),
                               new Ingredient("Vegetable Oil"),
                               new Ingredient("Garlic"),
                               new Ingredient("Bell Pepper"));
        sample1.setSteps(new Step("In a small bowl, mix soy sauce and crushed garlic."),
                         new Step("In a wok, heat vegetable oil over high heat."),
                         new Step("Add chicken and stir-fry for 2-3 minutes, until browned."),
                         new Step("Add broccoli and stir-fry for another 2-3 minutes."),
                         new Step("Pour the sauce over the chicken and vegetables and stir-fry for " +
                                          "another 2-3 minutes, until the sauce thickens and coats everything evenly."),
                         new Step("Serve.)"));

        Recipe sample2 = new Recipe(new Name("Balsamic Glazed Chicken"));
        sample2.setPortion(RecipePortion.of("1 - 2 Portions"));
        sample2.setTags(new Tag("GlutenFree"), new Tag("Healthy"));
        sample2.setIngredients(new Ingredient("Chicken Breast"),
                               new Ingredient("Balsamic Vinegar"),
                               new Ingredient("Honey"));
        sample2.setSteps(new Step("Preheat oven to 375 F."),
                         new Step("In a small bowl, mix together balsamic vinegar and honey."),
                         new Step("Arrange chicken breasts in a baking dish and pour the balsamic vinegar over them."),
                         new Step("Bake for 25-30 minutes, until the chicken is cooked through " +
                                          "and the glaze is thick and bubbly."),
                         new Step("Serve hot.)"));

        Recipe sample3 = new Recipe(new Name("Cacio E Pepe Pasta"));
        sample3.setDuration(RecipeDuration.of("15 minutes"));
        sample3.setPortion(RecipePortion.of("1 - 3 person"));
        sample3.setIngredients(new Ingredient("2 whole eggs"),
                               new Ingredient("100g spaghetti noodles"));
        sample3.setSteps(new Step("Crack the egg and separate the yolks and whites"),
                         new Step("Raise a pot of water to the boil and add the spaghetti"));

        return new Recipe[]{sample1, sample2, sample3};
    }

    public static ReadOnlyRecipeBook getSampleRecipeBook() {
        RecipeBook sampleRb = new RecipeBook();
        for (Recipe sampleRecipe : getSampleRecipes()) {
            sampleRb.addRecipe(sampleRecipe);
        }
        return sampleRb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }
}
