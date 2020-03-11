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

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.pixxo.breezil.pixxo.ui.main.MainViewModel;
import com.pixxo.breezil.pixxo.ui.main.home.detail.DetailViewModel;
import com.pixxo.breezil.pixxo.ui.main.saved.SavedPhotosViewModel;
import com.pixxo.breezil.pixxo.view_model.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel.class)
  abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(DetailViewModel.class)
  abstract ViewModel bindDetailViewModel(DetailViewModel detailViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(SavedPhotosViewModel.class)
  abstract ViewModel bindSavedPhotosViewModel(SavedPhotosViewModel savedPhotosViewModel);

  @Binds
  abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
