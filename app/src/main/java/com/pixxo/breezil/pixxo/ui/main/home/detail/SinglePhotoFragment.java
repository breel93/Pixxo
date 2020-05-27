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

import static com.pixxo.breezil.pixxo.utils.Constant.FIRST_TYPE;
import static com.pixxo.breezil.pixxo.utils.Constant.SINGLE_PHOTO;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSinglePhotoBinding;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
@SuppressLint("RestrictedApi")
public class SinglePhotoFragment extends DaggerFragment {

  private FragmentSinglePhotoBinding binding;

  @Inject ViewModelProvider.Factory viewModelFactory;
  private DetailViewModel viewModel;

  private ActionBottomSheetFragment actionBottomSheetFragment;

  public SinglePhotoFragment() {
    // Required empty public constructor
  }

  public static SinglePhotoFragment getPhoto(Photo photo) {
    SinglePhotoFragment fragment = new SinglePhotoFragment();
    Bundle args = new Bundle();
    args.putParcelable(SINGLE_PHOTO, photo);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_photo, container, false);
    binding.closeSingleImage.setOnClickListener(v -> getActivity().onBackPressed());
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailViewModel.class);
  }

  @Override
  public void onStart() {
    super.onStart();
    viewModel.setImage(getPhoto());
    viewModel.getImage().observe(getViewLifecycleOwner(), this::updateUI);
  }

  private void updateUI(Photo photo) {
    if (!isAdded()) {
      return;
    }
    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
    circularProgressDrawable.setStrokeWidth(15f);
    circularProgressDrawable.setCenterRadius(60f);
    circularProgressDrawable.setColorSchemeColors(
        R.color.colorAccent, R.color.colorPrimary, R.color.colorblue, R.color.hotPink);
    circularProgressDrawable.start();
    Glide.with(this)
        .load(photo.getWebformatURL())
        .apply(
            new RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.placeholder))
        .into(binding.singleCardImage);

    binding.detailFloatBtn.setOnClickListener(
        v -> {
          actionBottomSheetFragment = ActionBottomSheetFragment.getPhoto(photo, FIRST_TYPE);
          actionBottomSheetFragment.show(getChildFragmentManager(), getString(R.string.get));
        });
    binding.singleImageTitle.setText(photo.getTags());
  }

  private Photo getPhoto() {
    if (getArguments().getParcelable(SINGLE_PHOTO) != null) {
      return getArguments().getParcelable(SINGLE_PHOTO);
    } else {
      return null;
    }
  }
}
