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
package com.pixxo.breezil.pixxo.api;

import static com.pixxo.breezil.pixxo.BuildConfig.API_KEY;

import com.pixxo.breezil.pixxo.model.PhotosResult;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class EndpointRepository {

  private PhotoApi photoApi;

  @Inject
  EndpointRepository(PhotoApi photoApi) {
    this.photoApi = photoApi;
  }

  public Single<PhotosResult> getPhotos(
      String search, String lang, String category, String order, int page, int per_page) {
    return photoApi
        .getPhotos(API_KEY, search, lang, category, order, page, per_page)
        .retry(3)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
