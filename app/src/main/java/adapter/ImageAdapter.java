package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.srikanth.popularmoviesstage1.DetailActivity;
import com.srikanth.popularmoviesstage1.Movie;
import com.srikanth.popularmoviesstage1.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Result;

public class ImageAdapter extends BaseAdapter {
    private List<Result> imageUrls;
    private Context context;
    private LayoutInflater layoutInflater;
    public static String BASE_URL = "http://image.tmdb.org/t/p/w185";
    private Result result;

    public ImageAdapter(List<Result> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_view_item, parent, false);
            viewHolder = new ImageViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ImageViewHolder) view.getTag();
        }
        result = (Result) getItem(position);
        final String urlImage = BASE_URL + result.getPosterPath();
        Log.d("imgUrl", "" + urlImage);
        Picasso.get().load(urlImage).placeholder(R.mipmap.ic_launcher).fit().into(viewHolder.iv);
        return view;
    }

    class ImageViewHolder {
        @BindView(R.id.image_view)
        ImageView iv;

        public ImageViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.image_view)
        public void onClick() {
            Movie mMovie = new Movie();
            Intent i = new Intent(context, DetailActivity.class);
            mMovie.setPoster_path(result.getPosterPath());
            mMovie.setOverview(result.getOverview());
            mMovie.setTitle(result.getTitle());
            mMovie.setVote_average(String.valueOf(result.getVoteAverage()));
            mMovie.setRelease_date(result.getReleaseDate());
            i.putExtra("backdrop", mMovie);
            context.startActivity(i);
        }
    }
}
