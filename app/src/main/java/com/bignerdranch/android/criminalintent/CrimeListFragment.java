package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.view.View.*;


public class CrimeListFragment extends Fragment {
    public static final String SUBTITLE_VISIBILITY = "SUBTITLE_VISIBILITY";
    RecyclerView crimeRecyclerView;
    CrimeAdapter adapter;
    private boolean subtitleVisible;
    int crimePosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBILITY);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBILITY, subtitleVisible);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem item = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            item.setTitle(R.string.hide_subtitle);
        } else {
            item.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get().addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get();

        if (adapter == null) {
            adapter = new CrimeAdapter(crimeLab.getCrimes());
            crimeRecyclerView.setAdapter(adapter);
        } else {
            if(crimePosition < 0) {
                adapter.notifyDataSetChanged();
            } else {
                adapter.setCrimes(crimeLab.getCrimes());
                adapter.notifyItemChanged(crimePosition);
                crimePosition = -1;
            }
        }
        updateSubtitle();
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get();
        String subtitle = getString(R.string.subtitle_format, crimeLab.getCrimes().size());

        if(!subtitleVisible){
            subtitle = null;
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    private abstract class AbstractCrimeHolder extends RecyclerView.ViewHolder{
        public abstract void bind(Crime crime);

        public AbstractCrimeHolder(LayoutInflater inflater, ViewGroup parent, int layout) {
            super(inflater.inflate(layout, parent, false));
        }

    }

    private class CrimeHolder extends AbstractCrimeHolder implements OnClickListener{
        private Crime crime;

        private TextView crimeTitle;
        private TextView crimeDate;
        private ImageView solvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent,  R.layout.list_item_crime);

            itemView.setOnClickListener(this);

            crimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            crimeDate = (TextView) itemView.findViewById(R.id.crime_date);
            solvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);

        }

        @Override
        public void bind(Crime crime) {
            this.crime = crime;
            crimeTitle.setText(this.crime.getTitle());
            crimeDate.setText(DateFormat.format("EEEE, MMM d, yyyy", this.crime.getDate()));
            solvedImageView.setVisibility(this.crime.isSolved() ? VISIBLE : GONE);
        }

        @Override
        public void onClick(View v) {
            crimePosition = getAdapterPosition();
            startActivity(CrimePagerActivity.newIntent(getActivity(), crime.getId()));
        }
    }

    private class PoliceCrimeHolder extends AbstractCrimeHolder implements OnClickListener {
        private Crime crime;

        private TextView crimeTitle;
        private TextView crimeDate;
        private Button policeButton;

        public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.list_item_crime_requires_police);

            itemView.setOnClickListener(this);

            crimeTitle = (TextView) itemView.findViewById(R.id.crime_title);
            crimeDate = (TextView) itemView.findViewById(R.id.crime_date);
            policeButton = (Button) itemView.findViewById(R.id.police_button);

        }

        @Override
        public void bind(Crime crime) {
            this.crime = crime;
            crimeTitle.setText(this.crime.getTitle());
            crimeDate.setText(this.crime.getDate().toString());

            policeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "The po-po are on the way!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), crime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<AbstractCrimeHolder> {
        private List<Crime> crimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.crimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            this.crimes = crimes;
        }

        @Override
        public AbstractCrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == R.layout.list_item_crime_requires_police) {
                return new PoliceCrimeHolder(LayoutInflater.from(getActivity()), parent);
            }

            return new CrimeHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(AbstractCrimeHolder holder, int position) {
            holder.bind(crimes.get(position));
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(crimes.get(position).requiresPolice()) {
                return R.layout.list_item_crime_requires_police;
            }
            return R.layout.fragment_crime_list;
        }
    }
}
