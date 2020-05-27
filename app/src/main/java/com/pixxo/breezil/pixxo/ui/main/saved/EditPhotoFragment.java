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

import static com.pixxo.breezil.pixxo.utils.Constant.PIXXO_EDITED;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentEditPhotoBinding;
import com.pixxo.breezil.pixxo.ui.adapter.EditPhotoRecyclerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChoosePhotoBottomDialogFragment;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.EditPhotoActionBottomSheetFragment;
import com.pixxo.breezil.pixxo.ui.callbacks.EditedPhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.EditedPhotoLongClickListener;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SingleEditedPhotoFragment;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import dagger.android.support.DaggerFragment;
import java.util.Collections;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
public class EditPhotoFragment extends DaggerFragment {

  private ChoosePhotoBottomDialogFragment choosePhotoBottomDialogFragment =
      new ChoosePhotoBottomDialogFragment();

  private FragmentEditPhotoBinding binding;
  private EditPhotoRecyclerAdapter adapter;
  @Inject ViewModelFactory viewModelFactory;
  private EditedPhotoViewModel viewModel;

  public EditPhotoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_photo, container, false);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(EditedPhotoViewModel.class);
    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {
      if (PIXXO_EDITED != null) {
        setGridAdapter();
        updateViewModel();
      }
    } else {
      ActivityCompat.requestPermissions(
          getActivity(),
          new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
          STORAGE_PERMISSION_CODE);
    }
    return binding.getRoot();
  }

  private void setGridAdapter() {
    EditedPhotoClickListener photoClickListener =
        editedPhoto -> {
          SingleEditedPhotoFragment fragment = SingleEditedPhotoFragment.getPhoto(editedPhoto);
          getFragmentManager()
              .beginTransaction()
              .setCustomAnimations(
                  R.anim.fragment_slide_in,
                  R.anim.fragment_slide_out,
                  R.anim.fragment_pop_slide_in,
                  R.anim.fragment_pop_slide_out)
              .add(R.id.parent_container, fragment)
              .hide(this)
              .addToBackStack("fragment")
              .commit();
        };
    EditedPhotoLongClickListener photoLongClickListener =
        editedPhoto -> {
          EditPhotoActionBottomSheetFragment fragment =
              EditPhotoActionBottomSheetFragment.getPhoto(editedPhoto);
          fragment.show(getFragmentManager(), getString(R.string.do_something));
        };
    adapter = new EditPhotoRecyclerAdapter(photoClickListener, photoLongClickListener);
    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    binding.editedPhotoRecyclerView.setLayoutManager(staggeredGridLayoutManager);
  }

  private void updateViewModel() {
    viewModel
        .getEditedPhotos(getContext(), PIXXO_EDITED)
        .observe(
            getViewLifecycleOwner(),
            editedPhotos -> {
              if (editedPhotos.size() > ZERO) {
                binding.editedPhotoRecyclerView.setAdapter(adapter);
                Collections.reverse(editedPhotos);
                adapter.submitList(editedPhotos);
              } else {
                binding.emptyText.setVisibility(View.VISIBLE);
                binding.clickToEditbtn.setVisibility(View.VISIBLE);
                binding.clickToEditbtn.setOnClickListener(
                    v ->
                        choosePhotoBottomDialogFragment.show(
                            getFragmentManager(), getString(R.string.choose_image)));
              }
            });
  }
}
