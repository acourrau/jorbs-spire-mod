package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ExtraMonsterPatch;

public class PipedMonstersSaveData implements CustomSavable<String> {
    @Override
    public String onSave() {
        return ExtraMonsterPatch.ExtraMonsterField.pipedMonsters.get(AbstractDungeon.player);
    }

    @Override
    public void onLoad(String savedData) {
        ExtraMonsterPatch.ExtraMonsterField.pipedMonsters.set(AbstractDungeon.player, savedData);
    }
}
