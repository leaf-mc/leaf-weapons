package mc.leaf.modules.weapons.lib.converters.bukkit;

import mc.leaf.core.api.command.interfaces.IParameterConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * ArgumentConverter to use when you want to convert a command argument to a Player instance.
 *
 * @author alexpado
 */
public class PlayerConverter implements IParameterConverter<String, Player> {

    @Override
    public Player convert(String in) {

        return Bukkit.getPlayer(in);
    }

}
