package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.ExtraMonsterPatch;
import stsjorbsmod.patches.SelfExertField;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class PiedPiper extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PiedPiper.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public PiedPiper() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        SelfExertField.selfExert.set(this, true);
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ExtraMonsterPatch.ExtraMonsterField.pipedMonster.set(p, m);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeBaseCost(UPGRADED_COST);
            upgradeName();
            upgradeDescription();
        }
    }
}
