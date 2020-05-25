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
package com.pixxo.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.ui.main.saved.EditPhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotoFragment;

public class SavedPagerAdapter extends FragmentStatePagerAdapter {
  private Context context;
  private SparseArray<Fragment> fragmentList = new SparseArray<>();

  private static final int saved_Position = 0;
  private static final int editsaved_Position = 1;

  public SavedPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    if (position == saved_Position) {
      return new SavedPhotoFragment();
    } else {
      return new EditPhotoFragment();
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    Resources resources = context.getResources();
    switch (position) {
      case saved_Position:
        return resources.getString(R.string.saved);

      case editsaved_Position:
        return resources.getString(R.string.edited);
      default:
        return context.getString(R.string.none);
    }
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    fragmentList.put(position, fragment);
    return fragment;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    fragmentList.remove(position);
    super.destroyItem(container, position, object);
  }
}
