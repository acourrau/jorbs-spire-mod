package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.util.MonsterUtils;
import stsjorbsmod.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;

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
        public static void patch() {
            // Yucky monster placement. Don't like
            spawnMonsters(200, 325);
        }
    }

    @SpirePatch(clz = Hexaghost.class, method = "usePreBattleAction")
    @SpirePatch(clz = TheGuardian.class, method = "usePreBattleAction")
    public static class AddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch() {
            spawnMonsters(-300, 50);
        }
    }

    @SpirePatch(clz = TheCollector.class, method = "usePreBattleAction")
    public static class CollectorAddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch() {
            // Yucky monster placement. Don't like
            spawnMonsters(-300, 300);
        }
    }

    @SpirePatch(clz = Champ.class, method = "usePreBattleAction")
    public static class ChampAddExtraMonsterPatch
    {
        @SpirePostfixPatch
        public static void patch() {
            spawnMonsters(-300, 50);
        }
    }

    @SpirePatch(clz = Champ.class, method = SpirePatch.CONSTRUCTOR)
    public static class ChampRepositionPatch
    {
        @SpirePostfixPatch
        public static void patch(Champ __instance) {
            __instance.drawX = Settings.WIDTH * 0.75f + (200) * Settings.scale;
            __instance.refreshIntentHbLocation();
        }
    }

    @SpirePatch(clz = SlimeBoss.class, method = "die")
    @SpirePatch(clz = Hexaghost.class, method = "die")
    @SpirePatch(clz = TheGuardian.class, method = "die")
    @SpirePatch(clz = TheCollector.class, method = "die")
    @SpirePatch(clz = Champ.class, method = "die")
    public static class ScareExtraMonstersPatch
    {
        @SpirePostfixPatch
        public static void patch() {
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
