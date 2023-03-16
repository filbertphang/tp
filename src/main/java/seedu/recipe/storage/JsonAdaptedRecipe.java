package seedu.recipe.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.model.recipe.Ingredient;
import seedu.recipe.model.recipe.Name;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.RecipeDuration;
import seedu.recipe.model.recipe.RecipeIngredient;
import seedu.recipe.model.recipe.RecipePortion;
import seedu.recipe.model.recipe.Step;

import seedu.recipe.model.recipe.unit.IngredientAmountUnit;
import seedu.recipe.model.recipe.unit.PortionUnit;

import seedu.recipe.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Recipe}.
 */
class JsonAdaptedRecipe {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Recipe's %s field is missing!";
    private String name;
    private String portion;
    private String duration;
    private String tags;
    private String ingredients;
    private String steps;

    /**
     * Constructs a {@code JsonAdaptedRecipe} with the given recipe details.
     */
    @JsonCreator
    public JsonAdaptedRecipe(
            @JsonProperty("name") Name name,
            @JsonProperty("portion") Optional<RecipePortion> portion,
            @JsonProperty("duration") Optional<RecipeDuration> duration,
            @JsonProperty("tags") Optional<Set<Tag>> tags,
            @JsonProperty("ingredient") Optional<List<Ingredient>> ingredients,
            @JsonProperty("steps") Optional<List<Step>> steps) {
        this.name = name.toString();
        this.portion = portion.toString();
        this.duration = duration.toString();
        this.ingredients = ingredients.toString();
        this.tags = tags.toString();
        this.steps = steps.toString();
    }

    /**
     * Converts a given {@code Recipe} into this class for Jackson use.
     */
    public JsonAdaptedRecipe(Recipe source) {
        name = source.getName().toString();
        portion = source.getPortion().toString();
        duration = source.getDuration().toString();
        ingredients = source.getIngredients().toString();
        tags = source.getTags().toString();
        steps = source.getSteps().toString();
    }

    /**
     * Converts this Jackson-friendly adapted recipe object into the model's {@code Recipe} object.
     * Remember only name field is required, and the rest are optional. 
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted recipe.
     */
    public Recipe toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        // Example portion input is "10-20 g"
        String[] parameters = portion.split("[-,\\s+]");
        int lowerRange = Integer.parseInt(parameters[0]);
        int upperRange = Integer.parseInt(parameters[1]);
        PortionUnit portionUnit = new PortionUnit(parameters[2]);
        final Optional<RecipePortion> optionalModelPortion = Optional.of(
                new RecipePortion(lowerRange, upperRange, portionUnit)
        );

        // Example RecipeDuration input is "15 m"
        // parameters = duration.split(" ");
        // double time = Double.parseDouble(parameters[0]);
        // TimeUnit timeUnit = new TimeUnit(parameters[1]);
        final Optional<RecipeDuration> optionalModelDuration = Optional.of(
                RecipeDuration.of(duration)
        );

        // Example RecipeIngredient input is "2 potato" (minimum)
        // Example RecipeIngredient input is "5 tbsp pepper/salt" (maximum)
        List<Ingredient> OutputIngredientList = new ArrayList<>();
        String[] ingredientsArray= ingredients.split(",");
        for (String ingredient : ingredientsArray) {
            parameters = ingredient.split("[\\s+,/]"); 
            double ingredientAmount = Double.parseDouble(parameters[0]);
            IngredientAmountUnit ingredientUnit = new IngredientAmountUnit(parameters[1]);        
            String ingredientName = parameters[2];
            try {
                HashSet<Ingredient> substitutions = new HashSet<>();
                String[] substitutionsArray = parameters[3].split("[\\s+]");
                for (String substitution : substitutionsArray) {
                    Ingredient newSubstitution = new Ingredient(substitution);
                    substitutions.add(newSubstitution);
                }
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            } finally {
                RecipeIngredient newIngredient = new RecipeIngredient(ingredientName, ingredientAmount);
                OutputIngredientList.add(newIngredient);
            }       
        }    
        /*Right now RecipeIngredient has no way to take in Unit and Substitution in constructor method,
        so either we add it or we just set() unit and substitution under modelIngredients here.
        */
        Optional<List<Ingredient>> ingredients = Optional.of(OutputIngredientList);

        List<Step> stepList = new ArrayList<>();
        String[] stepArray = steps.split(",");
        for (String step : stepArray) {
            Step newStep = new Step(step);
            stepList.add(newStep);
        }
        if (!stepList.isEmpty()) {
            Optional<List<Step>> steps = Optional.of(stepList);
        }
        
        Set<Tag> output = new HashSet<>();
        String[] tagList = tags.split(",");
        for (String tag : tagList) {
            Tag newTag = new Tag(tag);
            output.add(newTag);
        }
        if (!output.isEmpty()) {
            final Optional<Set<Tag>> optionalModelTags = Optional.of(output);
        }
        Recipe res = new Recipe(modelName);
        //Setter methods/overloaded constructor
        return res;
    }

}
