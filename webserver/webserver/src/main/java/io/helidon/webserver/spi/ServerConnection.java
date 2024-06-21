/*
 * Copyright (c) 2022, 2024 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.webserver.spi;

import com.netflix.concurrency.limits.Limiter;

import java.time.Duration;
import java.util.concurrent.Semaphore;

/**
 * Server connection abstraction, used by any provider to handle a socket connection.
 */
public interface ServerConnection {
    /**
     * Start handling the connection. Data is provided through
     * {@link ServerConnectionSelector#connection(io.helidon.webserver.ConnectionContext)}.
     *
     * @param requestLimiter limiter that is responsible to control concurrent requests, the connection implementation
     *                         is responsible for acquiring a permit from the limiter for the duration of a request, and
     *                         releasing it when the request ends by using the limiter listener; please be very careful,
     *                         as this may lead to complete stop of the server if incorrectly implemented
     * @throws InterruptedException to interrupt any waiting state and terminate this connection
     */
    void handle(Limiter requestLimiter) throws InterruptedException;

    /**
     * How long is this connection idle. This is a duration from the last request to now.
     *
     * @return idle duration
     */
    Duration idleTime();

    /**
     * Close a connection. This may be called during shutdown of the server, or when idle timeout is reached.
     *
     * @param interrupt whether to interrupt in progress requests (always interrupts idle requests waiting for
     *                  initial request data)
     */
    void close(boolean interrupt);
}
