package mc.leaf.modules.weapons.listeners;


import mc.leaf.core.events.LeafListener;
import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.WeaponGenerator;
import mc.leaf.modules.weapons.data.entities.Ammunition;
import mc.leaf.modules.weapons.data.entities.Weapon;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class PlayerLootListener extends LeafListener {

    public static final double                                   WEAPON_DROP_RATE = 0.2;
    public static final double                                   AMMO_DROP_RATE   = 0.4;
    private final       LeafWeaponsModule                        module;
    private final       Map<Integer, List<Supplier<Weapon>>>     weapons          = new HashMap<>();
    private final       Map<Integer, List<Supplier<Ammunition>>> ammunition       = new HashMap<>();

    public PlayerLootListener(LeafWeaponsModule module) {

        this.module = module;

        this.weapons.put(1, Arrays.asList(WeaponGenerator::getTierOneRiffle, WeaponGenerator::getTierOneRocketLauncher));
        this.weapons.put(2, Arrays.asList(WeaponGenerator::getTierTwoRiffle, WeaponGenerator::getTierTwoRocketLauncher));
        this.weapons.put(3, Arrays.asList(WeaponGenerator::getTierThreeRiffle, WeaponGenerator::getTierThreeRocketLauncher));

        this.ammunition.put(1, Arrays.asList(WeaponGenerator::getTierOneRiffleAmmo, WeaponGenerator::getTierOneRocketLauncherAmmo));
        this.ammunition.put(2, Arrays.asList(WeaponGenerator::getTierTwoRiffleAmmo, WeaponGenerator::getTierTwoRocketLauncherAmmo));
        this.ammunition.put(3, Arrays.asList(WeaponGenerator::getTierThreeRiffleAmmo, WeaponGenerator::getTierThreeRocketLauncherAmmo));
    }

    private static boolean between(double random, double rate) {

        return (0.5 - (rate / 2)) < random && random < (0.5 + (rate / 2));
    }

    @Override
    public void onLootGenerate(LootGenerateEvent event) {

        double weaponPull = new Random().nextDouble();

        if (between(weaponPull, WEAPON_DROP_RATE)) {
            List<Supplier<Weapon>> suppliers     = this.weapons.get(this.getRandomTier());
            Supplier<Weapon>       stackSupplier = suppliers.get(new Random().nextInt(suppliers.size()));
            ItemStack              stack         = stackSupplier.get().asItem(this.module);
            stack.setAmount(1);
            event.getLoot().add(stack);
        }

        double ammoPull = new Random().nextDouble();

        if (between(ammoPull, AMMO_DROP_RATE)) {
            List<Supplier<Ammunition>> suppliers     = this.ammunition.get(this.getRandomTier());
            Supplier<Ammunition>       stackSupplier = suppliers.get(new Random().nextInt(suppliers.size()));
            ItemStack                  stack         = stackSupplier.get().asItem(this.module);
            stack.setAmount(new Random().nextInt(8) + 1);
            event.getLoot().add(stack);
        }
    }

    private int getRandomTier() {

        double tierRnd = new Random().nextDouble();

        if (between(tierRnd, 0.10)) {
            return 3;
        } else if (between(tierRnd, 0.50)) {
            return 2;
        } else {
            return 1;
        }
    }

}
