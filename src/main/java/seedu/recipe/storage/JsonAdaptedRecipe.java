package seedu.recipe.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final String name;
    private final RecipePortion portion;
    private final RecipeDuration duration;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedIngredient> ingredients = new ArrayList<>();
    private final List<JsonAdaptedStep> steps = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedRecipe} with the given recipe details.
     */
    @JsonCreator
    public JsonAdaptedRecipe(
            @JsonProperty("name") Name name,
            @JsonProperty("portion") RecipePortion portion,
            @JsonProperty("duration") RecipeDuration duration,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("ingredient") List<JsonAdaptedIngredient> ingredients,
            @JsonProperty("steps") List<JsonAdaptedStep> steps) {
        this.name = name.toString();
        this.portion = portion;
        this.duration = duration;

        if (tags != null) {
            this.tags.addAll(tags);
        };

        if (ingredients != null) {
            this.ingredients.addAll(ingredients);
        }

        if (steps != null) {
            this.steps.addAll(steps);
        }
    }

    /**
     * Converts a given {@code Recipe} into this class for Jackson use.
     */
    public JsonAdaptedRecipe(Recipe source) {
        name = source.getName().toString();
        portion = source.getPortion();
        duration = source.getDuration();

        tags.addAll(
                source.getTags().stream()
                    .map(JsonAdaptedTag::new)
                    .collect(Collectors.toList())
        );
        ingredients.addAll(
                source.getIngredients().stream()
                        .map(JsonAdaptedIngredient::new)
                        .collect(Collectors.toList())
        );
        steps.addAll(
                source.getSteps().stream()
                        .map(JsonAdaptedStep::new)
                        .collect(Collectors.toList())
        );
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
