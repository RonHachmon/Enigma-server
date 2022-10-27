package engine.bruteForce2.utils;

import DTO.DecryptionCandidate;

import java.util.ArrayList;
import java.util.List;

public class CandidateList {
    private List<DecryptionCandidate> decryptionCandidates=new ArrayList<>();

    public CandidateList()
    {

    }
    synchronized public void addCandidate(DecryptionCandidate decryptionCandidate)
    {
        decryptionCandidates.add(decryptionCandidate);
    }
    public int getSize()
    {
        return decryptionCandidates.size();
    }
    public List<DecryptionCandidate> getList()
    {
        return decryptionCandidates;
    }

}
