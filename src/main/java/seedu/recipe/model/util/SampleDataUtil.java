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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contains utility methods for populating {@code RecipeBook} with sample data.
 */
public class SampleDataUtil {
    public static Recipe[] getSampleRecipes() {
        Recipe sample1 = new Recipe(new Name("Chicken and Broccoli Stir-Fry"));
        sample1.setDuration(RecipeDuration.of("15 minutes"));
        sample1.setTags(createArrayFromItemNames(Tag::new, "Chinese", "Healthy"));
        sample1.setIngredients(createArrayFromItemNames(Ingredient::new, "Chicken Breast", "Broccoli", "Soy Sauce",
                                                        "Vegetable Oil", "Garlic", "Bell Pepper"));
        sample1.setSteps(createArrayFromItemNames(Step::new, "1 In a small bowl, mix soy sauce and crushed garlic.",
                                                  "2 In a wok, heat vegetable oil over high heat.",
                                                  "3 Add chicken and stir-fry for 2-3 minutes, until browned.",
                                                  "4 Add broccoli and stir-fry for another 2-3 minutes.",
                                                  "5 Pour the sauce over the chicken and vegetables and stir-fry for another 2-3 minutes, until the sauce thickens and coats everything evenly.",
                                                  "6 Serve."
                                                 ));

        Recipe sample2 = new Recipe(new Name("Balsamic Glazed Chicken"));
        sample1.setPortion(RecipePortion.of("1 - 2 Portions"));
        sample1.setDuration(RecipeDuration.of("40 minutes"));
        sample1.setTags(createArrayFromItemNames(Tag::new, "Gluten-free", "Healthy"));
        sample1.setIngredients(createArrayFromItemNames(Ingredient::new, "Chicken Breast", "Balsamic Vinegar",
                                                        "Honey"));
        sample1.setSteps(createArrayFromItemNames(Step::new, "1 Preheat oven to 375 F.",
                                                  "2 In a small bowl, mix together balsamic vinegar and honey.",
                                                  "3 Arrange chicken breasts in a baking dish and pour the balsamic vinegar over them.",
                                                  "4 Bake for 25-30 minutes, until the chicken is cooked through and the glaze is thick and bubbly.",
                                                  "5 Serve hot."
                                                 ));

        Recipe sample3 = new Recipe(new Name("Cacio E Pepe Pasta"));
        sample3.setDuration(RecipeDuration.of("15 minutes"));
        sample3.setPortion(RecipePortion.of("1 - 3 person"));
        sample3.setIngredients(
                new Ingredient("2 whole eggs"),
                new Ingredient("100g spaghetti noodles")
                              );
        sample3.setSteps(
                new Step("Crack the egg and separate the yolks and whites"),
                new Step("Raise a pot of water to the boil and add the spaghetti")
                        );

        return new Recipe[]{sample1, sample2, sample3};
    }

    public static ReadOnlyRecipeBook getSampleRecipeBook() {
        RecipeBook sampleAb = new RecipeBook();
        for (Recipe sampleRecipe : getSampleRecipes()) {
            sampleAb.addRecipe(sampleRecipe);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings).map(Tag::new).collect(Collectors.toSet());
    }

    // generates an array of items (Tag[], Ingredients[], Step[], ...)
    // from a list of Strings (the item names)
    // assumption: generator function only needs their name to create an item
    public static <T> T[] createArrayFromItemNames(Function<String, T> generator, String... items) {
        // probably safe enough
        return (T[]) Arrays.stream(items).map(generator).toArray();
    }

}
