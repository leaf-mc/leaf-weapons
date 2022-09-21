package mc.leaf.modules.armory;

import mc.leaf.modules.armory.entities.Weapon;
import org.bukkit.Color;

import java.util.TreeMap;

public class ArmoryUtils {

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    private ArmoryUtils() {}

    public static String toRoman(int number) {

        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static Integer getNaturalWeaponTier(Weapon weapon) {

        float maxAmmo = weapon.getMaxAmmo();
        float power   = weapon.getPower();

        float mode = switch (weapon.getType()) {
            case RIFLE -> 1;
            case ROCKET_LAUNCHER -> 3;
            case SPECIAL_ROCKET_LAUNCHER -> 5;
        };

        return Math.round(maxAmmo * (power / 100f) * mode);
    }

    private static String padHex(String hex) {

        if (hex.length() == 1) {
            return "0" + hex;
        }
        return hex;
    }

    public static String asHex(Color color) {

        return String.format(
                "#%s%s%s",
                padHex(Integer.toHexString(color.getRed()).toUpperCase()),
                padHex(Integer.toHexString(color.getGreen()).toUpperCase()),
                padHex(Integer.toHexString(color.getBlue()).toUpperCase())
        );
    }

}
