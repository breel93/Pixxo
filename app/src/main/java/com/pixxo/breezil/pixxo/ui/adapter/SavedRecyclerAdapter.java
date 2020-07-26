package com.pixxo.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.PhotoViewItemBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class SavedRecyclerAdapter
    extends RecyclerView.Adapter<SavedRecyclerAdapter.SavedImageHolder>{
  private PhotoLongClickListener savedPhotoLongClickListener;
  private PhotoClickListener savedPhotoClickListener;
  private Context context;
  private PhotoViewItemBinding binding;
  private List<Photo> photoList = new ArrayList<>();

  public SavedRecyclerAdapter(PhotoLongClickListener savedPhotoLongClickListener,
                              PhotoClickListener savedPhotoClickListener, Context context) {
    this.savedPhotoLongClickListener = savedPhotoLongClickListener;
    this.savedPhotoClickListener = savedPhotoClickListener;
    this.context = context;
  }

  @NonNull
  @Override
  public SavedImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    binding = PhotoViewItemBinding.inflate(layoutInflater, parent, false);
    return new SavedImageHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull SavedImageHolder holder, int position) {
    Photo photo = photoList.get(position);
    holder.bind(photo, savedPhotoClickListener, savedPhotoLongClickListener);
  }

  @Override
  public int getItemCount() {
    return photoList.size();
  }

  public void setPhotos(List<Photo> photos){
//    photoList = photos;
    photoList.addAll(photos);
    notifyDataSetChanged();
  }

  class SavedImageHolder extends RecyclerView.ViewHolder {
    SavedImageHolder(PhotoViewItemBinding photoItemBinding) {
      super(binding.getRoot());
      binding = photoItemBinding;
    }

    void bind(
        Photo photo,
        PhotoClickListener photoClickListener,
        PhotoLongClickListener photoLongClickListener) {
      itemView.setOnClickListener(v -> photoClickListener.showFullPhoto(photo));
      itemView.setOnLongClickListener(
          v -> {
            photoLongClickListener.doSomethingWithPhoto(photo);
            return true;
          });
      CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
      circularProgressDrawable.setStrokeWidth(10f);
      circularProgressDrawable.setCenterRadius(40f);
      circularProgressDrawable.setColorSchemeColors(
          R.color.colorAccent, R.color.colorPrimary, R.color.colorblue, R.color.hotPink);
      circularProgressDrawable.start();
      double width = photo.getWebformatWidth();
//      double height = photo.getWebformatHeight() / .75;
//      binding.photoItem.setAspectRatio(height / width);
//      Glide.with(context).clear(binding.photoItem);
      Glide.with(context)
          .load(photo.getWebformatURL())
          .skipMemoryCache(true)
          .diskCacheStrategy( DiskCacheStrategy.NONE )
          .apply(
              new RequestOptions()
                  .placeholder(R.drawable.placeholder)
                  .error(R.drawable.placeholder))
          .into(binding.photoItem);
    }
  }
}
