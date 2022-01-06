package mc.leaf.modules.weapons.data.tags;

import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.weapons.data.PersistentData;
import mc.leaf.modules.weapons.data.entities.Weapon;
import org.bukkit.Color;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


public class WeaponTag extends PersistentData<Weapon> {

    public WeaponTag(ILeafModule module) {

        super(module);
    }

    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @NotNull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {

        return PersistentDataContainer.class;
    }

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @NotNull
    @Override
    public Class<Weapon> getComplexType() {

        return Weapon.class;
    }

    /**
     * Returns the primitive data that resembles the complex object passed to this method.
     *
     * @param complex
     *         the complex object instance
     * @param context
     *         the context this operation is running in
     *
     * @return the primitive value
     */
    @NotNull
    @Override
    public PersistentDataContainer toPrimitive(@NotNull Weapon complex, @NotNull PersistentDataAdapterContext context) {

        PersistentDataContainer container = context.newPersistentDataContainer();

        container.set(this.getKey("tier"), PersistentDataType.INTEGER, complex.getTier());
        container.set(this.getKey("reach"), PersistentDataType.INTEGER, complex.getReach());
        container.set(this.getKey("ammo"), PersistentDataType.INTEGER, complex.getAmmo());
        container.set(this.getKey("maxAmmo"), PersistentDataType.INTEGER, complex.getMaxAmmo());
        container.set(this.getKey("type"), PersistentDataType.STRING, complex.getBulletType());
        container.set(this.getKey("power"), PersistentDataType.INTEGER, complex.getBulletPower());
        container.set(this.getKey("color"), PersistentDataType.INTEGER, complex.getBulletTrailColor().asRGB());
        container.set(this.getKey("sounds"), PersistentDataType.STRING, complex.getSoundCategory());
        container.set(this.getKey("nextShot"), PersistentDataType.LONG, complex.getNextShot());
        container.set(this.getKey("timeShot"), PersistentDataType.LONG, complex.getTimeShot());
        container.set(this.getKey("timeReload"), PersistentDataType.LONG, complex.getTimeReload());

        return container;
    }

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive
     *         the primitive value
     * @param context
     *         the context this operation is running in
     *
     * @return the complex object instance
     */
    @NotNull
    @Override
    public Weapon fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {

        Weapon weapon = new Weapon();

        weapon.setTier(primitive.get(this.getKey("tier"), PersistentDataType.INTEGER));
        weapon.setReach(primitive.get(this.getKey("reach"), PersistentDataType.INTEGER));
        weapon.setAmmo(primitive.get(this.getKey("ammo"), PersistentDataType.INTEGER));
        weapon.setMaxAmmo(primitive.get(this.getKey("maxAmmo"), PersistentDataType.INTEGER));
        weapon.setBulletType(primitive.get(this.getKey("type"), PersistentDataType.STRING));
        weapon.setBulletPower(primitive.get(this.getKey("power"), PersistentDataType.INTEGER));
        weapon.setBulletTrailColor(Color.fromRGB(primitive.get(this.getKey("color"), PersistentDataType.INTEGER)));
        weapon.setSoundCategory(primitive.get(this.getKey("sounds"), PersistentDataType.STRING));
        weapon.setNextShot(primitive.get(this.getKey("nextShot"), PersistentDataType.LONG));
        weapon.setTimeShot(primitive.get(this.getKey("timeShot"), PersistentDataType.LONG));
        weapon.setTimeReload(primitive.get(this.getKey("timeReload"), PersistentDataType.LONG));

        return weapon;
    }

}
