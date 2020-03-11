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
package com.pixxo.breezil.pixxo.ui.bottom_sheet;

import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentActionBottomSheetBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.ui.ImageSaveUtils;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotosViewModel;
import com.pixxo.photoeditor.EditImageActivity;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
public class ActionBottomSheetFragment extends BottomSheetDialogFragment {

  private FragmentActionBottomSheetBinding binding;
  private Context mContext;

  private ImageSaveUtils imageSaveUtils;

  private SavedPhotosViewModel viewModel;

  private ProgressDialog mProgress;

  @Inject ViewModelProvider.Factory viewModelFactory;

  public static ActionBottomSheetFragment getPhoto(Photo photo) {
    ActionBottomSheetFragment fragment = new ActionBottomSheetFragment();
    Bundle args = new Bundle();
    args.putParcelable(SINGLE_PHOTO, photo);
    fragment.setArguments(args);
    return fragment;
  }

  public ActionBottomSheetFragment() {
    // Required empty public constructor

  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_action_bottom_sheet, container, false);
    this.mContext = getActivity();
    imageSaveUtils = new ImageSaveUtils(mContext);
    mProgress = new ProgressDialog(mContext);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(SavedPhotosViewModel.class);
    updateUi(getPhoto());
    return binding.getRoot();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    AndroidSupportInjection.inject(this);
  }

  private void updateUi(Photo photo) {

    binding.selectEdit.setOnClickListener(
        v -> {
          Glide.with(getActivity())
              .load(photo.getWebformatURL())
              .listener(
                  new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                        @Nullable GlideException e,
                        Object model,
                        Target<Drawable> target,
                        boolean isFirstResource) {
                      Toast.makeText(
                              ActionBottomSheetFragment.this.mContext,
                              R.string.cant_edit_until_image_is_displayed,
                              Toast.LENGTH_SHORT)
                          .show();
                      return true;
                    }

                    @Override
                    public boolean onResourceReady(
                        Drawable resource,
                        Object model,
                        Target<Drawable> target,
                        DataSource dataSource,
                        boolean isFirstResource) {
                      Intent editIntent =
                          new Intent(
                              ActionBottomSheetFragment.this.mContext, EditImageActivity.class);
                      editIntent.putExtra(SINGLE_PHOTO, photo.getWebformatURL());
                      ActionBottomSheetFragment.this.mContext.startActivity(editIntent);
                      return true;
                    }
                  })
              .submit();
          dismiss();
        });

    binding.selectDownload.setOnClickListener(
        v -> {
          Glide.with(getActivity())
              .asBitmap()
              .load(photo.getWebformatURL())
              .listener(
                  new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(
                        @Nullable GlideException e,
                        Object model,
                        Target<Bitmap> target,
                        boolean isFirstResource) {
                      Toast.makeText(
                              ActionBottomSheetFragment.this.mContext,
                              R.string.cant_download_until_images_is_loaded,
                              Toast.LENGTH_SHORT)
                          .show();
                      return true;
                    }

                    @Override
                    public boolean onResourceReady(
                        Bitmap bitmap,
                        Object model,
                        Target<Bitmap> target,
                        DataSource dataSource,
                        boolean isFirstResource) {
                      if (ContextCompat.checkSelfPermission(
                              ActionBottomSheetFragment.this.mContext,
                              Manifest.permission.READ_EXTERNAL_STORAGE)
                          == PackageManager.PERMISSION_GRANTED) {
                        mProgress.setTitle(mContext.getString(R.string.downloading));
                        mProgress.setMessage(
                            mContext.getString(R.string.please_wait_image_is_downloading));
                        mProgress.setCancelable(false);
                        mProgress.show();
                        Handler handler = new Handler();
                        handler.postDelayed(
                            () -> {
                              imageSaveUtils.startDownloading(
                                  ActionBottomSheetFragment.this.mContext, bitmap);
                              mProgress.dismiss();
                              Toast.makeText(
                                      ActionBottomSheetFragment.this.mContext,
                                      R.string.downloaded,
                                      Toast.LENGTH_SHORT)
                                  .show();
                            },
                            1000);

                      } else {
                        ActivityCompat.requestPermissions(
                            (Activity) ActionBottomSheetFragment.this.mContext,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);
                      }

                      return true;
                    }
                  })
              .submit();
          dismiss();
        });
    binding.selectSaved.setOnClickListener(
        v ->
            viewModel
                .insertPhoto(photo)
                .observe(
                    getViewLifecycleOwner(),
                    s -> {
                      Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                      dismiss();
                    }));

    binding.selectShare.setOnClickListener(
        v -> {
          Glide.with(getActivity())
              .asBitmap()
              .load(photo.getWebformatURL())
              .listener(
                  new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(
                        @Nullable GlideException e,
                        Object model,
                        Target<Bitmap> target,
                        boolean isFirstResource) {
                      Toast.makeText(
                              ActionBottomSheetFragment.this.mContext,
                              R.string.cant_share_until_image_is_loaded,
                              Toast.LENGTH_SHORT)
                          .show();
                      return false;
                    }

                    @Override
                    public boolean onResourceReady(
                        Bitmap bitmap,
                        Object model,
                        Target<Bitmap> target,
                        DataSource dataSource,
                        boolean isFirstResource) {
                      startSharing(
                          imageSaveUtils.getLocalBitmapUri(
                              bitmap, ActionBottomSheetFragment.this.mContext));

                      return true;
                    }
                  })
              .submit();
          dismiss();
        });
  }

  private void startSharing(Uri localBitmapUri) {
    Activity activity = getActivity();
    if (activity != null && isAdded()) {
      final Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType(getString(R.string.image_jpg));
      shareIntent.putExtra(Intent.EXTRA_STREAM, localBitmapUri);
      startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)));
    }
    dismiss();
  }

  private Photo getPhoto() {
    if (getArguments().getParcelable(SINGLE_PHOTO) != null) {
      return getArguments().getParcelable(SINGLE_PHOTO);
    } else {
      return null;
    }
  }
}
