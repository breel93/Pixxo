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

import static com.pixxo.breezil.pixxo.utils.Constant.DOTJPG;
import static com.pixxo.breezil.pixxo.utils.Constant.DOTPNG;
import static com.pixxo.breezil.pixxo.utils.Constant.PIXXO_EDITED;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pixxo.breezil.pixxo.model.EditedPhoto;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class EditedPhotoRepository {
  private MutableLiveData<List<EditedPhoto>> editedPhotosList = new MutableLiveData<>();
  @Inject
  public EditedPhotoRepository() {}

  public LiveData<List<EditedPhoto>> getEditedPhotos(Context context) {
    ArrayList<EditedPhoto> editedPhotos = new ArrayList<>();
    File filePath = new File(PIXXO_EDITED);
    EditedPhoto photo;
    File[] files = new File(PIXXO_EDITED).listFiles(new ImageFileFilter());
    if (filePath.exists()) {
      assert files != null;
      for (File file : files) {
        photo = new EditedPhoto();
        Uri imageUri = Uri.fromFile(file);
        try {
          Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
          photo.setImage(bitmap);
          photo.setPath(file.getPath());
          photo.setUri(imageUri);

        } catch (IOException e) {
          e.printStackTrace();
        }
        if (file.isDirectory() && file.listFiles(new ImageFileFilter()).length > 0) {
          editedPhotos.add(new EditedPhoto(file.getAbsolutePath(), true, null,imageUri));
        }
        editedPhotos.add(photo);
        editedPhotosList.postValue(editedPhotos);
      }
    }
    return editedPhotosList;
  }



  private boolean isImageFile(String filePath) {
    // Add other formats as desired
    return filePath.endsWith(DOTPNG) || filePath.endsWith(DOTJPG);
  }

  /** This can be used to filter files. */
  private class ImageFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
      if (file.isDirectory()) return true;
      else return isImageFile(file.getAbsolutePath());
    }
  }
}
