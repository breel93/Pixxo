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
package com.pixxo.breezil.pixxo.ui.main.saved;

import static com.pixxo.breezil.pixxo.utils.Constant.PHOTO_STRING;
import static com.pixxo.breezil.pixxo.utils.Constant.SAVED_TYPE;
import static com.pixxo.breezil.pixxo.utils.Constant.TYPE;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSavedPhotoDialogBinding;

/** A simple {@link Fragment} subclass. */
public class SavedPhotoDialogFragment extends AppCompatDialogFragment {

  private FragmentSavedPhotoDialogBinding binding;

  public static SavedPhotoDialogFragment getImageString(String imageString, String type) {
    SavedPhotoDialogFragment fragment = new SavedPhotoDialogFragment();
    Bundle args = new Bundle();
    args.putString(PHOTO_STRING, imageString);
    args.putString(TYPE, type);
    fragment.setArguments(args);
    return fragment;
  }

  public SavedPhotoDialogFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_saved_photo_dialog, container, false);
    String type = getArguments().getString(TYPE);
    if (type != null) {
      updateUI(getImageString(), type);
    }

    return binding.getRoot();
  }

  private void updateUI(String imageString, String type) {

    if (type.equals(SAVED_TYPE)) {
      Glide.with(this)
          .load(imageString)
          .apply(
              new RequestOptions()
                  .placeholder(R.drawable.placeholder)
                  .error(R.drawable.placeholder))
          .into(binding.dialogImage);
    } else {
      binding.dialogImage.setImageURI(Uri.parse(imageString));
    }
  }

  private String getImageString() {

    if (getArguments().getString(PHOTO_STRING) != null) {
      return getArguments().getString(PHOTO_STRING);
    } else {
      return null;
    }
  }
}
