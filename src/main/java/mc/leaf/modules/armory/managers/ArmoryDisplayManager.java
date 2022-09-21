package mc.leaf.modules.armory.managers;

import mc.leaf.core.async.LeafManager;
import mc.leaf.core.utils.MinecraftColors;
import mc.leaf.modules.armory.ArmoryModule;
import mc.leaf.modules.armory.entities.License;
import mc.leaf.modules.armory.entities.Weapon;
import mc.leaf.modules.armory.enums.WeaponType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ArmoryDisplayManager extends LeafManager<Player> {

    private final ArmoryModule       module;
    private final PotionEffect       effect;
    private final Map<UUID, Boolean> hadDisplay = new HashMap<>();

    public ArmoryDisplayManager(ArmoryModule module) {

        super(module);
        this.module = module;
        this.effect = new PotionEffect(PotionEffectType.SLOW, 2, 4, true);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            ItemStack stack = player.getInventory().getItemInMainHand();
            ItemMeta  meta  = stack.getItemMeta();

            if (meta == null) {
                if (this.hadDisplay.getOrDefault(player.getUniqueId(), false)) {
                    player.sendActionBar(Component.empty());
                }
                this.hadDisplay.put(player.getUniqueId(), false);
                return;
            }

            Optional<Weapon>  optionalWeapon  = Weapon.from(this.module, meta);
            Optional<License> optionalLicence = License.from(this.module, player);

            if (optionalWeapon.isEmpty() || optionalLicence.isEmpty()) {
                if (this.hadDisplay.getOrDefault(player.getUniqueId(), false)) {
                    player.sendActionBar(Component.empty());
                }
                this.hadDisplay.put(player.getUniqueId(), false);
                return;
            }

            Weapon  weapon  = optionalWeapon.get();
            License license = optionalLicence.get();

            TextComponent weaponData;
            if (player.hasCooldown(stack.getType())) {
                weaponData = Component.text(String.format("%s / %s", weapon.getAmmo(), weapon.getMaxAmmo()))
                                      .color(MinecraftColors.RED);
            } else {
                weaponData = Component.text(String.format("%s / %s", weapon.getAmmo(), weapon.getMaxAmmo()));
            }

            if (player.isSneaking()) {
                if (license.getLevel() >= weapon.getTier() && weapon.getType() == WeaponType.RIFLE) {
                    player.addPotionEffect(this.effect);
                }

                int    reach  = Math.min(weapon.getReach(), 100);
                Entity entity = player.getTargetEntity(reach);

                if (entity != null) {
                    double        distance     = player.getLocation().distance(entity.getLocation());
                    TextComponent distanceData = Component.text(String.format("Distance: %.2f", distance));

                    player.sendActionBar(Component.empty()
                                                  .append(weaponData)
                                                  .append(Component.text(" | "))
                                                  .append(distanceData)
                    );
                    this.hadDisplay.put(player.getUniqueId(), true);
                    continue;
                }
            }
            player.sendActionBar(weaponData);
            this.hadDisplay.put(player.getUniqueId(), true);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
