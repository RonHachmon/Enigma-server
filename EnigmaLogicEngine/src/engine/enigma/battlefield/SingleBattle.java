package engine.enigma.battlefield;

import DTO.DecryptionCandidate;

import java.io.InputStream;
import java.util.List;

public class SingleBattle {
    private BattleFieldInfo battleField;
    private InputStream machineFile;
    private List<DecryptionCandidate> decryptedWords;
}
