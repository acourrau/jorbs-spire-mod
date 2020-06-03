package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;

import java.util.ArrayList;
import java.util.List;

import static stsjorbsmod.util.ReflectionUtils.tryConstructMonster;

public class ExtraMonsterPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class ExtraMonsterField {
        public static SpireField<ArrayList<AbstractMonster>> pipedMonsters = new SpireField<>(() -> new ArrayList<AbstractMonster>());
    }

    @SpirePatch(clz = SlimeBoss.class, method = "usePreBattleAction")
    public static class AddExtraMonsterPatch
    {
//        @SpirePostfixPatch
//        public static void patch(SlimeBoss __instance) {
//            ArrayList<AbstractMonster> extras = ExtraMonsterField.pipedMonsters.get(AbstractDungeon.player);
//            if (!extras.isEmpty()) {
//                for (AbstractMonster extra : extras) {
//                    extra.drawX = __instance.drawX - 200.0F * Settings.scale;
//                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(extra, true));
//                }
//            }
//        }

        @SpirePostfixPatch
        public static void patch(SlimeBoss __instance) {
            LouseDefensive d = new LouseDefensive(200.0F, 200.0F);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(tryConstructMonster(d.getClass().getName(), 200.0F, 200.0F), true));
        }
    }
}
