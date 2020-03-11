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

import androidx.annotation.Nullable;
import com.pixxo.breezil.pixxo.model.PhotosResult;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoApi {
  @GET("api/")
  Single<PhotosResult> getPhotos(
      @Query("key") @Nullable String key,
      @Query("q") @Nullable String search,
      @Query("lang") @Nullable String lang,
      @Query("category") @Nullable String category,
      @Query("order") @Nullable String order,
      @Query("page") int page,
      @Query("per_page") int per_page);
}
