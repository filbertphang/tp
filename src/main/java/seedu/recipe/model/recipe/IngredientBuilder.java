package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.AMOUNT_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.COMMON_NAME_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.ESTIMATE_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.NAME_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.REMARK_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.SUBSTITUTION_PREFIX;
import static seedu.recipe.model.recipe.ingredient.IngredientParser.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.recipe.logic.commands.EditCommand;
import seedu.recipe.logic.parser.Prefix;
import seedu.recipe.model.recipe.ingredient.Ingredient;
import seedu.recipe.model.recipe.ingredient.IngredientQuantifier;
import seedu.recipe.model.recipe.ingredient.IngredientQuantity;

/**
 * Represents a recipe's ingredient in the recipe book.
 * Guarantees: immutable; is valid as declared in {@link #isValidIngredient(String)}
 */
public class IngredientBuilder {
    public static final String MESSAGE_CONSTRAINTS =
            "Ingredients should follow this format: \n"
            + "`[-a AMOUNT] [-e ESTIMATED AMOUNT] -n NAME `"
            + "`[-cn COMMON NAME] [-r REMARKS]... [-s SUBSTITUTION]...\n"
            + "i.e. `-a 1 oz. -n butter -r cubed -s margarine`";
    public static final String VALIDATION_REGEX =
            "^(([1-9][0-9]*|(([0-9]|[1-9][0-9]+)[./][0-9]+))([a-z.-]+)?|[A-Za-z().,/-]+)(\\s+[0-9A-Za-z().,+-/:;]+)*$";

    public final String name;

    private final HashMap<Prefix, List<String>> arguments;

    /**
     * Constructs a {@code IngredientBuilder}.
     *
     * @param name A valid ingredient number.
     */
    public IngredientBuilder(String name) {
        requireNonNull(name);
        HashMap<Prefix, List<String>> tokens = parse(name);
        checkArgument(tokens.containsKey(NAME_PREFIX), MESSAGE_CONSTRAINTS);
        this.name = name;
        this.arguments = tokens;
    }

    /**
     * Returns true if a given string is a valid ingredient.
     */
    public static boolean isValidIngredient(String test) {
        HashMap<Prefix, List<String>> tokens = parse(test);
        return tokens.containsKey(NAME_PREFIX);
    }

    /**
     * Generates and returns a Key-Value pair for the enclosing {@code Recipe}
     * instance to store.
     * @return The {@code HashMap} instance containing the key-value pair.
     */
    public HashMap<Ingredient, IngredientQuantifier> build() {
        HashMap<Ingredient, IngredientQuantifier> out = new HashMap<>();
        Ingredient main = Ingredient.of(this.arguments.get(NAME_PREFIX).get(0));
        IngredientQuantity quantity = null;
        String commonName = null;
        List<String> remarks = new ArrayList<>();
        List<String> estimatedQuantity = new ArrayList<>();
        Set<Ingredient> substitutes = new HashSet<>();
        if (arguments.containsKey(AMOUNT_PREFIX)) {
            quantity = IngredientQuantity.of(arguments.get(AMOUNT_PREFIX).get(0));
        }
        if (arguments.containsKey(COMMON_NAME_PREFIX)) {
            commonName = arguments.get(COMMON_NAME_PREFIX).get(0);
        }
        if (arguments.containsKey(REMARK_PREFIX)) {
            remarks.addAll(arguments.get(REMARK_PREFIX));
        }
        if (arguments.containsKey(ESTIMATE_PREFIX)) {
            estimatedQuantity.addAll(arguments.get(ESTIMATE_PREFIX));
        }
        if (arguments.containsKey(SUBSTITUTION_PREFIX)) {
            substitutes.addAll(arguments.get(SUBSTITUTION_PREFIX)
                    .stream()
                    .map(Ingredient::of)
                    .collect(Collectors.toList())
            );
        }
        out.put(main, new IngredientQuantifier(
                quantity,
                commonName,
                estimatedQuantity.toArray(String[]::new),
                remarks.toArray(String[]::new),
                substitutes.toArray(Ingredient[]::new)
        ));
        return out;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IngredientBuilder // instanceof handles nulls
                && name.equals(((IngredientBuilder) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
