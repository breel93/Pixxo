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

import static com.pixxo.breezil.pixxo.BuildConfig.BASE_URL;

import android.app.Application;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.api.OkHttp;
import com.pixxo.breezil.pixxo.api.PhotoApi;
import com.pixxo.breezil.pixxo.db.AppDatabase;
import com.pixxo.breezil.pixxo.db.PhotosDao;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
  @Singleton
  @Provides
  PhotoApi provideImagesApi() {
    OkHttp okHttp = new OkHttp();
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttp.getClient())
        .build()
        .create(PhotoApi.class);
  }

  @Singleton
  @Provides
  public AppDatabase provideDatabase(Application app) {
    final Migration MIGRATION_1_2 =
        new Migration(1, 2) {
          @Override
          public void migrate(SupportSQLiteDatabase database) {
            database.beginTransaction();
          }
        };
    return Room.databaseBuilder(
            app.getApplicationContext(), AppDatabase.class, app.getString(R.string.pixxo_db))
        .addMigrations(MIGRATION_1_2)
        .build();
  }

  @Singleton
  @Provides
  public PhotosDao providePhotoDao(AppDatabase db) {
    return db.imagesDao();
  }

  @Provides
  CompositeDisposable provideCompositeDisposable() {
    return new CompositeDisposable();
  }
}
