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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ActivityMainBinding;
import com.pixxo.breezil.pixxo.ui.BaseActivity;
import com.pixxo.breezil.pixxo.ui.adapter.PagerAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChoosePhotoBottomDialogFragment;
import com.pixxo.breezil.pixxo.ui.callbacks.SinglePhotoFragmentOpenedListener;
import com.pixxo.breezil.pixxo.ui.main.home.MainFragment;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SingleEditedPhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SinglePhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SaveAndEditFragment;
import com.pixxo.breezil.pixxo.ui.settings.PreferenceFragment;
import com.pixxo.breezil.pixxo.utils.BottomNavigationHelper;
import com.pixxo.breezil.pixxo.utils.ConnectionUtils;
import com.pixxo.breezil.pixxo.utils.helper.FadeOutTransformation;

import java.lang.reflect.Field;

import javax.inject.Inject;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import static com.mikepenz.iconics.Iconics.getApplicationContext;

@SuppressLint("RestrictedApi")
public class MainActivity extends BaseActivity implements
    SinglePhotoFragmentOpenedListener{
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

  @Override
  public void isOpened(Boolean opened) {
    Transition transition = new Slide(Gravity.START);
    transition.addTarget(binding.mainContainer);
    TransitionManager.beginDelayedTransition(binding.parentContainer, transition);
    binding.mainContainer.setVisibility(opened ? View.GONE : View.VISIBLE);
  }


}
