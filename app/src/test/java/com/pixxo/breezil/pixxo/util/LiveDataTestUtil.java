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
package com.pixxo.breezil.pixxo.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil<T> {

  public T getValue(final LiveData<T> liveData) throws InterruptedException {

    final List<T> data = new ArrayList<>();

    // latch for blocking thread until data is set
    final CountDownLatch latch = new CountDownLatch(1);

    Observer<T> observer =
        new Observer<T>() {
          @Override
          public void onChanged(T t) {
            data.add(t);
            latch.countDown(); // release the latch
            liveData.removeObserver(this);
          }
        };
    liveData.observeForever(observer);
    try {
      latch.await(2, TimeUnit.SECONDS); // wait for onChanged to fire and set data
    } catch (InterruptedException e) {
      throw new InterruptedException("Latch failure");
    }
    if (data.size() > 0) {
      return data.get(0);
    }
    return null;
  }
}
