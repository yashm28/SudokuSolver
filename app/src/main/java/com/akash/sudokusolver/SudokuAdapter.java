package com.akash.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SudokuAdapter extends RecyclerView.Adapter<SudokuAdapter.ViewHolder> {

    Context context;
    int[][] solved, unsolved;

    public SudokuAdapter(Context context, int[][] solved, int[][] unsolved) {
        this.context = context;
        this.solved = solved;
        this.unsolved = unsolved;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_button, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Button button = (Button) holder.view.findViewById(R.id.item);
        button.setText(Integer.toString(solved[position/9][position%9]));
        if(unsolved[position/9][position%9] != 0) {
            button.setTextColor(Color.BLACK);
        }
        if(position/9 > 2 && position/9 < 6) {
            if(position%9 < 3 || position%9 > 5) {
                button.setBackground(context.getResources().getDrawable(R.drawable.grid_background_cyan));
            }
        }
        else {
            if(position%9 > 2 && position%9 < 6) {
                button.setBackground(context.getResources().getDrawable(R.drawable.grid_background_cyan));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 81;
    }

}
