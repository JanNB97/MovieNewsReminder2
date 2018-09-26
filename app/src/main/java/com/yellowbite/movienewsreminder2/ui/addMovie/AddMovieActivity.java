package com.yellowbite.movienewsreminder2.ui.addMovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;
import com.yellowbite.movienewsreminder2.ui.NoTitleBarActivity;

public class AddMovieActivity extends NoTitleBarActivity
{
    private TextView searchTextView;
    private Button searchMovieButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewWithoutTitleBar(R.layout.activity_add_movie);

        this.searchTextView = this.findViewById(R.id.searchTextView);
        this.searchMovieButton = this.findViewById(R.id.searchMovieButton);

        this.initSearchMovieButton();
    }

    private void initSearchMovieButton()
    {
        this.searchMovieButton.setOnClickListener(
                v -> this.handleClickedOnSearchMovie(searchTextView.getText().toString()));
    }

    // --- --- --- Handle user interaction --- --- ---
    private void handleClickedOnSearchMovie(String searchText)
    {
        // TODO
    }

    // --- --- --- start me from another activity --- --- ---
    public static int REQUEST_CODE = 2;

    public static void startForResult(AppCompatActivity app)
    {
        Intent intent = new Intent(app, AddMovieActivity.class);
        app.startActivityForResult(intent, REQUEST_CODE);
    }
}
