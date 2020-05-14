package stsjorbsmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.characters.ShriekingHatSaveData;

public class ShriekingHatPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShriekingHatPower.class);
    public static final String POWER_ID = STATIC.ID;

    public ShriekingHatPower(final AbstractCreature owner) {
        super(STATIC);

        this.isTurnBased = false;
        this.owner = owner;

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        ShriekingHatSaveData.damageTaken += damageAmount;
        updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], ShriekingHatSaveData.damageTaken);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShriekingHatPower(owner);
    }
}
