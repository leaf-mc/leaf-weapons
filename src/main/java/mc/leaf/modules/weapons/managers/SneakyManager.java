package mc.leaf.modules.weapons.managers;

import mc.leaf.core.async.LeafManager;
import mc.leaf.modules.weapons.LeafWeaponsModule;
import mc.leaf.modules.weapons.data.entities.Licence;
import mc.leaf.modules.weapons.data.entities.Weapon;
import mc.leaf.modules.weapons.data.tags.LicenceTag;
import mc.leaf.modules.weapons.data.tags.WeaponTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SneakyManager extends LeafManager<Player> {

    private final LeafWeaponsModule module;
    private final PotionEffect      effect;

    public SneakyManager(LeafWeaponsModule module) {

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

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ItemStack stack    = onlinePlayer.getInventory().getItemInMainHand();
            ItemMeta  metadata = stack.getItemMeta();

            if (metadata != null) {
                PersistentDataContainer container = metadata.getPersistentDataContainer();
                Weapon                  weapon    = container.get(this.module.getWeaponNamespace(), new WeaponTag(this.module));
                Licence licence = onlinePlayer.getPersistentDataContainer()
                        .get(this.module.getLicenceNamespace(), new LicenceTag(this.module));

                if (weapon != null && licence != null && licence.getLevel() >= weapon.getTier() && weapon
                        .getBulletType().equals("normal")) {
                    if (onlinePlayer.isSneaking()) {
                        onlinePlayer.addPotionEffect(this.effect);
                    } else {
                        onlinePlayer.removePotionEffect(this.effect.getType());
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
