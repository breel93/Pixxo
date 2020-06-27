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
    photosResult.setHits(mockPhotoList(id));
    return photosResult;
  }

  public static PhotosResult mockImageResult() {
    PhotosResult photosResult = new PhotosResult();
    photosResult.setHits(mockPhotoList());
    photosResult.setTotal(5869);
    photosResult.setTotalHits(500);
    return photosResult;
  }

  public static List<Photo> mockPhotoList(int id) {
    List<Photo> photoList = new ArrayList<>();

    Photo photo = new Photo();
    photo.setPhoto_id(id);
    photoList.add(photo);

    Photo photo2 = new Photo();
    photo.setPhoto_id(id);
    photoList.add(photo2);

    return photoList;
  }

  public static List<Photo> mockPhotoList() {
    List<Photo> photoList = new ArrayList<>();

    Photo photo =
        new Photo(
            "https://pixabay.com/get/55e2dc414351ae14f6da8c7dda79367e1c3dd8e755506c48702679d29249cd5cbf_1280.jpg",
            3292932,
            "https://pixabay.com/get/55e2dc414351ae14f1dc8460962937771739dce3524c704c7c2d7dd19345c15f_640.jpg",
            640,
            400,
            "photo",
            "sunflower, vase, vintage");
    photoList.add(photo);

    Photo photo2 =
        new Photo(
            "https://pixabay.com/get/55e1d4404953a414f6da8c7dda79367e1c3dd8e755506c48702679d29249cd5cbf_1280.jpg",
            3113318,
            "https://pixabay.com/get/55e1d4404953a414f1dc8460962937771739dce3524c704c7c2d7dd19345c15f_640.jpg",
            640,
            426,
            "photo",
            "sunflower, nature, flora");
    photoList.add(photo2);

    return photoList;
  }

  public static Photo mockPhoto(int id) {
    Photo photo = new Photo();
    photo.setRoomId(id);
    return photo;
  }

  public static Photo mockPhoto() {
    return new Photo(
        "https://pixabay.com/get/55e2dc414351ae14f6da8c7dda79367e1c3dd8e755506c48702679d29249cd5cbf_1280.jpg",
        3292932,
        "https://pixabay.com/get/55e2dc414351ae14f1dc8460962937771739dce3524c704c7c2d7dd19345c15f_640.jpg",
        640,
        400,
        "photo",
        "sunflower, vase, vintage");
  }
}
