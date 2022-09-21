package mc.leaf.modules.armory.listeners;

import mc.leaf.core.events.LeafListener;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.ArmoryGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class ArmoryWeaponLootListener extends LeafListener {

    public static final double                                  WEAPON_DROP_RATE = 0.2;
    public static final double                                  AMMO_DROP_RATE   = 0.4;
    private final       Map<Integer, List<Supplier<ItemStack>>> weapons          = new HashMap<>();
    private final       Map<Integer, List<Supplier<ItemStack>>> ammunition       = new HashMap<>();

    public ArmoryWeaponLootListener(ILeafModule module) {

        this.weapons.put(1, Arrays.asList(() -> ArmoryGenerator.getTierOneRiffle(module), () -> ArmoryGenerator.getTierOneRocketLauncher(module)));
        this.weapons.put(2, Arrays.asList(() -> ArmoryGenerator.getTierTwoRiffle(module), () -> ArmoryGenerator.getTierTwoRocketLauncher(module)));
        this.weapons.put(3, Arrays.asList(() -> ArmoryGenerator.getTierThreeRiffle(module), () -> ArmoryGenerator.getTierThreeRocketLauncher(module)));

        this.ammunition.put(1, Arrays.asList(() -> ArmoryGenerator.getRiffleAmmo(module, 1), () -> ArmoryGenerator.getRocketLauncherAmmo(module, 1)));
        this.ammunition.put(2, Arrays.asList(() -> ArmoryGenerator.getRiffleAmmo(module, 2), () -> ArmoryGenerator.getRocketLauncherAmmo(module, 2)));
        this.ammunition.put(3, Arrays.asList(() -> ArmoryGenerator.getRiffleAmmo(module, 3), () -> ArmoryGenerator.getRocketLauncherAmmo(module, 3)));
    }

    private static boolean between(double random, double rate) {

        return (0.5 - (rate / 2)) < random && random < (0.5 + (rate / 2));
    }

    @Override
    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {

        double weaponPull = new Random().nextDouble();

        if (between(weaponPull, WEAPON_DROP_RATE)) {
            List<Supplier<ItemStack>> suppliers     = this.weapons.get(this.getRandomTier());
            Supplier<ItemStack>       stackSupplier = suppliers.get(new Random().nextInt(suppliers.size()));
            ItemStack                 stack         = stackSupplier.get();
            stack.setAmount(1);
            event.getLoot().add(stack);
        }

        double ammoPull = new Random().nextDouble();

        if (between(ammoPull, AMMO_DROP_RATE)) {
            List<Supplier<ItemStack>> suppliers     = this.ammunition.get(this.getRandomTier());
            Supplier<ItemStack>       stackSupplier = suppliers.get(new Random().nextInt(suppliers.size()));
            ItemStack                 stack         = stackSupplier.get();
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
