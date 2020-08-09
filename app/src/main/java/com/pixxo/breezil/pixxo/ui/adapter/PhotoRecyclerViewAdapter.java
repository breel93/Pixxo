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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ItemNetworkStateBinding;
import com.pixxo.breezil.pixxo.databinding.PhotoViewItemBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.NetworkState;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;

public class PhotoRecyclerViewAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

  private PhotoLongClickListener photoLongClickListener;
  private PhotoClickListener photoClickListener;
  private Context context;

  private static final int TYPE_PROGRESS = 0;
  private static final int TYPE_ITEM = 1;
  private NetworkState networkState;

  public PhotoRecyclerViewAdapter(
      Context context,
      PhotoClickListener photoClickListener,
      PhotoLongClickListener photoLongClickListener) {
    super(DIFF_CALLBACK);
    this.context = context;
    this.photoClickListener = photoClickListener;
    this.photoLongClickListener = photoLongClickListener;
  }

  private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
          return oldItem.getLargeImageURL().equals(newItem.getLargeImageURL());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
          return newItem == oldItem;
        }
      };

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    if (viewType == TYPE_PROGRESS) {
      ItemNetworkStateBinding networkStateBinding =
          ItemNetworkStateBinding.inflate(layoutInflater, parent, false);
      return new NetworkStateItemViewHolder(networkStateBinding);
    } else {
      PhotoViewItemBinding binding = PhotoViewItemBinding.inflate(layoutInflater, parent, false);
      return new ImageHolder(binding);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof ImageHolder) {
      Photo photo = getItem(position);
      ((ImageHolder) viewHolder).bind(photo, photoClickListener, photoLongClickListener);
    } else {
      ((NetworkStateItemViewHolder) viewHolder).bindView(networkState);
    }
  }

  private boolean hasExtraRow() {
    return networkState != null && networkState != NetworkState.LOADED;
  }

  @Override
  public int getItemViewType(int position) {
    if (hasExtraRow() && position == getItemCount() - 1) {
      return TYPE_PROGRESS;
    } else {
      return TYPE_ITEM;
    }
  }

  public void setNetworkState(NetworkState newNetworkState) {
    NetworkState previousState = this.networkState;
    boolean previousExtraRow = hasExtraRow();
    this.networkState = newNetworkState;
    boolean newExtraRow = hasExtraRow();
    if (previousExtraRow != newExtraRow) {
      if (previousExtraRow) {
        notifyItemRemoved(getItemCount());
      } else {
        notifyItemInserted(getItemCount());
      }
    } else if (newExtraRow && previousState != newNetworkState && getItemCount() > 0) {
      notifyItemChanged(getItemCount() - 1);
    }
  }

  class ImageHolder extends RecyclerView.ViewHolder {
    PhotoViewItemBinding binding;
    private boolean clicked = false;

    ImageHolder(PhotoViewItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(
        Photo photo,
        PhotoClickListener photoClickListener,
        PhotoLongClickListener photoLongClickListener) {

      itemView.setOnClickListener(
          v -> {
            if (!clicked) {
              clicked = true;
              photoClickListener.showFullPhoto(photo);
              Handler clickHandler = new Handler();
              clickHandler.postDelayed(() -> clicked = false, 1000);
            }
          });
      itemView.setOnLongClickListener(
          v -> {
            photoLongClickListener.doSomethingWithPhoto(photo);
            return true;
          });

      CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
      circularProgressDrawable.setStrokeWidth(10f);
      circularProgressDrawable.setCenterRadius(40f);
      circularProgressDrawable.setColorSchemeColors(
          context.getResources().getColor(R.color.colorAccent),
          context.getResources().getColor(R.color.colorblue));
      circularProgressDrawable.start();

      double width = photo.getWebformatWidth();
      double height = photo.getWebformatHeight() / .75;
      binding.photoItem.setAspectRatio((height / width));
      Glide.with(context)
          .load(photo.getWebformatURL())
          .thumbnail(0.1f)
          //          .centerCrop()
          //          .fitCenter()
          .apply(
              new RequestOptions()
                  .placeholder(circularProgressDrawable)
                  .error(R.drawable.placeholder))
          .into(binding.photoItem);
    }
  }

  public static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

    private ItemNetworkStateBinding binding;

    NetworkStateItemViewHolder(ItemNetworkStateBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bindView(NetworkState networkState) {
      if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
        binding.progressBar.setVisibility(View.VISIBLE);
      } else {
        binding.progressBar.setVisibility(View.GONE);
      }

      if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
        binding.errorMsg.setVisibility(View.VISIBLE);
      } else {
        binding.errorMsg.setVisibility(View.GONE);
      }
    }
  }
}
