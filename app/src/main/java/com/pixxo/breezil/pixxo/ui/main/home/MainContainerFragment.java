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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentMainContainerBinding;
import com.pixxo.breezil.pixxo.ui.adapter.PagerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChoosePhotoBottomDialogFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SaveAndEditFragment;
import com.pixxo.breezil.pixxo.ui.settings.SettingsActivity;
import com.pixxo.breezil.pixxo.utils.BottomNavigationHelper;
import dagger.android.support.DaggerFragment;
import java.util.Objects;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/** A simple {@link Fragment} subclass. */
public class MainContainerFragment extends DaggerFragment {

  private FragmentMainContainerBinding binding;

  private ChoosePhotoBottomDialogFragment choosePhotoBottomDialogFragment =
      new ChoosePhotoBottomDialogFragment();
  private PagerAdapter pagerAdapter;

  public MainContainerFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_container, container, false);
    pagerAdapter = new PagerAdapter(getChildFragmentManager());

    setupBottomNavigation();

    HttpLoggingInterceptor logging =
        new HttpLoggingInterceptor(message -> Timber.tag(getString(R.string.okhttp)).d(message));
    logging.redactHeader(getString(R.string.authorization));
    logging.redactHeader(getString(R.string.cookie));
    binding.addButton.setOnClickListener(
        v ->
            choosePhotoBottomDialogFragment.show(
                getParentFragmentManager(), getString(R.string.choose_image)));
    return binding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    // set the menu layout
    inflater.inflate(R.menu.main_menu, menu);
  }

  @Override
  public void onResume() {
    super.onResume();
    setupBottomNavigation();
  }

  @Override
  public void onStart() {
    super.onStart();
    setupBottomNavigation();
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      Objects.requireNonNull(getActivity()).onBackPressed();
      return true;
    }

    if (item.getItemId() == R.id.preference) {
      Intent trendIntent = new Intent(getContext(), SettingsActivity.class);
      startActivity(trendIntent);
    }

    return true;
  }

  private void setupBottomNavigation() {

    pagerAdapter.addFragments(new MainFragment());
    pagerAdapter.addFragments(new SaveAndEditFragment());
    binding.mainViewPager.setAdapter(pagerAdapter);
    binding.mainViewPager.setOffscreenPageLimit(1);

    BottomNavigationHelper.disableShiftMode(binding.bottomNavViewBar);
    Menu menu = binding.bottomNavViewBar.getMenu();
    MenuItem menuItem = menu.getItem(0);
    menuItem.setChecked(true);
    /*
     * here sets the navigations to its corresponding activities
     */
    binding.bottomNavViewBar.setOnNavigationItemSelectedListener(
        item -> {
          loadFragmentBottomNav(item);
          return true;
        });
  }

  private void loadFragmentBottomNav(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.trending:
        binding.mainViewPager.setCurrentItem(0, true);
        break;
      case R.id.saved:
        binding.mainViewPager.setCurrentItem(1, true);
        break;
    }
  }
}
