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

import static com.pixxo.breezil.pixxo.BuildConfig.PIXXABAY_API_KEY;

import com.pixxo.breezil.pixxo.ApiAbstract;
import com.pixxo.breezil.pixxo.model.PhotosResult;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PhotoApiTest extends ApiAbstract<PhotoApi> {
  private PhotoApi photoApi;

  @Before
  public void initService() {
    this.photoApi = createService(PhotoApi.class);
  }

  @Test
  public void fetchPhotoResponseTest() throws IOException {
    enqueueResponse("photo_response.json");
    PhotosResult photosResult =
        photoApi.getPhotos(PIXXABAY_API_KEY, "sunflower", "", "", "", 1, 20).blockingGet();
    Assert.assertEquals(3292932, photosResult.getHits().get(0).getPhoto_id());
    Assert.assertEquals(20, photosResult.getHits().size());
    Assert.assertEquals(500, photosResult.getTotalHits());
    Assert.assertEquals(5869, photosResult.getTotal());
  }
}
