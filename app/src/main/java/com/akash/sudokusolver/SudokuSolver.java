package com.akash.sudokusolver;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SudokuSolver extends AppCompatActivity {

    public String picturePath;
    private Integer result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivityForResult(i, 1213);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {
            result = data.getIntExtra("type", 0);
            if(result == 1){
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1212);
            }
            else{
                SudokuSolve solver = new SudokuSolve();
                try {
                    int[][] ans = solver.solve(getBaseContext(), null, 0);
                    String result = "\t\t\t\t\t----------------------------------------------\n\t\t\t\t\t";
                    int i=0;
                    for (int[] row : ans) {
                        i++;
                        for (int j = 0; j < row.length; j++) {
                            if (j != 0 && j % 3 != 0) {
                                result += "| " + row[j] + " ";
                            } else {
                                result += "|| " + row[j] + " ";
                            }
                        }
                        if(i !=0 && i % 3 == 0){
                            result += "||\n\t\t\t\t\t----------------------------------------------\n\t\t\t\t\t";
                        }
                        else{
                            result += "||\n\t\t\t\t\t||-------------||-------------||-------------||\n\t\t\t\t\t";
                        }
                    }
                    TextView tv = (TextView) findViewById(R.id.sudoku_text);
                    tv.setText(result);
                    tv.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    Toast.makeText(this, "Photo is not readable...", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == 1212 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            SudokuSolve solver = new SudokuSolve();
            try {
                int[][] ans = solver.solve(getBaseContext(), picturePath, 1);
                String result = "\t\t\t\t\t----------------------------------------------\n\t\t\t\t\t";

                int i=0;
                for (int[] row : ans) {
                    i++;
                    for (int j = 0; j < row.length; j++) {
                        if (j != 0 && j % 3 != 0) {
                            result += "| " + row[j] + " ";
                        } else {
                            result += "|| " + row[j] + " ";
                        }
                    }
                    if(i !=0 && i % 3 == 0){
                        result += "||\n\t\t\t\t\t----------------------------------------------\n\t\t\t\t\t";
                    }
                    else{
                        result += "||\n\t\t\t\t\t||-------------||-------------||-------------||\n\t\t\t\t\t";
                    }
                }
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                TextView tv = (TextView) findViewById(R.id.sudoku_text);
                tv.setText(result);
                tv.setVisibility(View.VISIBLE);
//                RecyclerView rv = (RecyclerView) findViewById(R.id.sudoku_text);
//                rv.setVisibility(View.VISIBLE);
//                RecyclerView.LayoutManager lm = new GridLayoutManager(this, 9, LinearLayoutManager.VERTICAL, false);
//                rv.setLayoutManager(lm);
//                SudokuAdapter adapter = new SudokuAdapter(this, ans, solver.matrix);
//                rv.setAdapter(adapter);
            } catch (IOException e) {
                Toast.makeText(this, "Photo is not readable...", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "You didn't pick any Image",
                    Toast.LENGTH_LONG).show();
        }
    }

}