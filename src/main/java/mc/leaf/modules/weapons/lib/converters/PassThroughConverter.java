package mc.leaf.modules.weapons.lib.converters;

import org.jetbrains.annotations.NotNull;

/**
 * Default converter for all arguments. The output will be the same as the input.
 *
 * @author alexpado
 */
public class PassThroughConverter implements ArgumentConverter<String> {

    /**
     * Return the value provided without any mutation.
     *
     * @param input
     *         The value to return
     *
     * @return The value provided
     */
    @Override
    @NotNull
    public String output(String input) {
        return input;
    }

}
