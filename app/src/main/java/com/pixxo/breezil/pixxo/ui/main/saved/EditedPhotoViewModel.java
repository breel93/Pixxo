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

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.EditedPhotoRepository;

import java.io.File;
import java.util.List;
import javax.inject.Inject;

public class EditedPhotoViewModel extends ViewModel {
  private EditedPhotoRepository editedPhotoRepository;
  public MutableLiveData<Boolean> hasBeenDeleted = new MutableLiveData<>();

  @Inject
  public EditedPhotoViewModel(EditedPhotoRepository editedPhotoRepository) {
    this.editedPhotoRepository = editedPhotoRepository;
  }

  public LiveData<List<EditedPhoto>> getEditedPhotos(Context context) {
    return editedPhotoRepository.getEditedPhotos(context);
  }
  public void deletePhoto(EditedPhoto photo){
    File file = new File(photo.getPath());
    if (file.exists()) {
      file.delete();
      hasBeenDeleted.postValue(true);
    }
  }
}
