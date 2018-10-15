package com.srikanth.popularmoviesstage1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import adapter.ImageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.txtTitle)
    TextView mTxtTitle;

    @BindView(R.id.txtReleaseDate)
    TextView mReleaseDate;

    @BindView(R.id.txtVoteAverage)
    TextView mTxtVoteAverage;

    @BindView(R.id.txtSynopsis)
    TextView mTxtOverview;

    @BindView(R.id.poster_path)
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra("backdrop");

        String overView = movie.getOverview();
        mTxtOverview.setText(overView);
        Log.d("overview", "" + overView);

        String title = movie.getTitle();
        Log.d("title", "" + title);
        mTxtTitle.setText(title);

        String voteAverage = movie.getVote_average();
        Log.d("average", "" + voteAverage);
        mTxtVoteAverage.setText(voteAverage);

        String releaseDate = movie.getRelease_date();
        Log.d("release date", "" + releaseDate);
        mReleaseDate.setText(releaseDate);

        String mImageUrl = movie.getPoster_path();
        Picasso.get().load(ImageAdapter.BASE_URL + mImageUrl).placeholder(R.mipmap.ic_launcher).into(mImageView);
        Log.d("imgUrl", "" + movie.getPoster_path());
    }
}
