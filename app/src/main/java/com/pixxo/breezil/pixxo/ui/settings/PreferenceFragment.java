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

import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import com.pixxo.breezil.pixxo.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import timber.log.Timber;

public class PreferenceFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  private MultiSelectListPreference mCategoryPref;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preference, rootKey);

    PreferenceManager.setDefaultValues(
        Objects.requireNonNull(getActivity()), R.xml.preference, false);

    PreferenceGroup preferenceGroup = (PreferenceGroup) getPreferenceScreen().getPreference(ZERO);

    mCategoryPref = (MultiSelectListPreference) preferenceGroup.getPreference(ZERO);

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
      restartActivity();

    } else {
      updateMultiSummary(
          findPreference(key),
          sharedPreferences.getStringSet(getString(R.string.pref_category_key), null));
      restartActivity();
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
      updateMultiSummary(
          p,
          PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()))
              .getStringSet(getString(R.string.pref_category_key), null));
      updateNightMode(p);
    }
  }

  private void updateSummary(Preference p) {
    if (p instanceof ListPreference) {
      ListPreference listPref = (ListPreference) p;
      p.setSummary(listPref.getEntry());
    }
  }

  private void updateMultiSummary(Preference p, Set<String> value) {
    if (p instanceof MultiSelectListPreference) {
      MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) p;

      List<String> entries = new ArrayList<>(value);
      StringBuilder allEntries = new StringBuilder();

      for (int i = 0; i < entries.size(); i++) {
        allEntries
            .append(
                multiSelectListPreference
                    .getEntries()[multiSelectListPreference.findIndexOfValue(entries.get(i))])
            .append(", ");
      }

      if (allEntries.length() > 0) {
        allEntries.deleteCharAt(allEntries.length() - 2);
      }

      p.setSummary(allEntries.toString());
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

  public void restartActivity() {
    Intent intent = new Intent(getActivity(), SettingsActivity.class);
    startActivity(intent);
    getActivity().finish();
  }
}
