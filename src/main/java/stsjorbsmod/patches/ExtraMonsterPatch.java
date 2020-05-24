package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import javassist.CtBehavior;
import stsjorbsmod.powers.WitheringPower;

public class ExtraMonsterPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class ExtraMonsterField {
        public static SpireField<AbstractMonster> pipedMonster = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = SlimeBoss.class, method = "usePreBattleAction")
    public static class AddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch(SlimeBoss __instance) {
            AbstractMonster extra = ExtraMonsterField.pipedMonster.get(AbstractDungeon.player);
            if (extra != null)
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(extra, false));
        }
    }
}
