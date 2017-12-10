package com.bignerdranch.android.criminalintent;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab crimeLab;
    private Map<UUID, Crime> crimes;

    private CrimeLab() {
        crimes = new LinkedHashMap<>();

        for(int i = 0; i<100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            crime.setRequiresPolice(i % 10 == 0);
            crimes.put(crime.getId(), crime);
        }
    }

    public static CrimeLab get() {
        if(crimeLab == null) {
            crimeLab = new CrimeLab();
        }
        return crimeLab;
    }

    public Crime getCrime(UUID uuid) {
        return crimes.get(uuid);
    }

    public void solveCrime(UUID uuid, boolean isChecked) {
        getCrime(uuid).setSolved(isChecked);
    }

    public List<Crime> getCrimes() {
        return new ArrayList<>(crimes.values());    
    }
}
