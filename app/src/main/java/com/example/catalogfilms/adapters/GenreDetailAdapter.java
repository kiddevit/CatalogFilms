package com.example.catalogfilms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogfilms.R;
import com.example.catalogfilms.models.DetailGenre;
import com.example.catalogfilms.services.GenreDetailSingletonService;

import java.util.List;

import static com.example.catalogfilms.utils.ColorUtils.mapColorByRating;

public class GenreDetailAdapter extends RecyclerView.Adapter<GenreDetailAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<DetailGenre> detailGenreList;
    private GenreDetailSingletonService genreDetailService;

    public GenreDetailAdapter(Context context, List<DetailGenre> detailGenreList) {
        this.detailGenreList = detailGenreList;
        this.inflater = LayoutInflater.from(context);
        this.genreDetailService = GenreDetailSingletonService.getInstance();
    }

    @NonNull
    @Override
    public GenreDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.genre_detail_item, parent, false);
        return new GenreDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreDetailAdapter.ViewHolder holder, int position) {
        DetailGenre detailGenre = detailGenreList.get(position);
        holder.ratingView.setText(detailGenre.getRating().toString());
        holder.ratingView.setTextColor(mapColorByRating(detailGenre.getRating()));
        holder.titleView.setText(detailGenre.getTitle());
        holder.descriptionView.setText(detailGenre.getDescription());
    }

    @Override
    public int getItemCount() {
        return detailGenreList.size();
    }

    public List<DetailGenre> getDetailGenreList() {
        return detailGenreList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ratingView, titleView, descriptionView;

        ViewHolder(View view) {
            super(view);
            ratingView = (TextView) view.findViewById(R.id.rating);
            titleView = (TextView) view.findViewById(R.id.title);
            descriptionView = (TextView) view.findViewById(R.id.description);
            initClickItem(view, this);
        }
    }

    private void initClickItem(View view, final GenreDetailAdapter.ViewHolder viewHolder) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genreDetailService.setSelectDetailGenre(viewHolder);
            }
        });
    }
}
