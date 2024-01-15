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

#![deny(elided_lifetimes_in_paths)]
#![deny(unused_must_use)]

use futures::stream::StreamExt;
use typedb_driver::{
    concept::{EntityType, Transitivity},
    transaction::concept::api::EntityTypeAPI,
    Connection, DatabaseManager, Session, SessionType, TransactionType,
};

#[tokio::test]
async fn basic() -> typedb_driver::Result {
    let connection = Connection::new_core("localhost:1729")?;
    let databases = DatabaseManager::new(connection);
    databases.create("typedb").await?;
    let session = Session::new(databases.get("typedb").await?, SessionType::Data).await?;
    let tx = session.transaction(TransactionType::Write).await?;
    let mut subtypes = EntityType::root().get_subtypes(&tx, Transitivity::Transitive)?;
    assert!(subtypes.next().await.is_some_and(|res| res.is_ok()));
    assert!(subtypes.next().await.is_none());
    Ok(())
}
