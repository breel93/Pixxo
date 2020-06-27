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
package com.pixxo.breezil.pixxo.ui.main.home;

import static com.pixxo.breezil.pixxo.utils.Constant.FIRST_TYPE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentMainBinding;
import com.pixxo.breezil.pixxo.ui.adapter.PhotoRecyclerViewAdapter;
import com.pixxo.breezil.pixxo.ui.adapter.QuickSelectRecyclerListAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.QuickSelectListener;
import com.pixxo.breezil.pixxo.ui.main.MainViewModel;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SinglePhotoFragment;
import com.pixxo.breezil.pixxo.ui.settings.SettingsFragment;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import dagger.android.support.DaggerFragment;
import java.util.Arrays;
import java.util.Collections;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
public class MainFragment extends DaggerFragment {

  @Inject ViewModelFactory viewModelFactory;

  private MainViewModel viewModel;
  private FragmentMainBinding binding;

  private PhotoRecyclerViewAdapter adapter;

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
    viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);

    upDateAdapter();
    updateUi();
    updateQuickSearch();
    doSearch();
    gotoOptionSelect();
    return binding.getRoot();
  }

  private void upDateAdapter() {
    binding.mainRecyclerView.hasFixedSize();
    PhotoClickListener photoClickListener =
        photo -> {
          SinglePhotoFragment fragment = SinglePhotoFragment.getPhoto(photo);
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

    PhotoLongClickListener photoLongClickListener =
        photo -> {
          ActionBottomSheetFragment actionBottomSheetFragment =
              ActionBottomSheetFragment.getPhoto(photo, FIRST_TYPE);
          actionBottomSheetFragment.show(
              getChildFragmentManager(), getString(R.string.do_something));
        };

    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    binding.photoRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    adapter =
        new PhotoRecyclerViewAdapter(getActivity(), photoClickListener, photoLongClickListener);
    binding.photoRecyclerView.hasFixedSize();
    binding.photoRecyclerView.setAdapter(adapter);
  }

  private void updateUi() {
    viewModel.setParameter(
        getString(R.string.blank),
        getString(R.string.blank),
        getString(R.string.en),
        getString(R.string.random));
    viewModel
        .getPhotoList()
        .observe(
            getViewLifecycleOwner(),
            photos -> {
              adapter.submitList(photos);
            });

    viewModel
        .getNetworkState()
        .observe(
            getViewLifecycleOwner(),
            networkState -> {
              if (networkState != null) {
                adapter.setNetworkState(networkState);
              }
            });
  }

  private void doSearch() {
    binding.searchBar.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            if (!query.isEmpty()) {
              search(query);
            } else {
              refresh();
            }
            return true;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            return false;
          }
        });

  }

  private void updateQuickSearch() {
    String[] quickSelectName = getResources().getStringArray(R.array.search_list);
    String[] quickSelectColor = getResources().getStringArray(R.array.color_list);

    Collections.shuffle(Arrays.asList(quickSelectName));

    LinearLayoutManager layoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

    QuickSelectListener quickSelectListener =
        string -> {
          SingleListFragment fragment = SingleListFragment.getCategory(string);
          getFragmentManager()
              .beginTransaction()
              .setCustomAnimations(
                  R.anim.fragment_slide_in,
                  R.anim.fragment_slide_out,
                  R.anim.fragment_pop_slide_in,
                  R.anim.fragment_pop_slide_out)
              .add(R.id.parent_container, fragment)
              .addToBackStack("single_list")
              .hide(this)
              .commit();
        };
    QuickSelectRecyclerListAdapter quickSelectRecyclerListAdapter =
        new QuickSelectRecyclerListAdapter(quickSelectName, quickSelectColor, quickSelectListener);
    binding.mainRecyclerView.setLayoutManager(layoutManager);
    binding.mainRecyclerView.setAdapter(quickSelectRecyclerListAdapter);
  }

  private void search(String search) {
    viewModel.setParameter(
        search, getString(R.string.blank), getString(R.string.en), getString(R.string.random));

    viewModel
        .refreshPhotos()
        .observe(
            this,
            photos -> {
              adapter.submitList(photos);
            });
    viewModel
        .getNetworkState()
        .observe(
            this,
            networkState -> {
              if (networkState != null) {
                adapter.setNetworkState(networkState);
              }
            });
  }

  private void refresh() {
    viewModel.setParameter(
        getString(R.string.blank),
        getString(R.string.blank),
        getString(R.string.en),
        getString(R.string.random));

    viewModel.refreshPhotos().observe(this, photos -> adapter.submitList(photos));
    viewModel
        .getNetworkState()
        .observe(
            this,
            networkState -> {
              if (networkState != null) {
                adapter.setNetworkState(networkState);
              }
            });
  }

  private void gotoOptionSelect() {
    binding.gotoPreference.setOnClickListener(
        v -> {
          SettingsFragment fragment = new SettingsFragment();
          getFragmentManager()
              .beginTransaction()
              .setCustomAnimations(
                  R.anim.fragment_slide_in,
                  R.anim.fragment_slide_out,
                  R.anim.fragment_pop_slide_in,
                  R.anim.fragment_pop_slide_out)
              .add(R.id.parent_container, fragment)
              .addToBackStack("settings")
              .hide(this)
              .commit();
        });
  }
}
