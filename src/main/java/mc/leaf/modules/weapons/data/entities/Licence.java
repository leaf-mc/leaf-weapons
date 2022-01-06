package mc.leaf.modules.weapons.data.entities;

public class Licence {

    private Integer level;

    public Integer getLevel() {

        return level;
    }

    public void setLevel(Integer level) {

        this.level = level;
    }

    public boolean canUseWeapon(Weapon weapon) {

        return weapon.getTier() <= this.getLevel();
    }

}
