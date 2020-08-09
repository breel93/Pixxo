/**
 * Designed and developed by Kola Emiola
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixxo.breezil.pixxo.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.PhotoViewItemBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import com.pixxo.breezil.pixxo.ui.callbacks.EditedPhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.EditedPhotoLongClickListener;

public class EditPhotoRecyclerAdapter
    extends ListAdapter<EditedPhoto, EditPhotoRecyclerAdapter.EditPhotoHolder> {
  private PhotoViewItemBinding binding;
  private EditedPhotoClickListener photoClickListener;
  private EditedPhotoLongClickListener photoLongClickListener;

  public EditPhotoRecyclerAdapter(
      EditedPhotoClickListener photoClickListener,
      EditedPhotoLongClickListener photoLongClickListener) {
    super(DIFF_CALLBACK);
    this.photoClickListener = photoClickListener;
    this.photoLongClickListener = photoLongClickListener;
  }

  private static final DiffUtil.ItemCallback<EditedPhoto> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<EditedPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull EditedPhoto oldItem, @NonNull EditedPhoto newItem) {
          return oldItem.getImage() == newItem.getImage();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(
            @NonNull EditedPhoto oldItem, @NonNull EditedPhoto newItem) {
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
    holder.bind(photo, photoClickListener, photoLongClickListener);
  }

  class EditPhotoHolder extends RecyclerView.ViewHolder {
    private boolean clicked = false;

    EditPhotoHolder(PhotoViewItemBinding photoViewItemBinding) {
      super(binding.getRoot());
      binding = photoViewItemBinding;
    }

    void bind(
        EditedPhoto editedPhoto,
        EditedPhotoClickListener photoClickListener,
        EditedPhotoLongClickListener photoLongClickListener) {
      itemView.setOnClickListener(v -> {
        if (!clicked) {
          clicked = true;
          photoClickListener.showFullPicture(editedPhoto);
          Handler clickHandler = new Handler();
          clickHandler.postDelayed(() -> clicked = false, 1000);
        }
      });
      itemView.setOnLongClickListener(
          v -> {
            photoLongClickListener.doSomething(editedPhoto);
            return true;
          });
      double ratio = 0;
      if(editedPhoto.getImage() != null) {
        double width = editedPhoto.getImage().getWidth();
        double height = editedPhoto.getImage().getHeight() / .75;
        ratio = height / width;
      }
      binding.photoItem.setAspectRatio(ratio);
      if (editedPhoto.getImage() != null) {
        binding.photoItem.setImageBitmap(editedPhoto.getImage());
      } else {
        binding.photoItem.setImageResource(R.drawable.placeholder);
      }
    }
  }
}
