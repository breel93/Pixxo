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
package com.pixxo.breezil.pixxo.ui.main;

import android.app.Application;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import com.pixxo.breezil.pixxo.model.Photo;
import com.pixxo.breezil.pixxo.repository.paging.PhotoDataSource;
import com.pixxo.breezil.pixxo.repository.paging.PhotoDataSourceFactory;
import com.pixxo.breezil.pixxo.utils.helper.AppExecutors;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

  private MainViewModel mainViewModel;

  @Mock MutableLiveData<List<Photo>> listMutableLiveData;

  @Mock PhotoDataSourceFactory photoDataSourceFactory;
  @Mock
  PhotoDataSource dataSource;
  @Mock AppExecutors appsExecutor;

  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @Before
  public void init() {
    PhotoDataSourceFactory photoDataSourceFactory;
    Application app = (Application) InstrumentationRegistry.getInstrumentation().getContext();
    appsExecutor = new AppExecutors();
  }

  @Test
  public void imageListTest() {

  }
}
