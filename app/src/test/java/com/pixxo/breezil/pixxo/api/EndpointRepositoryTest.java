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
package com.pixxo.breezil.pixxo.api;

import static com.pixxo.breezil.pixxo.BuildConfig.PIXXABAY_API_KEY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.pixxo.breezil.pixxo.MockTestUtil;
import com.pixxo.breezil.pixxo.model.PhotosResult;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EndpointRepositoryTest {

  @Mock private PhotoApi photoApi;

  private EndpointRepository repository;

  private TestSubscriber<PhotosResult> mPhotosTestSubscriber;

  @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    repository = new EndpointRepository(photoApi);
    mPhotosTestSubscriber = new TestSubscriber<>();
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void getPhotos() {
    PhotosResult mockResult = MockTestUtil.mockImageResult();
    when(photoApi.getPhotos(PIXXABAY_API_KEY, "sunflower", "", "", "", 1, 20))
        .thenReturn(Single.just(mockResult));

    // act
    Single<PhotosResult> data = repository.getPhotos("sunflower", "", "", "", 1, 20);

    // assert
    verify(photoApi).getPhotos(PIXXABAY_API_KEY, "sunflower", "", "", "", 1, 20);

    TestObserver testObserver = new TestObserver();
    data.subscribe(testObserver);
    testObserver.assertNoErrors();
  }

  @Test
  public void getPhotoss() {
    PhotosResult mockResult = MockTestUtil.mockImageResult();
    when(photoApi.getPhotoss(PIXXABAY_API_KEY, "sunflower", "", "", "", 1, 20))
        .thenReturn(Flowable.just(mockResult));

    // act
    repository.getPhotoss("sunflower", "", "", "", 1, 20);

    // assert
    verify(photoApi).getPhotoss(PIXXABAY_API_KEY, "sunflower", "", "", "", 1, 20);
  }
}
