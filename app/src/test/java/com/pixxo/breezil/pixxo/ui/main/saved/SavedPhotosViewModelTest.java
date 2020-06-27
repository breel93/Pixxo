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
package com.pixxo.breezil.pixxo.ui.main.saved;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import com.pixxo.breezil.pixxo.MockTestUtil;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.SavedRepository;
import com.pixxo.breezil.pixxo.util.LiveDataTestUtil;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SavedPhotosViewModelTest {

  private SavedPhotosViewModel savedPhotosViewModel;

  @Mock private SavedRepository repository;

  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    savedPhotosViewModel = new SavedPhotosViewModel(repository);
  }

  @Test
  public void retrievePhotoList() throws Exception {
    // arrange
    List<Photo> photoList = MockTestUtil.mockPhotoList();
    LiveDataTestUtil<List<Photo>> liveDataTestUtil = new LiveDataTestUtil<>();
    MutableLiveData<List<Photo>> returnedValue = new MutableLiveData<>();
    returnedValue.setValue(photoList);
    when(repository.getSavedPhotos()).thenReturn(returnedValue);

    // act
    savedPhotosViewModel.getSavedPhoto();
    List<Photo> observedData = liveDataTestUtil.getValue(savedPhotosViewModel.getSavedPhoto());

    // assert
    Assert.assertEquals(photoList, observedData);
  }

  @Test
  public void insertPhoto() throws Exception {
    Photo photo = MockTestUtil.mockPhoto();
    String expected = "Successful";
    LiveDataTestUtil<String> liveDataTestUtil = new LiveDataTestUtil<>();
    MutableLiveData<String> returnData = new MutableLiveData<>();
    returnData.setValue(expected);
    when(repository.insertPhoto(photo)).thenReturn(returnData);

    // act

    savedPhotosViewModel.insertPhoto(photo);
    String observedResult = liveDataTestUtil.getValue(savedPhotosViewModel.insertPhoto(photo));

    Assert.assertEquals(expected, observedResult);
  }

  @Test
  public void deletePhoto() throws Exception {
    Photo photo = MockTestUtil.mockPhoto();
    String expected = "Successful";
    LiveDataTestUtil<String> liveDataTestUtil = new LiveDataTestUtil<>();
    MutableLiveData<String> returnData = new MutableLiveData<>();
    returnData.setValue(expected);
    repository.insertPhoto(photo);
    when(repository.deletePhoto(photo)).thenReturn(returnData);

    // act

    savedPhotosViewModel.insertPhoto(photo);
    String observedResult = liveDataTestUtil.getValue(savedPhotosViewModel.deletePhoto(photo));

    Assert.assertEquals(expected, observedResult);
  }
}
