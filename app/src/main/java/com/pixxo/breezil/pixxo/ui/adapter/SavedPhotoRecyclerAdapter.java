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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.PhotoViewItemBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;

public class SavedPhotoRecyclerAdapter
    extends ListAdapter<Photo, SavedPhotoRecyclerAdapter.SavedImageHolder> {
  private PhotoLongClickListener savedPhotoLongClickListener;
  private PhotoClickListener savedPhotoClickListener;
  private Context context;
  private PhotoViewItemBinding binding;

  public SavedPhotoRecyclerAdapter(
      Context context,
      PhotoLongClickListener savedPhotoLongClickListener,
      PhotoClickListener savedPhotoClickListener) {
    super(DIFF_CALLBACK);
    this.context = context;
    this.savedPhotoClickListener = savedPhotoClickListener;
    this.savedPhotoLongClickListener = savedPhotoLongClickListener;
  }

  private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
          return oldItem.getRoomId() == newItem.getRoomId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
          return oldItem == newItem;
        }
      };

  @NonNull
  @Override
  public SavedImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
    binding = PhotoViewItemBinding.inflate(layoutInflater, viewGroup, false);
    return new SavedImageHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull SavedImageHolder savedImageHolder, int i) {
    Photo photo = getItem(i);
    savedImageHolder.bind(photo, savedPhotoClickListener, savedPhotoLongClickListener);
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
      double height = photo.getWebformatHeight() / .75;
      binding.photoItem.setAspectRatio(height / width);
      Glide.with(context)
          .load(photo.getWebformatURL())
          .apply(
              new RequestOptions()
                  .placeholder(R.drawable.placeholder)
                  .error(R.drawable.placeholder))
          .into(binding.photoItem);
    }
  }
}
