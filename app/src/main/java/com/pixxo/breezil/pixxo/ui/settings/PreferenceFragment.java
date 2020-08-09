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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.pixxo.breezil.pixxo.R;

import timber.log.Timber;

public class PreferenceFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preference, rootKey);

    PreferenceManager.setDefaultValues(
        requireActivity(), R.xml.preference, false);

    initSummary(getPreferenceScreen());
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final ListView listView = view.findViewById(android.R.id.list);
    if (listView != null) ViewCompat.setNestedScrollingEnabled(listView, true);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (!key.equals(getString(R.string.pref_category_key))) {
      updateSummary(findPreference(key));
      updateNightMode(findPreference(key));
      requireActivity().recreate();
    }
  }

  private void initSummary(Preference p) {
    if (p instanceof PreferenceGroup) {
      PreferenceGroup pGrp = (PreferenceGroup) p;
      for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
        initSummary(pGrp.getPreference(i));
      }
    } else {
      updateSummary(p);
      updateNightMode(p);
    }
  }

  private void updateSummary(Preference p) {
    if (p instanceof ListPreference) {
      ListPreference listPref = (ListPreference) p;
      p.setSummary(listPref.getEntry());
    }
  }

  private void updateNightMode(Preference p) {
    if (p instanceof SwitchPreference) {
      if (((SwitchPreference) p).isChecked()) {
        p.setSummary(getString(R.string.pref_theme_checked_summary));
        p.setDefaultValue(getString(R.string.pref_theme_true_value));
        Timber.d(String.valueOf(((SwitchPreference) p).getSummaryOn()));
      } else {
        p.setSummary(getString(R.string.pref_theme_unchecked_summary));
        p.setDefaultValue(getString(R.string.pref_theme_false_value));
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onPause() {
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onPause();
  }

  @Override
  public void onStop() {
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    super.onStop();
  }


}
