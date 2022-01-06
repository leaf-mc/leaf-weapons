package mc.leaf.modules.weapons.event.licences;

import mc.leaf.modules.weapons.data.entities.Licence;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class LicenceUpdatedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final        Licence     oldLicence;
    private final        Licence     newLicence;
    private              boolean     cancelled;

    public LicenceUpdatedEvent(Player player, Licence oldLicence, Licence newLicence) {

        super(player);
        this.oldLicence = oldLicence;
        this.newLicence = newLicence;
        this.cancelled  = false;
    }

    public static HandlerList getHandlerList() {

        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not be executed in the server, but will still
     * pass to other plugins.
     *
     * @param cancel
     *         true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {

        this.cancelled = cancel;
    }

    public Licence getNewLicence() {

        return newLicence;
    }

    public Licence getOldLicence() {

        return oldLicence;
    }

}
