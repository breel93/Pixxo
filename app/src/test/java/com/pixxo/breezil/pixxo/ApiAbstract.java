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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(JUnit4.class)
public class ApiAbstract<T> {

  private MockWebServer mockWebServer;

  @Before
  public void mockServer() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @After
  public void stopServer() throws IOException {
    mockWebServer.shutdown();
  }

  public void enqueueResponse(String fileName) throws IOException {
    enqueueResponse(fileName, Collections.EMPTY_MAP);
  }

  private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
    InputStream inputStream =
        ApiAbstract.class
            .getClassLoader()
            .getResourceAsStream(String.format("api-response/%s", fileName));
    Source source = Okio.buffer(Okio.source(inputStream));
    MockResponse mockResponse = new MockResponse();
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      mockResponse.addHeader(entry.getKey(), entry.getValue());
    }
    mockWebServer.enqueue(
        mockResponse.setBody(((BufferedSource) source).readString(StandardCharsets.UTF_8)));
  }

  public T createService(Class<T> clazz) {
    return new Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(clazz);
  }

  public void assertRequestPath(String path) throws InterruptedException {
    RecordedRequest request = mockWebServer.takeRequest();
    MatcherAssert.assertThat(request.getPath(), CoreMatchers.is(path));
  }
}
