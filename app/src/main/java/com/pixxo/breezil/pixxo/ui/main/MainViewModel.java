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
package com.pixxo.breezil.pixxo.ui.main;

import static com.pixxo.breezil.pixxo.utils.Constant.FIVE;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.NetworkState;
import com.pixxo.breezil.pixxo.repository.paging.PhotoDataSource;
import com.pixxo.breezil.pixxo.repository.paging.PhotoDataSourceFactory;
import com.pixxo.breezil.pixxo.utils.helper.AppExecutors;
import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {
  private LiveData<PagedList<Photo>> photoList;
  private LiveData<NetworkState> networkState;
  private LiveData<NetworkState> initialLoading;
  private AppExecutors appsExecutor;
  private PhotoDataSourceFactory photoDataSourceFactory;

  @Inject
  MainViewModel(
      PhotoDataSourceFactory photoDataSourceFactory,
      AppExecutors appsExecutor,
      @NonNull Application application) {
    super(application);
    this.photoDataSourceFactory = photoDataSourceFactory;
    this.appsExecutor = appsExecutor;

    networkState =
        Transformations.switchMap(
            photoDataSourceFactory.getPhotoDataSources(), PhotoDataSource::getNetworkState);
    initialLoading = Transformations.switchMap(photoDataSourceFactory.getPhotoDataSources(), PhotoDataSource::getInitialState);

    PagedList.Config config =
        new PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(FIVE)
            .setPrefetchDistance(FIVE)
            .setPageSize(FIVE)
            .build();

    photoList =
        new LivePagedListBuilder<>(photoDataSourceFactory, config)
            .setFetchExecutor(appsExecutor.networkIO())
            .build();
  }

  public LiveData<PagedList<Photo>> getPhotoList() {
    return photoList;
  }

  public void setParameter(String search, String category, String lang, String order) {
    photoDataSourceFactory.getDataSource().setSearch(search);
    photoDataSourceFactory.getDataSource().setCategory(category);
    photoDataSourceFactory.getDataSource().setLang(lang);
    photoDataSourceFactory.getDataSource().setOrder(order);
  }

  public void setNetworkState(){
    networkState = Transformations.switchMap(photoDataSourceFactory.getPhotoDataSources(),
        PhotoDataSource::getNetworkState);
    initialLoading = Transformations.switchMap(photoDataSourceFactory.getPhotoDataSources(), PhotoDataSource::getInitialState);
  }

  public LiveData<PagedList<Photo>> refreshPhotos() {
    PagedList.Config config =
        new PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(FIVE).build();

    photoList =
        new LivePagedListBuilder<>(photoDataSourceFactory, config)
            .setFetchExecutor(appsExecutor.networkIO())
            .build();

    return photoList;
  }

  public LiveData<NetworkState>
  getNetworkState() {
    return networkState;
  }
  public LiveData<NetworkState> getInitialLoading() {
    return initialLoading;
  }

}
