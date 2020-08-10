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

import static com.pixxo.breezil.pixxo.utils.Constant.CATEGORY;
import static com.pixxo.breezil.pixxo.utils.Constant.FIRST_TYPE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSingleListBinding;
import com.pixxo.breezil.pixxo.repository.NetworkState;
import com.pixxo.breezil.pixxo.ui.adapter.PhotoRecyclerViewAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ActionBottomSheetFragment;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.PhotoLongClickListener;
import com.pixxo.breezil.pixxo.ui.callbacks.RetryListener;
import com.pixxo.breezil.pixxo.ui.main.MainViewModel;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SinglePhotoFragment;
import com.pixxo.breezil.pixxo.utils.ConnectionUtils;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import dagger.android.support.DaggerFragment;
import javax.inject.Inject;

/** A simple {@link Fragment} subclass. */
public class SingleListFragment extends DaggerFragment implements RetryListener {

  private FragmentSingleListBinding binding;
  private MainViewModel viewModel;
  private PhotoRecyclerViewAdapter photoRecyclerViewAdapter;

  @Inject ViewModelFactory viewModelFactory;

  @Inject ConnectionUtils connectionUtils;

  static SingleListFragment getCategory(String category) {
    SingleListFragment fragment = new SingleListFragment();
    Bundle args = new Bundle();
    args.putString(CATEGORY, category);
    fragment.setArguments(args);
    return fragment;
  }

  public SingleListFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_single_list, container, false);

    viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
    ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(getCategory());
    ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setUpAdapter();
    setUpViewModel();
    setHasOptionsMenu(true);
    binding.swipeRefresh.setOnRefreshListener(this::refresh);
    return binding.getRoot();
  }

  private void setUpAdapter() {
    binding.shimmerViewContainer.startShimmer();
    binding.shimmerViewContainer.setVisibility(View.VISIBLE);
    PhotoClickListener photoClickListener =
        photo -> {
          SinglePhotoFragment fragment = SinglePhotoFragment.getPhoto(photo, FIRST_TYPE);
          requireActivity().getSupportFragmentManager()
              .beginTransaction()
              .setCustomAnimations(
                  R.anim.fragment_slide_in,
                  R.anim.fragment_slide_out,
                  R.anim.fragment_pop_slide_in,
                  R.anim.fragment_pop_slide_out)
              .replace(R.id.parent_container, fragment)
              .addToBackStack("fragment")
              .commit();
        };
    PhotoLongClickListener photoLongClickListener =
        photo -> {
          ActionBottomSheetFragment actionBottomSheetFragment =
              ActionBottomSheetFragment.getPhoto(photo, FIRST_TYPE);
          actionBottomSheetFragment.show(requireActivity().getSupportFragmentManager(), getString(R.string.do_something));
        };
    photoRecyclerViewAdapter =
        new PhotoRecyclerViewAdapter(getContext(), photoClickListener, photoLongClickListener);
    binding.imageList.hasFixedSize();
    binding.imageList.setAdapter(photoRecyclerViewAdapter);
  }

  private void setUpViewModel() {
    binding.swipeRefresh.setVisibility(View.VISIBLE);
    binding.swipeRefresh.setColorSchemeResources(
        R.color.colorAccent, R.color.colorPrimary, R.color.colorblue, R.color.hotPink);
    viewModel.setNetworkState();
    setupLoading();
    viewModel.setParameter("", getCategory(), getString(R.string.en), "");
    viewModel
        .getPhotoList()
        .observe(
            getViewLifecycleOwner(),
            photo -> photoRecyclerViewAdapter.submitList(photo));

    if (binding.swipeRefresh != null) {
      binding.swipeRefresh.setRefreshing(false);
    }
  }

  private void refresh() {
    viewModel.setNetworkState();
    setupLoading();
    viewModel.setParameter("", getCategory(), getString(R.string.en), "");
    viewModel
        .refreshPhotos()
        .observe(getViewLifecycleOwner(), photo -> photoRecyclerViewAdapter.submitList(photo));
    if (binding.swipeRefresh != null) {
      binding.swipeRefresh.setRefreshing(false);
    }
  }

  private String getCategory() {
    if (requireArguments().getString(CATEGORY) != null) {
      return requireArguments().getString(CATEGORY);
    } else {
      return null;
    }
  }

  private void setupLoading(){
    viewModel.getInitialLoading().observe(getViewLifecycleOwner(), networkState -> {
      if(networkState != null){
        if(networkState.getStatus() == NetworkState.Status.SUCCESS){
          binding.shimmerViewContainer.setVisibility(View.GONE);
          binding.searchError.setVisibility(View.GONE);
          binding.responseError.setVisibility(View.GONE);
        }else if(networkState.getStatus() == NetworkState.Status.FAILED){
          binding.shimmerViewContainer.setVisibility(View.GONE);
          binding.searchError.setVisibility(View.GONE);
          binding.responseError.setVisibility(View.VISIBLE);
        }else if(networkState.getStatus() == NetworkState.Status.NO_RESULT){
          binding.shimmerViewContainer.setVisibility(View.GONE);
          binding.searchError.setVisibility(View.VISIBLE);
          binding.responseError.setVisibility(View.GONE);
        }else{
          binding.shimmerViewContainer.setVisibility(View.VISIBLE);
          binding.searchError.setVisibility(View.GONE);
          binding.responseError.setVisibility(View.GONE);
        }
      }
    });
    viewModel.getNetworkState().observe(getViewLifecycleOwner(), networkState -> {
      if(networkState != null){
        photoRecyclerViewAdapter.setNetworkState(networkState);
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      requireActivity().onBackPressed();
      return true;
    }
    return false;
  }

  @Override
  public void onRefresh() {
    if (connectionUtils.sniff()) {
      refresh();
    } else {
      Toast.makeText(requireContext(), getString(R.string.no_active_internet), Toast.LENGTH_LONG)
          .show();
    }
  }
}
