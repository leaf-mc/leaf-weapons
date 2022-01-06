package mc.leaf.modules.weapons.data.tags;

import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.weapons.data.PersistentData;
import mc.leaf.modules.weapons.data.entities.Ammunition;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class AmmunitionTag extends PersistentData<Ammunition> {

    public AmmunitionTag(ILeafModule module) {

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
    public Class<Ammunition> getComplexType() {

        return Ammunition.class;
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
    public PersistentDataContainer toPrimitive(@NotNull Ammunition complex, @NotNull PersistentDataAdapterContext context) {

        PersistentDataContainer container = context.newPersistentDataContainer();

        container.set(this.getKey("type"), PersistentDataType.STRING, complex.getType());
        container.set(this.getKey("tier"), PersistentDataType.INTEGER, complex.getTier());

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
    public Ammunition fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {

        Ammunition ammunition = new Ammunition();

        ammunition.setTier(primitive.get(this.getKey("tier"), PersistentDataType.INTEGER));
        ammunition.setType(primitive.get(this.getKey("type"), PersistentDataType.STRING));

        return ammunition;
    }

}
