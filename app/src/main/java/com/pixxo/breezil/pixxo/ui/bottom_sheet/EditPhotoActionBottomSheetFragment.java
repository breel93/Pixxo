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

import static com.pixxo.breezil.pixxo.utils.Constant.EDITED_IMAGE_URI_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentEditPhotoActionBottomSheetBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import com.pixxo.photoeditor.EditImageActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;

/** A simple {@link Fragment} subclass. */
public class EditPhotoActionBottomSheetFragment extends BottomSheetDialogFragment {
  private FragmentEditPhotoActionBottomSheetBinding binding;

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
    updateUi(getPhoto());
    //    Toast.makeText(getContext(), String.valueOf(getPhoto().getPath()),
    // Toast.LENGTH_LONG).show();
    return binding.getRoot();
  }

  private void updateUi(EditedPhoto photo) {
    edit(photo);
    share(photo);
    delete(photo);
  }

  private void delete(EditedPhoto photo) {
    binding.selectDelete.setOnClickListener(
        v -> {
          File file = new File(photo.getPath());
          if (file.exists()) {
            file.delete();
          }
        });
  }

  private void edit(EditedPhoto photo) {
    binding.selectEdit.setOnClickListener(
        v -> {
          Intent editIntent = new Intent(getContext(), EditImageActivity.class);
          editIntent.putExtra(EDITED_IMAGE_URI_STRING, String.valueOf(photo.getPath()));
          getContext().startActivity(editIntent);
        });
  }

  private void share(EditedPhoto photo) {
    binding.selectShare.setOnClickListener(
        v -> {
          Uri uri = getImageUri(getContext(), photo.getImage());
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

  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path =
        MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(), inImage, getString(R.string.title), null);
    return Uri.parse(path);
  }
}
