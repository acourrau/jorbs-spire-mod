package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class ShriekingHatPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShriekingHatPower.class);
    public static final String POWER_ID = STATIC.ID;

    public static int damageTaken = 0;

    public ShriekingHatPower(final AbstractCreature owner) {
        super(STATIC);

        this.isTurnBased = false;
        this.owner = owner;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], damageTaken);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShriekingHatPower(owner);
    }
}
