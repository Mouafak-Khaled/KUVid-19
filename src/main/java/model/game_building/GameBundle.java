package model.game_building;

import model.game_entities.*;
import model.game_running.ProjectileContainer;
import model.game_running.ShieldHandler;
import model.game_space.Player;

import java.util.ArrayList;

public class GameBundle {

    private ArrayList<Atom> atoms;
    private ArrayList<Blocker> blockers;
    private ArrayList<Powerup> powerUps;
    private ArrayList<Molecule> molecules;
    private ShieldHandler shieldHandler;
    private Shooter shooter;
    private ConfigBundle configBundle;
    private Player player;
    private ProjectileContainer projectileContainer;

    // returning cloned lists in the below getters to avoid exposing private fields.
    public ArrayList<Atom> getAtoms() {
        return new ArrayList<>(atoms);
    }

    public ArrayList<Blocker> getBlockers() {
        return new ArrayList<>(blockers);
    }

    public ArrayList<Powerup> getPowerUps() {
        return new ArrayList<>(powerUps);
    }

    public ArrayList<Molecule> getMolecules() {
        return new ArrayList<>(molecules);
    }

    public Shooter getShooter() {
        return shooter;
    }

    public Player getPlayer() {
        return player;
    }

    public ConfigBundle getConfigBundle() {
        return configBundle;
    }

    public ProjectileContainer getProjectileContainer() {
        return projectileContainer;
    }

    public ShieldHandler getShieldHandler() {
        return shieldHandler;
    }

    private GameBundle() { //private because we only want to make game bundles through building.
    }


    public static class Builder {
        private final ArrayList<Atom> atoms;
        private final ArrayList<Blocker> blockers;
        private final ArrayList<Powerup> powerUps;
        private final ArrayList<Molecule> molecules;
        private Shooter shooter;
        private ProjectileContainer projectileContainer;
        private Player player;
        private ConfigBundle configBundle;
        private ShieldHandler shieldHandler;

        public Builder() {
            atoms = new ArrayList<>();
            blockers = new ArrayList<>();
            powerUps = new ArrayList<>();
            molecules = new ArrayList<>();
        }

        public Builder addEntity(Atom atom) {
            atoms.add(atom);
            return this;
        }

        public Builder addEntity(Powerup atom) {
            powerUps.add(atom);
            return this;
        }

        public Builder addEntity(Molecule atom) {
            molecules.add(atom);
            return this;
        }

        public Builder addEntity(Blocker atom) {
            blockers.add(atom);
            return this;
        }

        public Builder setShooter(Shooter shooter) {
            this.shooter = shooter;
            return this;
        }

        public Builder setProjectileContainer(ProjectileContainer projectileContainer) {
            this.projectileContainer = projectileContainer;
            return this;
        }

        public Builder setPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder setConfigBundle(ConfigBundle configBundle) {
            this.configBundle = configBundle;
            return this;
        }

        public Builder setShieldHandler(ShieldHandler shieldHandler) {
            this.shieldHandler = shieldHandler;
            return this;
        }


        public GameBundle build() {
            GameBundle bundle = new GameBundle();
            bundle.atoms = this.atoms;
            bundle.molecules = this.molecules;
            bundle.blockers = this.blockers;
            bundle.powerUps = this.powerUps;
            bundle.shooter = this.shooter;
            bundle.configBundle = this.configBundle;
            bundle.player = this.player;
            bundle.projectileContainer = this.projectileContainer;
            bundle.shieldHandler = this.shieldHandler;
            return bundle;
        }
    }

}
