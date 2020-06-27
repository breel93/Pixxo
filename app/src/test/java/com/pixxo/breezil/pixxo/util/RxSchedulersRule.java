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

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RxSchedulersRule implements TestRule {

  private Scheduler SCHEDULER_INSTANCE = Schedulers.trampoline();

  private Function<Scheduler, Scheduler> schedulerMapper = scheduler -> SCHEDULER_INSTANCE;
  private Function<Callable<Scheduler>, Scheduler> schedulerMapperLazy =
      schedulerCallable -> SCHEDULER_INSTANCE;

  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        RxAndroidPlugins.reset();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerMapperLazy);

        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(schedulerMapper);
        RxJavaPlugins.setNewThreadSchedulerHandler(schedulerMapper);
        RxJavaPlugins.setComputationSchedulerHandler(schedulerMapper);

        base.evaluate();

        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
      }
    };
  }
}
