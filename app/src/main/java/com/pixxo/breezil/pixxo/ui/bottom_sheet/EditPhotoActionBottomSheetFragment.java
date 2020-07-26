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

import static android.app.Activity.RESULT_OK;
import static com.pixxo.breezil.pixxo.utils.Constant.EDITED_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.EDIT_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentEditPhotoActionBottomSheetBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import com.pixxo.breezil.pixxo.ui.ImageSaveUtils;
import com.pixxo.breezil.pixxo.ui.main.saved.EditedPhotoViewModel;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotosViewModel;
import com.pixxo.photoeditor.EditImageActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/** A simple {@link Fragment} subclass. */
public class EditPhotoActionBottomSheetFragment extends BottomSheetDialogFragment {

  private FragmentEditPhotoActionBottomSheetBinding binding;
  private EditedPhotoViewModel viewModel;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  public static EditPhotoActionBottomSheetFragment getPhoto(EditedPhoto photo) {
    EditPhotoActionBottomSheetFragment fragment = new EditPhotoActionBottomSheetFragment();
    Bundle args = new Bundle();
    args.putParcelable(SINGLE_PHOTO, photo);
    fragment.setArguments(args);
    return fragment;
  }


  public EditPhotoActionBottomSheetFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding =
        DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_photo_action_bottom_sheet, container, false);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(EditedPhotoViewModel.class);
    updateUi(getPhoto());
    return binding.getRoot();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    AndroidSupportInjection.inject(this);
  }

  private void updateUi(EditedPhoto photo) {
    share(photo);
  }

  private void share(EditedPhoto photo) {
    binding.selectShare.setOnClickListener(
        v -> {
          Uri uri = getImageUri(requireContext(), photo.getImage());
          startSharing(uri);
        });
  }

  private EditedPhoto getPhoto() {
    if (getArguments().getParcelable(SINGLE_PHOTO) != null) {
      return getArguments().getParcelable(SINGLE_PHOTO);
    } else {
      return null;
    }
  }

  private void startSharing(Uri localBitmapUri) {
    final Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType(getString(R.string.image_jpg));
    shareIntent.putExtra(Intent.EXTRA_STREAM, localBitmapUri);
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)));
  }

  public Uri getImageUri(Context context, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path =
        MediaStore.Images.Media.insertImage(
            context.getContentResolver(), inImage, getString(R.string.title), null);
    return Uri.parse(path);
  }

}
