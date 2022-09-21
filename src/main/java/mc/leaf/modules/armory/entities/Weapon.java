package mc.leaf.modules.armory.entities;

import mc.leaf.core.api.persistence.Persist;
import mc.leaf.core.api.persistence.Persistable;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.ArmoryKeys;
import mc.leaf.modules.armory.ArmoryUtils;
import mc.leaf.modules.armory.entities.weapons.RifleWeapon;
import mc.leaf.modules.armory.entities.weapons.RocketLauncherWeapon;
import mc.leaf.modules.armory.entities.weapons.SpecialRocketLauncherWeapon;
import mc.leaf.modules.armory.enums.WeaponType;
import mc.leaf.modules.armory.generics.ShotTarget;
import mc.leaf.modules.armory.generics.WeaponWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Weapon extends Persistable {

    @Persist(key = "tier")
    private Integer    tier;
    @Persist(key = "reach")
    private Integer    reach;
    @Persist(key = "ammo")
    private Integer    ammo;
    @Persist(key = "maxAmmo")
    private Integer    maxAmmo;
    @Persist(key = "type")
    private WeaponType type;
    @Persist(key = "power")
    private Integer    power;
    @Persist(key = "trailColor")
    private Color      trailColor;
    @Persist(key = "timeShot")
    private Integer    timeShot;
    @Persist(key = "timeReload")
    private Integer    timeReload;

    public Weapon(ILeafModule module) {

        super(module);
    }

    /**
     * Try to read a {@link Weapon} entity from the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with a {@link Weapon} entity.
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link Weapon} entity will be read.
     *
     * @return An {@link Optional} {@link Weapon}, if found.
     */
    public static Optional<Weapon> from(ILeafModule module, PersistentDataHolder holder) {

        return Persistable.from(Weapon.class, module, holder, ArmoryKeys.weapon(module), Weapon::new);
    }

    /**
     * Try to write a {@link Weapon} entity to the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with the provided {@link Weapon} entity.
     * @param holder
     *         The {@link PersistentDataHolder} into which the {@link Weapon} entity will be written.
     * @param tag
     *         The {@link Weapon} to write.
     */
    public static void persist(ILeafModule module, PersistentDataHolder holder, Weapon tag) {

        Persistable.persist(Weapon.class, module, holder, tag);
    }

    /**
     * Try to remove a {@link Weapon} entity from the provided {@link PersistentDataHolder}.
     *
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link Weapon} entity will be removed.
     * @param tag
     *         The {@link Weapon} to remove.
     */
    public static void desist(PersistentDataHolder holder, Weapon tag) {

        Persistable.desist(holder, tag);
    }

    public Integer getTier() {

        return this.tier;
    }

    public void setTier(Integer tier) {

        this.tier = tier;
    }

    public Integer getReach() {

        return this.reach;
    }

    public void setReach(Integer reach) {

        this.reach = reach;
    }

    public Integer getAmmo() {

        return this.ammo;
    }

    public void setAmmo(Integer ammo) {

        this.ammo = ammo;
    }

    public Integer getMaxAmmo() {

        return this.maxAmmo;
    }

    public void setMaxAmmo(Integer maxAmmo) {

        this.maxAmmo = maxAmmo;
    }

    public WeaponType getType() {

        return this.type;
    }

    public void setType(WeaponType type) {

        this.type = type;
    }

    public Integer getPower() {

        return this.power;
    }

    public void setPower(Integer power) {

        this.power = power;
    }

    public Color getTrailColor() {

        return this.trailColor;
    }

    public void setTrailColor(Color trailColor) {

        this.trailColor = trailColor;
    }

    public Integer getTimeShot() {

        return this.timeShot;
    }

    public void setTimeShot(Integer timeShot) {

        this.timeShot = timeShot;
    }

    public Integer getTimeReload() {

        return this.timeReload;
    }

    public void setTimeReload(Integer timeReload) {

        this.timeReload = timeReload;
    }

    /**
     * Retrieve the {@link NamespacedKey} in which this {@link Persistable} can be read/written.
     *
     * @return The {@link NamespacedKey} for this {@link Persistable}.
     */
    @Override
    public NamespacedKey getNamespacedKey() {

        return ArmoryKeys.weapon(this.getModule());
    }

    @Override
    public void onPersisted(@NotNull PersistentDataHolder holder) {

        if (!(holder instanceof ItemMeta meta)) {
            return;
        }

        switch (this.getType()) {
            case RIFLE -> meta.displayName(Component.text("Rifle", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
            case ROCKET_LAUNCHER -> meta.displayName(Component.text("Rocket Launcher", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
            case SPECIAL_ROCKET_LAUNCHER -> meta.displayName(Component.text("Rocket Launcher", Style.style(TextColor.fromCSSHexString("#FF0000"), TextDecoration.BOLD)));
        }

        String loreTier = "Tier " + ArmoryUtils.toRoman(this.getTier());
        String loreAmmo = "Ammo " + this.getAmmo() + " / " + this.getMaxAmmo();
        String loreType = "Type " + this.getType();

        meta.lore(Arrays.asList(
                Component.text(loreTier, Style.style(TextColor.fromCSSHexString("#FFAA00"), TextDecoration.BOLD)),
                Component.empty(),
                Component.text(loreAmmo, Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)),
                Component.text(loreType, Style.style(TextColor.fromCSSHexString("#AAAAAA"), TextDecoration.BOLD))
        ));
    }

    @Override
    public boolean onPersisting(@NotNull PersistentDataHolder holder) {

        if (this.getTier() == null) {
            this.setTier(ArmoryUtils.getNaturalWeaponTier(this));
        }

        return true;
    }

    @Override
    public void onDesisted(@NotNull PersistentDataHolder holder) {

        if (holder instanceof ItemMeta meta) {
            meta.lore(null);
            meta.displayName(null);
        }
    }

    public WeaponWrapper getWrapper(Player player) {

        return switch (this.getType()) {
            case RIFLE -> new RifleWeapon(player, this);
            case ROCKET_LAUNCHER -> new RocketLauncherWeapon(player, this);
            case SPECIAL_ROCKET_LAUNCHER -> new SpecialRocketLauncherWeapon(player, this);
        };
    }

    public List<ShotTarget> findTargets(Player player) {

        return Collections.emptyList();
    }

}
