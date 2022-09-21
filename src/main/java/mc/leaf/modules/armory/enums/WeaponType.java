package mc.leaf.modules.armory.enums;

import java.util.Arrays;
import java.util.List;

public enum WeaponType {

    RIFLE(AmmoType.RIFLE), ROCKET_LAUNCHER(AmmoType.EXPLOSIVE), SPECIAL_ROCKET_LAUNCHER(AmmoType.EXPLOSIVE);

    private final AmmoType ammoType;

    WeaponType(AmmoType ammoType) {

        this.ammoType = ammoType;
    }

    public static List<String> autocomplete() {

        return Arrays.stream(WeaponType.values()).map(WeaponType::name).map(String::toLowerCase).toList();
    }

    public static WeaponType of(String str) {

        return WeaponType.valueOf(str.toUpperCase());
    }

    public AmmoType getAmmoType() {

        return this.ammoType;
    }
}
