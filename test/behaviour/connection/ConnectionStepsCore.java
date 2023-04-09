/*
 * Copyright (C) 2022 Vaticle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vaticle.typedb.client.test.behaviour.connection;

import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBOptions;
import com.vaticle.typedb.common.test.TypeDBRunner;
import com.vaticle.typedb.common.test.core.TypeDBCoreRunner;
import com.vaticle.typedb.common.test.TypeDBSingleton;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionStepsCore extends ConnectionStepsBase {
    private TypeDBCoreRunner server;

    @Override
    void beforeAll() {
        try {
            server = new TypeDBCoreRunner();
        } catch (InterruptedException | TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
        server.start();
        TypeDBSingleton.setTypeDBRunner(server);
    }

    @Before
    public synchronized void before() {
        super.before();
    }

    @After
    public synchronized void after() {
        super.after();
    }

    @Override
    TypeDBClient createTypeDBClient(String address) {
        return TypeDB.coreClient(address);
    }

    @Override
    TypeDBOptions createOptions() {
        return TypeDBOptions.core();
    }

    @Override
    @When("open connection")
    public void open_connection() {
        client = createTypeDBClient(TypeDBSingleton.getTypeDBRunner().address());
    }

    @When("typedb starts")
    public void typedb_starts() {
        TypeDBRunner runner = TypeDBSingleton.getTypeDBRunner();
        if (runner != null && runner.isStopped()) {
            runner.start();
        } else {
            try {
                TypeDBCoreRunner typeDBCoreRunner = new TypeDBCoreRunner();
                TypeDBSingleton.setTypeDBRunner(typeDBCoreRunner);
                typeDBCoreRunner.start();
            } catch (Exception e) {}
        }
    }

    @When("typedb stops")
    public void typedb_stops() {
        TypeDBSingleton.getTypeDBRunner().stop();
    }

    @Given("connection has been opened")
    public void connection_has_been_opened() {
        super.connection_has_been_opened();
    }

    @Given("connection does not have any database")
    public void connection_does_not_have_any_database() {
        super.connection_does_not_have_any_database();
    }

}
