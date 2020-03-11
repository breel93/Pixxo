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
package com.pixxo.breezil.pixxo.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.ActivityPreferenceBinding;
import com.pixxo.breezil.pixxo.ui.BaseActivity;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import javax.inject.Inject;

public class SettingsActivity extends BaseActivity {

  @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    ActivityPreferenceBinding binding =
        DataBindingUtil.setContentView(this, R.layout.activity_preference);

    binding.aboutText.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));

    Toolbar mToolbar = findViewById(R.id.pixxoToolBar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle(getString(R.string.settings));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return false;
  }
}
