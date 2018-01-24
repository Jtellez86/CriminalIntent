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
    }

    public void addCrime(Crime crime) {
        crimes.put(crime.getId(), crime);
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
