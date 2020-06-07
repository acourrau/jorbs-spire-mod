package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
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
    public static class SlimeBossAddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch(SlimeBoss __instance) {
            spawnMonsters(200, 325);
        }
    }

    @SpirePatch(clz = SlimeBoss.class, method = "die")
    public static class SlimeBossScareExtraMonstersPatch
    {
        @SpirePostfixPatch
        public static void patch(SlimeBoss __instance) {
            scarePipedMinions();
        }
    }

    @SpirePatch(clz = Hexaghost.class, method = "usePreBattleAction")
    public static class HexaghostAddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch(Hexaghost __instance) {
            spawnMonsters(-300, 50);
        }
    }

    @SpirePatch(clz = Hexaghost.class, method = "die")
    public static class HexaghostScareExtraMonstersPatch
    {
        @SpirePostfixPatch
        public static void patch(Hexaghost __instance) {
            scarePipedMinions();
        }
    }

    @SpirePatch(clz = TheGuardian.class, method = "usePreBattleAction")
    public static class TheGuardianAddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch(TheGuardian __instance) {
            spawnMonsters(-300, 50);
        }
    }

    @SpirePatch(clz = TheGuardian.class, method = "die")
    public static class TheGuardianScareExtraMonstersPatch
    {
        @SpirePostfixPatch
        public static void patch(TheGuardian __instance) {
            scarePipedMinions();
        }
    }

    private static void spawnMonsters(float startingOffsetX, float startingOffsetY) {
        List<String> extras = Arrays.asList(ExtraMonsterField.pipedMonsters.get(AbstractDungeon.player).split(","));
        if (!extras.isEmpty()) {
            int spacing = 0;
            for (String extra : extras) {
                String[] monsterParts = extra.split("\\|");
                String className = monsterParts[0];
                int maxHp = Integer.parseInt(monsterParts[1]);

                AbstractMonster pipedMonster = ReflectionUtils.tryConstructMonster(className, startingOffsetX - spacing, startingOffsetY);
                pipedMonster.maxHealth = maxHp;
                pipedMonster.currentHealth = maxHp;

                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(pipedMonster, true));

                spacing += pipedMonster.hb_w;
            }
        }
    }

    // Copied from Gremlin Leader
    private static void scarePipedMinions() {
//        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
//            if (!m.isDying) {
//                AbstractDungeon.actionManager.addToBottom(new ShoutAction((AbstractCreature)m, "RUN!!!", 0.5F, 1.2F));
//            }
//        }
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }
    }
}
