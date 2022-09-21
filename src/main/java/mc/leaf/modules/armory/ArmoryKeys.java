package mc.leaf.modules.armory;

import mc.leaf.core.interfaces.ILeafModule;
import org.bukkit.NamespacedKey;

public final class ArmoryKeys {

    private ArmoryKeys() {}

    public static NamespacedKey ammunition(ILeafModule module) {

        return new NamespacedKey(module.getPlugin(), "ammunition");
    }

    public static NamespacedKey normalAmmunition(ILeafModule module, int tier) {

        return new NamespacedKey(module.getPlugin(), "ammunition.normal.%s".formatted(tier));
    }

    public static NamespacedKey explosiveAmmunition(ILeafModule module, int tier) {

        return new NamespacedKey(module.getPlugin(), "ammunition.explosive.%s".formatted(tier));
    }

    public static NamespacedKey weapon(ILeafModule module) {

        return new NamespacedKey(module.getPlugin(), "weapon");
    }

    public static NamespacedKey licence(ILeafModule module) {

        return new NamespacedKey(module.getPlugin(), "licence");
    }

}
