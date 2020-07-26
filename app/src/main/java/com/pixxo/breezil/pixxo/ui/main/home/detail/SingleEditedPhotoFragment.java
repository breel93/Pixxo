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
package com.pixxo.breezil.pixxo.ui.main.home.detail;

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
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSingleEditedPhotoBinding;
import com.pixxo.breezil.pixxo.model.EditedPhoto;

import java.io.ByteArrayOutputStream;

import dagger.android.support.DaggerFragment;

/** A simple {@link Fragment} subclass. */
public class SingleEditedPhotoFragment extends DaggerFragment {

  private FragmentSingleEditedPhotoBinding binding;
  SingleEditedPhotoFragmentListener openedlistener;

  public interface SingleEditedPhotoFragmentListener {
    void isOpened(Boolean opened);
  }
  public SingleEditedPhotoFragment() {
    // Required empty public constructor
  }

  public static SingleEditedPhotoFragment getPhoto(EditedPhoto photo) {
    SingleEditedPhotoFragment fragment = new SingleEditedPhotoFragment();
    Bundle args = new Bundle();
    args.putParcelable(SINGLE_PHOTO, photo);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_single_edited_photo, container, false);
    binding.closeSingleImage.setOnClickListener(v -> requireActivity().onBackPressed());
    openedlistener.isOpened(true);
    updateUi(getPhoto());
    return binding.getRoot();
  }
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      openedlistener = (SingleEditedPhotoFragment.SingleEditedPhotoFragmentListener) requireActivity();
    }catch (ClassCastException e){
      throw new ClassCastException(context.toString()
          + " must implement SinglePhotoFragmentListener ");
    }
  }

  private void updateUi(EditedPhoto photo) {
    if (!isAdded()) return;
    binding.singleCardImage.setImageBitmap(photo.getImage());
    share(photo);
  }

  private EditedPhoto getPhoto() {
    if (getArguments().getParcelable(SINGLE_PHOTO) != null) {
      return getArguments().getParcelable(SINGLE_PHOTO);
    } else {
      return null;
    }
  }

  private void share(EditedPhoto photo) {
    binding.shareIcon.setOnClickListener(
        v -> {
          Uri uri = getImageUri(requireContext(), photo.getImage());
          startSharing(uri);
        });
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
  @Override
  public void onDetach() {
    super.onDetach();
    openedlistener.isOpened(false);
  }
}
