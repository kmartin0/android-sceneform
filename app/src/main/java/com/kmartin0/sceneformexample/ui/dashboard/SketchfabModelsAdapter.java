package com.kmartin0.sceneformexample.ui.dashboard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmartin0.sceneformexample.R;
import com.kmartin0.sceneformexample.model.SketchfabModel;

import java.util.List;

public class SketchfabModelsAdapter extends RecyclerView.Adapter<SketchfabModelsAdapter.ViewHolder> {

    private Context context;
    private List<SketchfabModel> modelData;
    private ItemClickListener modelClickListener;

    public SketchfabModelsAdapter(List<SketchfabModel> modelData, ItemClickListener modelClickListener) {
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
        final SketchfabModel model = modelData.get(position);

        holder.name.setText(model.getName());

        Glide.with(context)
                .load(model.getThumbnailUrl())
                .into(holder.modelIMG);
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView modelIMG;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.model_name);
            modelIMG = itemView.findViewById(R.id.prevImageModel);

            itemView.setOnClickListener(v -> {
                if (modelClickListener != null)
                modelClickListener.onItemClick(modelData.get(getAdapterPosition())); // TODO: Download link
            });
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(SketchfabModel sketchfabModel);
    }

}
