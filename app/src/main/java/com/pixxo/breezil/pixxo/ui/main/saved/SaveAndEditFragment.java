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
package com.pixxo.breezil.pixxo.ui.main.saved;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.databinding.FragmentSaveAndEditBinding;
import com.pixxo.breezil.pixxo.ui.adapter.SavedPagerAdapter;
import dagger.android.support.AndroidSupportInjection;
import java.lang.reflect.Field;

/** A simple {@link Fragment} subclass. */
public class SaveAndEditFragment extends Fragment {

  private FragmentSaveAndEditBinding binding;

  public SaveAndEditFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(@NonNull Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_save_and_edit, container, false);

    setupAdapter();

    return binding.getRoot();
  }

  private void setupAdapter() {
    SavedPagerAdapter pagerAdapter =
        new SavedPagerAdapter(getParentFragmentManager(), getContext());
    binding.container.setAdapter(pagerAdapter);
    binding.tabs.setupWithViewPager(binding.container);
    binding.container.setCurrentItem(0, false);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}