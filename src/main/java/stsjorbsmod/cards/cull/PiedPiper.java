package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.ExtraMonsterPatch;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.util.MonsterUtils;
import stsjorbsmod.util.ReflectionUtils;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class PiedPiper extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PiedPiper.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    private static String TEXT = "Powerful monsters are not so easily tricked.";

    public PiedPiper() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        SelfExertField.selfExert.set(this, true);
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, TEXT, true));
        }
        else {
            ExtraMonsterPatch.ExtraMonsterField.addPipedMonster(m);
            m.escape();
        }
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
