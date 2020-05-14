package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.ShriekingHatPower;

public class ShriekingHat extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ShriekingHat.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = COST_UNPLAYABLE;

    private boolean sumDamage = false;

    public ShriekingHat() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
    }

    @Override
    public void triggerWhenDrawn() {
        if (!sumDamage) {
            AbstractPlayer p = AbstractDungeon.player;
            addToTop(new ApplyPowerAction(p, p, new ShriekingHatPower(p), this.magicNumber));
            sumDamage = true;
        }
    }

    @Override
    public void onRetained() {
        if (!sumDamage) {
            AbstractPlayer p = AbstractDungeon.player;
            addToTop(new ApplyPowerAction(p, p, new ShriekingHatPower(p), this.magicNumber));
            sumDamage = true;
        }
    }

    private void stopSummingDamage() {
        if (sumDamage) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new RemoveSpecificPowerAction(p, p, ShriekingHatPower.POWER_ID));
            sumDamage = false;
        }
    }

    @Override
    public void triggerOnExhaust() {
        stopSummingDamage();
    }

    @Override
    public void triggerOnManualDiscard() {
        stopSummingDamage();
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }
}
