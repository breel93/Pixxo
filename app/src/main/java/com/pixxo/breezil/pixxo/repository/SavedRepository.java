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
package com.pixxo.breezil.pixxo.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pixxo.breezil.pixxo.db.PhotosDao;
import com.pixxo.breezil.pixxo.model.Photo;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SavedRepository {

  private PhotosDao photosDao;

  private MutableLiveData<String> isSuccessful = new MutableLiveData<>();

  @Inject
  public SavedRepository(PhotosDao photosDao) {
    this.photosDao = photosDao;
  }

  public LiveData<List<Photo>> getSavedPhotos() {
    return photosDao.getSavedPhoto();
  }

  public MutableLiveData<String> insertPhoto(Photo photo) {
    Completable.fromAction(() -> photosDao.insert(photo))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new CompletableObserver() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onComplete() {
                isSuccessful.postValue("Successful");
              }

              @Override
              public void onError(Throwable e) {
                isSuccessful.postValue(e.getMessage());
              }
            });

    return isSuccessful;
  }

  public MutableLiveData<String> deletePhoto(Photo photo) {
    Completable.fromAction(() -> photosDao.delete(photo))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new CompletableObserver() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onComplete() {
                isSuccessful.postValue("Successful");
              }

              @Override
              public void onError(Throwable e) {
                isSuccessful.postValue(e.getMessage());
              }
            });
    return isSuccessful;
  }
}
