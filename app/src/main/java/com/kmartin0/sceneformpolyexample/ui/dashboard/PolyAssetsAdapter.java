package com.kmartin0.sceneformpolyexample.ui.dashboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmartin0.sceneformpolyexample.R;
import com.kmartin0.sceneformpolyexample.api.PolyResponse;

import java.util.List;

public class PolyAssetsAdapter extends RecyclerView.Adapter<PolyAssetsAdapter.ViewHolder> {

    private Context context;
    private List<PolyResponse> modelData;
    private ItemClickListener modelClickListener;

    public PolyAssetsAdapter(List<PolyResponse> modelData, ItemClickListener modelClickListener) {
        this.modelData = modelData;
        this.modelClickListener = modelClickListener;
    }

    // inflates the row layout from xml when needed
    @NonNull
	@Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_model, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PolyResponse model = modelData.get(position);

        holder.name.setText(model.getDisplayName());
        holder.description.setText(model.getDescription());

        Glide.with(context)
                .load(model.getThumbnail().getUrl())
                .into(holder.modelIMG);
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public ImageView modelIMG;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.model_name);
            description = itemView.findViewById(R.id.model_description);
            modelIMG = itemView.findViewById(R.id.prevImageModel);

            itemView.setOnClickListener(v -> {
                if (modelClickListener != null)
                modelClickListener.onItemClick(modelData.get(getAdapterPosition()));
            });
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(PolyResponse pollyResponse);
    }

}
