package model.game_running;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_building.Configuration;
import model.game_entities.Atom;
import model.game_entities.Shooter;
import model.game_entities.enums.ShieldType;
import model.game_entities.shields.*;

public class ShieldHandler {

    private Shooter shooter;

    private ShieldTuple tempShields;
    private ShieldTuple shields;

    public ShieldHandler() {
    }

    public ShieldHandler(Shooter shooter) {
        this.shooter = shooter;
        shields = new ShieldTuple();
        tempShields = new ShieldTuple();

        shields.setShieldsCount(Configuration.getInstance().getNumOfEtaShields(), ShieldType.ETA);
        shields.setShieldsCount(Configuration.getInstance().getNumOfLotaShields(), ShieldType.LOTA);
        shields.setShieldsCount(Configuration.getInstance().getNumOfThetaShields(), ShieldType.THETA);
        shields.setShieldsCount(Configuration.getInstance().getNumOfZetaShields(), ShieldType.ZETA);
    }

    public void applyShield(ShieldType type) {
        if (shields.getShieldsCount(type) > 0 && shooter.projectileIsAtom()) {
            shields.decreaseShieldCount(type);
            tempShields.addShield(type);
            shooter.updateStatisticsShieldCount();
            if (shooter.projectileIsAtom()) {
                Atom shieldedAtom = applyShield(shooter.getAtomProjectile(), type);
                shooter.setCurrentProjectile(shieldedAtom);
            }
        }
    }

    private Atom applyShield(Atom atom, ShieldType type) {
        switch (type) {
            case ETA:
                return new EtaShield(atom);
            case LOTA:
                return new LotaShield(atom);
            case THETA:
                return new ThetaShield(atom);
            case ZETA:
                return new ZetaShield(atom);
        }
        return atom;
    }

    public void emptyTempShields() {
        tempShields.reset();
    }

    public ShieldTuple getTempShields() {
        return new ShieldTuple(this.tempShields);
    }

    public void setTempShields(ShieldTuple shields) {
        this.tempShields = shields;
    }

    public ShieldTuple getShields() {
        return shields;
    }

    @JsonIgnore
    public String getShieldsCount(ShieldType type) {
        return shields.getShieldsCount(type) + "";
    }

    @JsonIgnore
    public void setShooter(Shooter shooter) {
        this.shooter = shooter;
    }
}




