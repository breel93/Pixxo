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
package com.pixxo.breezil.pixxo.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.databinding.DataBindingUtil;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ActivityMainBinding;
import com.pixxo.breezil.pixxo.ui.BaseActivity;
import com.pixxo.breezil.pixxo.ui.adapter.PagerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChoosePhotoBottomDialogFragment;
import com.pixxo.breezil.pixxo.ui.main.home.MainFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SaveAndEditFragment;
import com.pixxo.breezil.pixxo.utils.BottomNavigationHelper;
import com.pixxo.breezil.pixxo.utils.ConnectionUtils;
import com.pixxo.breezil.pixxo.utils.helper.FadeOutTransformation;
import javax.inject.Inject;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@SuppressLint("RestrictedApi")
public class MainActivity extends BaseActivity {

  public ActivityMainBinding binding;
  private ChoosePhotoBottomDialogFragment choosePhotoBottomDialogFragment =
      new ChoosePhotoBottomDialogFragment();
  private PagerAdapter pagerAdapter;

  @Inject ConnectionUtils connectionUtils;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    pagerAdapter = new PagerAdapter(getSupportFragmentManager());

    setupBottomNavigation();
    HttpLoggingInterceptor logging =
        new HttpLoggingInterceptor(
            message -> {
              Timber.tag(getString(R.string.okhttp)).d(message);
            });
    logging.redactHeader(getString(R.string.authorization));
    logging.redactHeader(getString(R.string.cookie));
    binding.addButton.setOnClickListener(
        v ->
            choosePhotoBottomDialogFragment.show(
                getSupportFragmentManager(), getString(R.string.choose_image)));
  }

  private void setupBottomNavigation() {

    pagerAdapter.addFragments(new MainFragment());
    pagerAdapter.addFragments(new SaveAndEditFragment());
    binding.mainViewPager.setAdapter(pagerAdapter);
    binding.mainViewPager.setOffscreenPageLimit(1);
    binding.mainViewPager.setPageTransformer(false, new FadeOutTransformation());

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
