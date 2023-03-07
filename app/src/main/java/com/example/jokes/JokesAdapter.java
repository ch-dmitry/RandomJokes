package com.example.jokes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {

    private List<Joke> jokes = new ArrayList<>();
    private OnJokeClickListener onJokeClickListener;

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
        notifyDataSetChanged();
    }

    public List<Joke> getJokes() {
        return new ArrayList<>(jokes);
    }

    public void setOnJokeClickListener(OnJokeClickListener onJokeClickListener) {
        this.onJokeClickListener = onJokeClickListener;
    }

    @NonNull
    @Override
    public JokesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_item, parent, false);
        return new JokesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JokesViewHolder viewHolder, int position) {
        Joke joke = jokes.get(position);

        viewHolder.tvAddedScreenSetup.setText(joke.getSetup());
        viewHolder.tvAddedScreenPunchline.setText(joke.getPunchline());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJokeClickListener != null) {
                    onJokeClickListener.onJokeClick(joke);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    class JokesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddedScreenSetup;
        private TextView tvAddedScreenPunchline;

        public JokesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddedScreenSetup = itemView.findViewById(R.id.tvAddedScreenSetup);
            tvAddedScreenPunchline = itemView.findViewById(R.id.tvAddedScreenPunchline);
        }
    }

    interface OnJokeClickListener {
        void onJokeClick(Joke joke);
    }
}
