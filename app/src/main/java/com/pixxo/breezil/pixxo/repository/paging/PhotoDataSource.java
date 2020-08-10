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
package com.pixxo.breezil.pixxo.repository.paging;

import static com.pixxo.breezil.pixxo.utils.Constant.ONE;
import static com.pixxo.breezil.pixxo.utils.Constant.TEN;
import static com.pixxo.breezil.pixxo.utils.Constant.TWO;
import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import com.pixxo.breezil.pixxo.api.EndpointRepository;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.model.PhotosResult;
import com.pixxo.breezil.pixxo.repository.NetworkState;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton
public class PhotoDataSource extends PageKeyedDataSource<Integer, Photo>
    implements PaginationListener<PhotosResult, Photo> {

  private String mSearch;
  private String mCategory;
  private String mLang;
  private String mOrder;
  private CompositeDisposable compositeDisposable;
  private EndpointRepository endpointRepository;

  private final MutableLiveData<NetworkState> mNetworkState;
  private final MutableLiveData<NetworkState> mInitialLoading;

  @Inject
  public PhotoDataSource(
      EndpointRepository endpointRepository, CompositeDisposable compositeDisposable) {
    mNetworkState = new MutableLiveData<>();
    mInitialLoading = new MutableLiveData<>();
    this.compositeDisposable = compositeDisposable;
    this.endpointRepository = endpointRepository;
  }

  public MutableLiveData<NetworkState> getNetworkState() {
    return mNetworkState;
  }

  public MutableLiveData<NetworkState> getInitialState() {
    return mInitialLoading;
  }

  public void setSearch(String search) {
    mSearch = search;
  }

  public String getSearch() {
    return mSearch;
  }

  public void setCategory(String category) {
    mCategory = category;
  }

  public String getCategory() {
    return mCategory;
  }

  public String getLang() {
    return mLang;
  }

  public void setLang(String lang) {
    mLang = lang;
  }

  public void setOrder(String order) {
    mOrder = order;
  }

  public String getOrder() {
    return mOrder;
  }

  @Override
  public void loadInitial(
      @NonNull LoadInitialParams<Integer> params,
      @NonNull LoadInitialCallback<Integer, Photo> callback) {

    List<Photo> modelList = new ArrayList<>();
    Disposable disposable =
        endpointRepository
            .getPhotos(getSearch(), getLang(), getCategory(), getOrder(), ONE, TEN)
            .subscribe(
                photosResult -> {
                  onInitialSuccess(photosResult, callback, modelList);
                },
                this::onInitialError);
    compositeDisposable.add(disposable);
  }

  @Override
  public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {

  }


  @Override
  public void loadAfter(
      @NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Photo> callback) {
    List<Photo> modelList = new ArrayList<>();
    Disposable disposable =
        endpointRepository
            .getPhotos(getSearch(), getLang(), getCategory(), getOrder(), params.key, TEN)
            .subscribe(
                (PhotosResult response) ->
                    onPaginationSuccess(response, callback, params, modelList),
                this::onPaginationError);

    compositeDisposable.add(disposable);
  }

  @Override
  public void onInitialError(Throwable throwable) {
    mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED));
    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
    Timber.e(throwable);
  }

  @Override
  public void onInitialSuccess(
      PhotosResult photosResult, LoadInitialCallback<Integer, Photo> callback, List<Photo> photos) {
    if (photosResult.getHits().size() > ZERO) {
      photos.addAll(photosResult.getHits());
      callback.onResult(photos, null, TWO);
      mInitialLoading.postValue(NetworkState.LOADED);
      mNetworkState.postValue(NetworkState.LOADED);

    } else {
      mInitialLoading.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
      mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
    }
  }

  @Override
  public void onPaginationError(Throwable throwable) {
    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED));
    Timber.e(throwable);
  }

  @Override
  public void onPaginationSuccess(
      PhotosResult photosResult,
      LoadCallback<Integer, Photo> callback,
      LoadParams<Integer> params,
      List<Photo> photos) {
    if (photosResult.getHits().size() > ZERO) {

      photos.addAll(photosResult.getHits());
      Integer key = (params.key > ONE) ? params.key + ONE : null;
      callback.onResult(photos, key);

      mNetworkState.postValue(NetworkState.LOADED);
    } else {
      mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_RESULT));
    }
  }

  @Override
  public void clear() {
    compositeDisposable.clear();
  }
}
