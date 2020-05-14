package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ManifestPatch;
import stsjorbsmod.powers.ShriekingHatPower;

public class ShriekingHatSaveData implements CustomSavable<Integer> {
    public static int damageTaken = 0;

    @Override
    public Integer onSave() {
        return damageTaken;
    }

    @Override
    public void onLoad(Integer savedData) {
        damageTaken = savedData;
    }
}
