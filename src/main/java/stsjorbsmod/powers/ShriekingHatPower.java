package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import stsjorbsmod.characters.ShriekingHatSaveData;
import stsjorbsmod.util.CardMetaUtils;

public class ShriekingHatPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShriekingHatPower.class);
    public static final String POWER_ID = STATIC.ID;

    private AbstractCard card;

    public ShriekingHatPower(final AbstractCreature owner, AbstractCard card) {
        super(STATIC);

        this.isTurnBased = false;
        this.card = card;
        this.owner = owner;

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        ShriekingHatSaveData.damageTaken += damageAmount;
        card.baseMagicNumber = card.magicNumber = ShriekingHatSaveData.damageTaken;
        card.initializeDescription();

        if (damageAmount >= owner.currentHealth) {
            info.output = damageAmount = 0;
            addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
            addToBot(new VFXAction(owner, new ShockWaveEffect(owner.hb.cX, owner.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5F));
            addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(ShriekingHatSaveData.damageTaken, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING));
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            CardMetaUtils.destroyCardPermanently(card);
        }

        updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], ShriekingHatSaveData.damageTaken);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShriekingHatPower(owner, card);
    }
}
