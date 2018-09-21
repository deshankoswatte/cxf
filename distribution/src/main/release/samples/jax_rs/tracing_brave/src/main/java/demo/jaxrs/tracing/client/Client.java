/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.jaxrs.tracing.client;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.tracing.brave.jaxrs.BraveClientProvider;

import demo.jaxrs.tracing.server.CatalogTracing;

public final class Client {
    private Client() {
    }

    public static void main(final String[] args) throws Exception {
        final CatalogTracing tracing = new CatalogTracing("catalog-client");
        final BraveClientProvider provider = new BraveClientProvider(tracing.getHttpTracing());

        final Response response = WebClient
            .create("http://localhost:9000/catalog", Arrays.asList(provider))
            .accept(MediaType.APPLICATION_JSON)
            .get();

        System.out.println(response.readEntity(String.class));
        response.close();
        
        // Give the tracer the time to flush traces (since we are using async reporter)
        Thread.sleep(1000);
    }
}
