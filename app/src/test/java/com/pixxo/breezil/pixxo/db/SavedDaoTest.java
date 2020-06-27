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
package com.pixxo.breezil.pixxo.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.pixxo.breezil.pixxo.MockTestUtil;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.util.LiveDataTestUtil;
import java.util.List;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SavedDaoTest extends AppDatabaseTest {

  @Rule public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

  @Test
  public void insertPhotoModelTest() {
    Photo photo;
    photo = MockTestUtil.mockPhoto(5);
    db.imagesDao().insert(photo);
    Photo savedPhoto = db.imagesDao().getPhotoById(5);
    Assert.assertEquals(5, savedPhoto.getRoomId());
  }

  @Test
  public void deletePhotoModelTest() {
    Photo photo;
    photo = MockTestUtil.mockPhoto(5);
    db.imagesDao().delete(photo);
    Photo savedPhoto = db.imagesDao().getPhotoById(5);
    Assert.assertNull(savedPhoto);
  }

  @Test
  public void getPhotosTest() throws Exception {
    Photo photo = MockTestUtil.mockPhoto();

    db.imagesDao().insert(photo);

    LiveDataTestUtil<List<Photo>> liveDataTestUtil = new LiveDataTestUtil<>();
    List<Photo> photoList = liveDataTestUtil.getValue(db.imagesDao().getSavedPhoto());
    assertNotNull(photoList);
    assertEquals(photo.getPhoto_id(), photoList.get(0).getPhoto_id());
  }
}
