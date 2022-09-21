package mc.leaf.modules.armory.enums;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum AmmoType {

    RIFLE(Material.IRON_NUGGET), EXPLOSIVE(Material.GOLD_NUGGET);

    private final Material defaultMaterial;

    AmmoType(Material defaultMaterial) {

        this.defaultMaterial = defaultMaterial;
    }

    public static List<String> autocomplete() {

        return Arrays.stream(AmmoType.values()).map(AmmoType::name).map(String::toLowerCase).toList();
    }

    public static AmmoType of(String str) {

        return AmmoType.valueOf(str.toUpperCase());
    }

    public Material getDefaultMaterial() {

        return this.defaultMaterial;
    }

}
