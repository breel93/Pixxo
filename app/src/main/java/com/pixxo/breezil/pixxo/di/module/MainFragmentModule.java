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
package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.main.home.MainFragment;
import com.pixxo.breezil.pixxo.ui.main.home.SingleListFragment;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SingleEditedPhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.home.detail.SinglePhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.EditPhotoFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SaveAndEditFragment;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotoFragment;
import com.pixxo.breezil.pixxo.ui.settings.SettingsFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentModule {

  @ContributesAndroidInjector
  abstract MainFragment contributeMainFragment();

  @ContributesAndroidInjector
  abstract SaveAndEditFragment contributeSaveAndEditFragment();

  @ContributesAndroidInjector
  abstract SingleListFragment contributeSingleListFragment();

  @ContributesAndroidInjector
  abstract SinglePhotoFragment contributeSinglePhotoFragment();

  @ContributesAndroidInjector
  abstract SingleEditedPhotoFragment contributeSingleEditedPhotoFragment();

  @ContributesAndroidInjector
  abstract SavedPhotoFragment contributeSavedPhotoFragment();

  @ContributesAndroidInjector
  abstract EditPhotoFragment contributeEditSavedFragment();

  @ContributesAndroidInjector
  abstract SettingsFragment contributeSettingsFragment();
}
