package com.pixxo.breezil.pixxo.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.PhotoViewItemBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;

public class EditPhotoRecyclerAdapter extends ListAdapter<EditedPhoto, EditPhotoRecyclerAdapter.EditPhotoHolder> {
  private PhotoViewItemBinding binding;

  public EditPhotoRecyclerAdapter() {
    super(DIFF_CALLBACK);
  }

  private static final DiffUtil.ItemCallback<EditedPhoto> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<EditedPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull EditedPhoto oldItem, @NonNull EditedPhoto newItem) {
          return oldItem.getImage() == newItem.getImage();
        }


        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull EditedPhoto oldItem, @NonNull EditedPhoto newItem) {
          return oldItem == newItem;
        }
      };


  @NonNull
  @Override
  public EditPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    binding = PhotoViewItemBinding.inflate(layoutInflater, parent, false);
    return new EditPhotoHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull EditPhotoHolder holder, int position) {
    EditedPhoto photo = getItem(position);
    holder.bind(photo);
  }

  class EditPhotoHolder extends RecyclerView.ViewHolder {
    EditPhotoHolder(PhotoViewItemBinding photoViewItemBinding) {
      super(binding.getRoot());
      binding = photoViewItemBinding;
    }

    void bind(EditedPhoto editedPhoto) {
      double width = editedPhoto.getImage().getWidth();
      double height = editedPhoto.getImage().getHeight() / .75;
      binding.photoItem.setAspectRatio(height / width);
      if (editedPhoto.getImage() != null) {
        binding.photoItem.setImageBitmap(editedPhoto.getImage());
      } else {
        binding.photoItem.setImageResource(R.drawable.placeholder);
      }
    }
  }
}
