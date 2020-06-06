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
                int spacing = 0;
                for (String extra : extras) {
                    String[] monsterParts = extra.split("\\|");

                    // I hate this high up placement, but slime boss is a problem with how he splits.
                    AbstractMonster pipedMonster = ReflectionUtils.tryConstructMonster(monsterParts[0], 200 - spacing, 325);
                    pipedMonster.maxHealth = Integer.parseInt(monsterParts[1]);
                    pipedMonster.currentHealth = Integer.parseInt(monsterParts[1]);

                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(pipedMonster, true));

                    spacing += pipedMonster.hb_w + 50;
                }
            }
        }
    }
}
