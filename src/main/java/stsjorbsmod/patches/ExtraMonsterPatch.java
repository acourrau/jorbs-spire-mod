package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import stsjorbsmod.util.MonsterUtils;
import stsjorbsmod.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static stsjorbsmod.util.ReflectionUtils.tryConstructMonster;

public class ExtraMonsterPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class ExtraMonsterField {
        public static SpireField<String> pipedMonsters = new SpireField<>(() -> "");

        public static void addPipedMonster(AbstractMonster m) {
            String existingMonsters = pipedMonsters.get(AbstractDungeon.player);

            if (!existingMonsters.isEmpty())
                existingMonsters += ",";
            existingMonsters += MonsterUtils.serializeMonsterForSaving(m);

            pipedMonsters.set(AbstractDungeon.player, existingMonsters);
        }
    }

    @SpirePatch(clz = SlimeBoss.class, method = "usePreBattleAction")
    public static class AddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch(SlimeBoss __instance) {
            List<String> extras = Arrays.asList(ExtraMonsterField.pipedMonsters.get(AbstractDungeon.player).split(","));
            if (!extras.isEmpty()) {
                for (String extra : extras) {
                    String[] monsterParts = extra.split("\\|");

                    // need special math here to place monsters correctly
                    AbstractMonster pipedMonster = ReflectionUtils.tryConstructMonster(monsterParts[0], 200, 0);
                    pipedMonster.maxHealth = Integer.parseInt(monsterParts[1]);

                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(pipedMonster, true));
                }
            }
        }
    }
}
