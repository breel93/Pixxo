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

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.SavedRepository;
import java.util.List;
import javax.inject.Inject;

public class SavedPhotosViewModel extends ViewModel {
  private SavedRepository savedRepository;

  private MediatorLiveData<List<Photo>> photos = new MediatorLiveData<>();

  @Inject
  public SavedPhotosViewModel(SavedRepository savedRepository) {
    this.savedRepository = savedRepository;
  }

  public LiveData<List<Photo>> getSavedList() {
    return photos;
  }

  public LiveData<List<Photo>> getSavedPhoto() {
    return savedRepository.getSavedPhotos();
  }

  public void getPhotos() {
    final LiveData<List<Photo>> source = savedRepository.getSavedPhotos();
    photos.addSource(
        source,
        new Observer<List<Photo>>() {
          @Override
          public void onChanged(List<Photo> photos_list) {
            if (photos != null) {
              photos.setValue(photos_list);
            }
            photos.removeSource(source);
          }
        });
  }

  public LiveData<String> insertPhoto(Photo photo) {
    return savedRepository.insertPhoto(photo);
  }

  public LiveData<String> deletePhoto(Photo photo) {
    return savedRepository.deletePhoto(photo);
  }
}
