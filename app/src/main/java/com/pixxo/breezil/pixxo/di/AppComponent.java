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
package com.pixxo.breezil.pixxo.di;

import android.app.Application;
import com.pixxo.breezil.pixxo.PixxoApp;
import com.pixxo.breezil.pixxo.di.module.ActionBottomSheetFragmentModule;
import com.pixxo.breezil.pixxo.di.module.AppModule;
import com.pixxo.breezil.pixxo.di.module.BaseActivityModule;
import com.pixxo.breezil.pixxo.di.module.MainActivityModule;
import com.pixxo.breezil.pixxo.di.module.SavedActionBottomSheetFragmentModule;
import com.pixxo.breezil.pixxo.di.module.SavedFragmentModule;
import com.pixxo.breezil.pixxo.di.module.SettingsActivityModule;
import com.pixxo.breezil.pixxo.di.module.SinglePhotoFragmentModule;
import com.pixxo.breezil.pixxo.di.module.SplashActivityModule;
import com.pixxo.breezil.pixxo.di.module.ViewModelModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
      AndroidInjectionModule.class,
      AppModule.class,
      MainActivityModule.class,
      SavedFragmentModule.class,
      SettingsActivityModule.class,
      BaseActivityModule.class,
      SplashActivityModule.class,
      ActionBottomSheetFragmentModule.class,
      SavedActionBottomSheetFragmentModule.class,
      SinglePhotoFragmentModule.class,
      ViewModelModule.class
    })
public interface AppComponent extends AndroidInjector<PixxoApp> {
  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);

    AppComponent build();
  }
}
