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
package com.pixxo.breezil.pixxo.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.pixxo.breezil.pixxo.R;
import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity {
  private SharedPreferences sharedPreferences;

  boolean themeMode = false;

  @SuppressLint("SourceLockedOrientationActivity")
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    themeMode = sharedPreferences.getBoolean(getString(R.string.pref_theme_key), false);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    setAppTheme(themeMode);
  }

  @Override
  protected void onResume() {
    super.onResume();
    boolean selectedTheme = sharedPreferences.getBoolean(getString(R.string.pref_theme_key), false);
    if (themeMode != selectedTheme) {
      recreate();
    }
  }

  private void setAppTheme(boolean currentTheme) {
    if (currentTheme) {
      setTheme(R.style.DarkTheme);
    } else {
      setTheme(R.style.AppTheme);
    }
  }
}
