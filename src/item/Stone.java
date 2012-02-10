package item;

import jpkmn.Driver;
import pokemon.Pokemon;

public class Stone extends Item {

    public enum Type {
        FIRE, WATER, THUNDER, MOON, LEAF;

        private String formatName() {
            return name().charAt(0)
                    + name().substring(1, name().length() - 1).toLowerCase()
                    + "stone";
        }
    }

    public Type type;

    public Stone(int power, int quantity, Type type) {
        super(power, quantity, type.formatName());
        this.type = type;
    }

    @Override
    public boolean effect(Pokemon p) {
        if (!reduce()) {
            Driver.log(Stone.class, "Not enough to use type " + getName());
            return false;
        }

        // @preformat
        if (this.type == Type.FIRE) {
            switch (p.number) {
            case 133:
            case 134:
            case 135:
            case 37:
            case 58:
                return report(p, true);
            default:
                return report(p, false);
            }
        } else if (this.type == Type.WATER) {
            switch (p.number) {
            case 60:
            case 90:
            case 120:
            case 133:
            case 135:
            case 136:
                return report(p, true);
            default:
                return report(p, false);
            }
        } else if (this.type == Type.LEAF) {
            switch (p.number) {
            case 102:
            case 70:
            case 44:
                return report(p, true);
            default:
                return report(p, false);
            }
        } else if (this.type == Type.THUNDER) {
            switch (p.number) {
            case 133:
            case 134:
            case 136:
            case 25:
                return report(p, true);
            default:
                return report(p, false);
            }
        } else if (this.type == Type.MOON) {
            switch (p.number) {
            case 33:
            case 30:
            case 35:
            case 39:
                return report(p, true);
            default:
                return report(p, false);
            }
        }
        // @format

        return false;
    }

    /**
     * Much of this is copied from Pokemon.evolve
     * 
     * @param p
     *            Pokemon
     * @param b
     *            Whether or not stone is effective with selected Pokemon
     * @return
     */
    private boolean report(Pokemon p, boolean b) {
        if (b) {
            Driver.log(Stone.class, getName() + " is effectively used on : " + p.name);

            if (p.number < 133 || p.number > 136) {
                b = p.changeSpecies();
            }

            // All evolutions of Eevee have their stats increased specially
            else if (this.type == Type.FIRE) b = p.changeSpecies(136);
            else if (this.type == Type.THUNDER) b = p.changeSpecies(135);
            else if (this.type == Type.WATER) b = p.changeSpecies(134);
        } 
        else {
                gui.Tools.notify(p, "ERROR", getName() + " does not work on :" + p.name);
                Driver.log(Stone.class, getName() + " does not work on : " + p.name);
        }
        return b;
    }
}