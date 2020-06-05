package stsjorbsmod.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterUtils {
    public static String serializeMonsterForSaving(AbstractMonster m) {
        String monsterString = "";
        if (m != null) {
            monsterString += m.getClass().toString().substring(6) + "|" + m.maxHealth;
        }
        return monsterString;
    }

    public static AbstractMonster deserializeMonsterFromSave(String monsterString) {
        return null;
    }
}
