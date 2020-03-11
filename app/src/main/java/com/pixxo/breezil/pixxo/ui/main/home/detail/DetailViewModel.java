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
package com.pixxo.breezil.pixxo.ui.main.home.detail;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.pixxo.breezil.pixxo.model.Photo;
import javax.inject.Inject;

public class DetailViewModel extends AndroidViewModel {

  private MutableLiveData<Photo> imageModel = new MutableLiveData<>();

  @Inject
  DetailViewModel(Application application) {
    super(application);
  }

  public void setImage(Photo image) {
    this.imageModel.setValue(image);
  }

  public MutableLiveData<Photo> getImage() {
    return imageModel;
  }
}
