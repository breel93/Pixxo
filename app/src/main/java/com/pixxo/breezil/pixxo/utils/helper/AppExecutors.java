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
package com.pixxo.breezil.pixxo.utils.helper;

import static com.pixxo.breezil.pixxo.utils.Constant.FIVE;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * More from here Architecture Guide "https://developer.android.com/arch" Helper code from
 * googlesamples/android-architecture-components:.
 * "https://github.com/breel93/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/pixxo/github/AppExecutors.kt"
 */
@Singleton
public class AppExecutors {

  private final Executor networkIO;
  private final Executor diskIO;
  private final Executor mainIO;

  @Inject
  public AppExecutors() {
    this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(FIVE));
  }

  public AppExecutors(Executor diskIO, Executor networkIO) {
    this.diskIO = diskIO;
    this.networkIO = networkIO;
    this.mainIO = new MainThreadExecutor();
  }

  static class MainThreadExecutor implements Executor {
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
      mainThreadHandler.post(command);
    }
  }

  public Executor networkIO() {
    return networkIO;
  }

  public Executor diskIO() {
    return diskIO;
  }

  public Executor mainIO() {
    return mainIO;
  }
}
