package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ManifestPatch;
import stsjorbsmod.powers.ShriekingHatPower;

public class ShriekingHatSaveData implements CustomSavable<Integer> {
    @Override
    public Integer onSave() {
        return ShriekingHatPower.damageTaken;
    }

    @Override
    public void onLoad(Integer savedData) {
        ShriekingHatPower.damageTaken = savedData;
    }
}
