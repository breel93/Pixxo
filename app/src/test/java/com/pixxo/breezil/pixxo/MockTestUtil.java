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
package com.pixxo.breezil.pixxo;

import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.model.PhotosResult;
import java.util.ArrayList;
import java.util.List;

public class MockTestUtil {

  public static PhotosResult mockImageResult(int id) {
    PhotosResult photosResult = new PhotosResult();
    photosResult.setHits(mockImageList(id));
    return photosResult;
  }

  public static List<Photo> mockImageList(int id) {
    List<Photo> photoList = new ArrayList<>();

    Photo photo = new Photo();
    photo.setId(id);
    photoList.add(photo);

    Photo photo2 = new Photo();
    photo.setId(id);
    photoList.add(photo2);

    return photoList;
  }

  public static Photo mockImage(int id) {
    Photo photo = new Photo();
    photo.setId(id);
    return photo;
  }

  public static List<SavedImageModel> mockSavedImageList(int id) {
    List<SavedImageModel> savedImageModelList = new ArrayList<>();

    SavedImageModel savedImageModel = new SavedImageModel();
    savedImageModel.setId(id);
    savedImageModelList.add(savedImageModel);

    SavedImageModel imagesModel2 = new SavedImageModel();
    savedImageModel.setId(id);
    savedImageModelList.add(imagesModel2);

    return savedImageModelList;
  }

  public static SavedImageModel mockSavedImage(int id) {
    SavedImageModel savedImageModel = new SavedImageModel();
    savedImageModel.setId(id);
    return savedImageModel;
  }
}
