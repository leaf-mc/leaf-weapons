package mc.leaf.modules.armory.entities;

import mc.leaf.core.api.persistence.Persist;
import mc.leaf.core.api.persistence.Persistable;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.ArmoryKeys;
import mc.leaf.modules.armory.ArmoryUtils;
import mc.leaf.modules.armory.enums.AmmoType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class Ammunition extends Persistable {

    @Persist(key = "tier")
    private Integer  tier;
    @Persist(key = "type")
    private AmmoType type;

    public Ammunition(ILeafModule module) {

        super(module);
    }

    /**
     * Try to read a {@link Ammunition} entity from the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with a {@link Ammunition} entity.
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link Ammunition} entity will be read.
     *
     * @return An {@link Optional} {@link Ammunition}, if found.
     */
    public static Optional<Ammunition> from(ILeafModule module, PersistentDataHolder holder) {

        if (holder == null) {
            return Optional.empty();
        }

        return Persistable.from(Ammunition.class, module, holder, ArmoryKeys.ammunition(module), Ammunition::new);
    }

    /**
     * Try to write a {@link Ammunition} entity to the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with the provided {@link Ammunition} entity.
     * @param holder
     *         The {@link PersistentDataHolder} into which the {@link Ammunition} entity will be written.
     * @param tag
     *         The {@link Ammunition} to write.
     */
    public static void persist(ILeafModule module, PersistentDataHolder holder, Ammunition tag) {

        Persistable.persist(Ammunition.class, module, holder, tag);
    }

    /**
     * Try to remove a {@link Ammunition} entity from the provided {@link PersistentDataHolder}.
     *
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link Ammunition} entity will be removed.
     * @param tag
     *         The {@link Ammunition} to remove.
     */
    public static void desist(PersistentDataHolder holder, Ammunition tag) {

        Persistable.desist(holder, tag);
    }

    public Integer getTier() {

        return this.tier;
    }

    public void setTier(Integer tier) {

        this.tier = tier;
    }

    public AmmoType getType() {

        return this.type;
    }

    public void setType(AmmoType type) {

        this.type = type;
    }

    public boolean isCompatible(Weapon weapon) {

        return weapon.getType().getAmmoType() == this.getType() && this.getTier().equals(weapon.getTier());
    }

    /**
     * Retrieve the {@link NamespacedKey} in which this {@link Persistable} can be read/written.
     *
     * @return The {@link NamespacedKey} for this {@link Persistable}.
     */
    @Override
    public NamespacedKey getNamespacedKey() {

        return ArmoryKeys.ammunition(this.getModule());
    }

    @Override
    public void onPersisted(@NotNull PersistentDataHolder holder) {

        if (holder instanceof ItemMeta meta) {
            String loreTier = "Tier " + ArmoryUtils.toRoman(this.getTier());
            String loreType = "Type " + this.getType();

            meta.lore(Arrays.asList(
                    Component.text(loreTier, Style.style(TextColor.fromCSSHexString("#FFAA00"), TextDecoration.BOLD)),
                    Component.empty(),
                    Component.text(loreType, Style.style(TextColor.fromCSSHexString("#AAAAAA"), TextDecoration.BOLD))
            ));

            meta.displayName(Component.text("Munition", Style.style(TextColor.fromCSSHexString("#55FF55"), TextDecoration.BOLD)));
        }
    }

    @Override
    public void onDesisted(@NotNull PersistentDataHolder holder) {

        if (holder instanceof ItemMeta meta) {
            meta.lore(null);
            meta.displayName(null);
        }
    }

}
