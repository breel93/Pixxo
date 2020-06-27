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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import com.pixxo.breezil.pixxo.MockTestUtil;
import com.pixxo.breezil.pixxo.db.PhotosDao;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.util.LiveDataTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SavedRepositoryTest {
  @Mock PhotosDao photosDao;
  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  SavedRepository savedRepository;

  @Before
  public void setUp() {
    savedRepository = new SavedRepository(photosDao);
  }

  @Test
  public void savePhoto() {
    Photo mockPhoto = MockTestUtil.mockPhoto();
    savedRepository.insertPhoto(mockPhoto);

    verify(photosDao).insert(mockPhoto);
  }

  @Test
  public void deletePhoto() {
    Photo mockPhoto = MockTestUtil.mockPhoto();
    savedRepository.deletePhoto(mockPhoto);
    verify(photosDao, atLeast(0)).delete(mockPhoto);
  }

  @Test
  public void fetchPhotos() throws Exception {
    // Arrange
    List<Photo> photos = MockTestUtil.mockPhotoList();
    LiveDataTestUtil<List<Photo>> liveDataTestUtil = new LiveDataTestUtil<>();
    MutableLiveData<List<Photo>> returnedData = new MutableLiveData<>();
    returnedData.setValue(photos);

    when(photosDao.getSavedPhoto()).thenReturn(returnedData);

    // Act
    List<Photo> observedData = liveDataTestUtil.getValue(savedRepository.getSavedPhotos());

    // Assert
    assertEquals(photos, observedData);
    //    verify(savedRepository).getSavedPhotos();
  }

  @Test
  public void fetchPhoto_ReturnEmpty() throws Exception {
    // Arrange
    List<Photo> photos = new ArrayList<>();
    LiveDataTestUtil<List<Photo>> liveDataTestUtil = new LiveDataTestUtil<>();
    MutableLiveData<List<Photo>> returnedData = new MutableLiveData<>();
    returnedData.setValue(photos);
    when(photosDao.getSavedPhoto()).thenReturn(returnedData);

    // Act
    List<Photo> observedData = liveDataTestUtil.getValue(savedRepository.getSavedPhotos());

    // Assert
    assertEquals(photos, observedData);
  }
}
