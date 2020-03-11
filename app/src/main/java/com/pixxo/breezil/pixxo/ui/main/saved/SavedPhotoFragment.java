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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSavedPhotoBinding;
import com.pixxo.breezil.pixxo.ui.adapter.SavedPhotoRecyclerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.SavedActionBottomSheetFragment;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SinglePhotoFragment;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
public class SavedPhotoFragment extends DaggerFragment {

  private FragmentSavedPhotoBinding binding;

  @Inject ViewModelFactory viewModelFactory;

  private SavedPhotosViewModel viewModel;
  private SavedPhotoRecyclerAdapter savedPhotoRecyclerAdapter;

  public SavedPhotoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_photo, container, false);

    binding.savedList.setHasFixedSize(true);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(SavedPhotosViewModel.class);
    setUpAdapter();
    setUpViewModel();
    return binding.getRoot();
  }
  //  ParentFragment.this.getChildFragmentManager()

  private void setUpAdapter() {
    PhotoClickListener photoClickListener =
        photo -> {
          SinglePhotoFragment fragment = SinglePhotoFragment.getPhoto(photo);
          getParentFragmentManager()
              .beginTransaction()
              .setCustomAnimations(
                  R.anim.fragment_slide_in,
                  R.anim.fragment_slide_out,
                  R.anim.fragment_pop_slide_in,
                  R.anim.fragment_pop_slide_out)
              .replace(R.id.parent_container, fragment)
                  .hide(this)
              .addToBackStack("")
              .commit();
        };

    PhotoLongClickListener photoLongClickListener =
        photo -> {
          SavedActionBottomSheetFragment savedActionBottomSheetFragment =
              SavedActionBottomSheetFragment.getSavedPhoto(photo);
          savedActionBottomSheetFragment.show(
              getFragmentManager(), getString(R.string.do_something));
        };

    savedPhotoRecyclerAdapter =
        new SavedPhotoRecyclerAdapter(getContext(), photoLongClickListener, photoClickListener);

    binding.savedList.setHasFixedSize(true);
    binding.savedList.setAdapter(savedPhotoRecyclerAdapter);
  }

  private void setUpViewModel() {
    viewModel
        .getSavedPhoto()
        .observe(getViewLifecycleOwner(), photos -> savedPhotoRecyclerAdapter.submitList(photos));
  }

  //  @Override
  //  public void onPause() {
  //    super.onPause();
  //
  //    for ( Fragment f : getChildFragmentManager().getFragments() ) {
  //      if ( f instanceof SavedPhotoFragment) {
  //        getChildFragmentManager().beginTransaction().remove( f ).commit();
  //      }
  //    }
  //
  //  }
}
