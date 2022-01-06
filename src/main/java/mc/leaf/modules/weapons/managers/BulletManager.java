package mc.leaf.modules.weapons.managers;

import mc.leaf.core.async.LeafManager;
import mc.leaf.core.interfaces.ILeafModule;
import mc.leaf.modules.weapons.entities.BulletEntity;

import java.util.ArrayList;
import java.util.List;

public class BulletManager extends LeafManager<BulletEntity> {

    public BulletManager(ILeafModule module) {

        super(module);
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

        List<BulletEntity> bullets = new ArrayList<>();
        while (!this.getHolder().isEmpty()) {
            BulletEntity entity = this.getHolder().poll();
            if (entity != null) {
                entity.tick();

                if (entity.isValid()) {
                    bullets.add(entity);
                }
            }
        }
        bullets.forEach(this::offer);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

}
