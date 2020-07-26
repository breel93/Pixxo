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
import android.util.Log;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentActionBottomSheetBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.ui.ImageSaveUtils;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotosViewModel;
import com.pixxo.photoeditor.EditImageActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static android.app.Activity.RESULT_OK;
import static com.pixxo.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.FIRST_TYPE;
import static com.pixxo.breezil.pixxo.utils.Constant.SAVED_TYPE;
import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActionBottomSheetFragment extends BottomSheetDialogFragment {

  private FragmentActionBottomSheetBinding binding;

  private ImageSaveUtils imageSaveUtils;

  private SavedPhotosViewModel viewModel;

  private ProgressDialog mProgress;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  public static ActionBottomSheetFragment getPhoto(Photo photo, String type) {
    ActionBottomSheetFragment fragment = new ActionBottomSheetFragment();
    Bundle args = new Bundle();
    args.putParcelable(SINGLE_PHOTO, photo);
    args.putString(TYPE, type);
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
    imageSaveUtils = new ImageSaveUtils(requireContext());
    mProgress = new ProgressDialog(requireContext());
    viewModel = new ViewModelProvider(this, viewModelFactory).get(SavedPhotosViewModel.class);
    if (getType().equals(FIRST_TYPE)) {
      binding.selectDelete.setVisibility(View.GONE);
    } else if (getType().equals(SAVED_TYPE)) {
      binding.selectSaved.setVisibility(View.GONE);
    }
    updateUi(getPhoto());
    return binding.getRoot();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    AndroidSupportInjection.inject(this);
  }

  private void updateUi(Photo photo) {
    edit(photo);
    download(photo);
    saved(photo);
    delete(photo);
    share(photo);
  }

  private void share(Photo photo) {
    binding.selectShare.setOnClickListener(
        v -> Glide.with(this)
            .asBitmap()
            .load(photo.getWebformatURL())
            .into(new CustomTarget<Bitmap>() {
              @Override
              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                startSharing(
                    imageSaveUtils.getLocalBitmapUri(
                        resource, requireContext()));
                dismiss();
              }

              @Override
              public void onLoadCleared(@Nullable Drawable placeholder) {

              }
            }));
  }

  private void delete(Photo photo) {
    binding.selectDelete.setOnClickListener(
        v ->
            viewModel
                .deletePhoto(photo)
                .observe(
                    getViewLifecycleOwner(),
                    s -> {
                      Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show();
                      dismiss();
                    }));
  }

  private void saved(Photo photo) {
    binding.selectSaved.setOnClickListener(
        v ->
            viewModel
                .insertPhoto(photo)
                .observe(
                    getViewLifecycleOwner(),
                    s -> {
                      Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show();
                      dismiss();
                    }));
  }

  private void download(Photo photo) {
    binding.selectDownload.setOnClickListener(
        v -> Glide.with(this)
            .asBitmap()
            .load(photo.getWebformatURL())
            .into(new CustomTarget<Bitmap>() {
              @Override
              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                  mProgress.setTitle(requireContext().getString(R.string.downloading));
                  mProgress.setMessage(
                      requireContext().getString(
                          R.string.please_wait_image_is_downloading));
                  mProgress.setCancelable(false);
                  mProgress.show();
                  Handler handler = new Handler();
                  handler.postDelayed(
                      () -> {
                        imageSaveUtils.saveImage(
                            requireContext(), resource, getString(R.string._slash_pixxo));
                        mProgress.dismiss();
                        Toast.makeText(
                            requireContext(),
                            R.string.downloaded,
                            Toast.LENGTH_SHORT)
                            .show();
                        dismiss();
                      },
                      1000);
                } else {
                  ActivityCompat.requestPermissions(
                      requireActivity(),
                      new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                      STORAGE_PERMISSION_CODE);
                  dismiss();
                }
              }

              @Override
              public void onLoadCleared(@Nullable Drawable placeholder) {
              }
            }));
  }

  private void edit(Photo photo) {
    binding.selectEdit.setOnClickListener(
        v -> Glide.with(this)
            .asBitmap()
            .load(photo.getWebformatURL())
            .into(new CustomTarget<Bitmap>() {
              @Override
              public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                startEdit(resource);
              }

              @Override
              public void onLoadCleared(@Nullable Drawable placeholder) {

              }
            }));
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

  private String getType() {
    if (getArguments().getString(TYPE) != null) {
      return getArguments().getString(TYPE);
    } else {
      return null;
    }
  }

  private void startEdit(Bitmap bitmap){
    Uri uri = imageSaveUtils.getLocalBitmapUri(
        bitmap, requireContext());
    try {
      CropImage.activity(uri).start(requireContext(),this);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // copied from Aurthur Edmondo github for crop action
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        Uri imageUri = result.getUri();
        if(imageUri != null){
          Intent editImageIntent = new Intent(getContext(), EditImageActivity.class);
          editImageIntent.putExtra(EDIT_IMAGE_URI_STRING, String.valueOf(imageUri));
          startActivity(editImageIntent);
          dismiss();
        }
      }
    }
  }

}
