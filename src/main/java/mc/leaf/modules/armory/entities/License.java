package mc.leaf.modules.armory.entities;

import mc.leaf.core.api.persistence.Persist;
import mc.leaf.core.api.persistence.Persistable;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.armory.ArmoryKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.Optional;

public class License extends Persistable {

    @Persist(key = "level")
    private Integer level;

    public License(ILeafModule module) {

        super(module);
    }

    /**
     * Try to read a {@link License} entity from the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with a {@link License} entity.
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link License} entity will be read.
     *
     * @return An {@link Optional} {@link License}, if found.
     */
    public static Optional<License> from(ILeafModule module, PersistentDataHolder holder) {

        return Persistable.from(License.class, module, holder, ArmoryKeys.licence(module), License::new);
    }

    /**
     * Try to write a {@link License} entity to the provided {@link PersistentDataHolder}.
     *
     * @param module
     *         The {@link ILeafModule} associated with the provided {@link License} entity.
     * @param holder
     *         The {@link PersistentDataHolder} into which the {@link License} entity will be written.
     * @param tag
     *         The {@link License} to write.
     */
    public static void persist(ILeafModule module, PersistentDataHolder holder, License tag) {

        Persistable.persist(License.class, module, holder, tag);
    }

    /**
     * Try to remove a {@link License} entity from the provided {@link PersistentDataHolder}.
     *
     * @param holder
     *         The {@link PersistentDataHolder} from which the {@link License} entity will be removed.
     * @param tag
     *         The {@link License} to remove.
     */
    public static void desist(PersistentDataHolder holder, License tag) {

        Persistable.desist(holder, tag);
    }

    public Integer getLevel() {

        return this.level;
    }

    public void setLevel(Integer level) {

        this.level = level;
    }

    public boolean canUseWeapon(Weapon weapon) {

        return weapon.getTier() <= this.getLevel();
    }

    /**
     * Retrieve the {@link NamespacedKey} in which this {@link Persistable} can be read/written.
     *
     * @return The {@link NamespacedKey} for this {@link Persistable}.
     */
    @Override
    public NamespacedKey getNamespacedKey() {

        return ArmoryKeys.licence(this.getModule());
    }

}
