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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSettingsBinding;

import java.lang.reflect.Field;

import dagger.android.support.DaggerFragment;

/** A simple {@link Fragment} subclass. */
public class SettingsFragment extends DaggerFragment {
  private FragmentSettingsBinding binding;

  public SettingsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
    goBack();
    return binding.getRoot();
  }

  private void goBack() {
    binding.backPressed.setOnClickListener(v ->
        requireActivity().getSupportFragmentManager().popBackStack());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }


}
