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

use std::sync::Arc;

use crate::{
    common::{stream::Stream, Promise, IID},
    concept::{Attribute, AttributeType, Entity, EntityType, Relation, RelationType, SchemaException, ValueType},
    connection::TransactionStream,
    promisify, resolve, Result,
};

/// Provides access for all Concept API methods.
#[derive(Debug)]
pub struct ConceptManager {
    pub(super) transaction_stream: Arc<TransactionStream>,
}

impl ConceptManager {
    pub(crate) fn new(transaction_stream: Arc<TransactionStream>) -> Self {
        Self { transaction_stream }
    }

    /// Retrieves an `EntityType` by its label.
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `EntityType` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_entity_type(label)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_entity_type(label).await")]
    /// ```
    pub fn get_entity_type(&self, label: String) -> impl Promise<Result<Option<EntityType>>> {
        promisify! {
            resolve!(self.transaction_stream.get_entity_type(label))
        }
    }

    /// Retrieves a `RelationType` by its label.
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `RelationType` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_relation_type(label)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_relation_type(label).await")]
    /// ```
    pub fn get_relation_type(&self, label: String) -> impl Promise<Result<Option<RelationType>>> {
        promisify! {
            resolve!(self.transaction_stream.get_relation_type(label))
        }
    }

    /// Retrieves an `AttributeType` by its label.
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `AttributeType` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_attribute_type(label)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_attribute_type(label).await")]
    /// ```
    pub fn get_attribute_type(&self, label: String) -> impl Promise<Result<Option<AttributeType>>> {
        promisify! {
            resolve!(self.transaction_stream.get_attribute_type(label))
        }
    }

    /// Creates a new `EntityType` if none exists with the given label, otherwise retrieves the existing one.
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `EntityType` to create or retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().put_entity_type(label)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().put_entity_type(label).await")]
    /// ```
    pub fn put_entity_type(&self, label: String) -> impl Promise<Result<EntityType>> {
        promisify! {
            resolve!(self.transaction_stream.put_entity_type(label))
        }
    }

    /// Creates a new `RelationType` if none exists with the given label, otherwise retrieves the existing one.
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `RelationType` to create or retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().put_relation_type(label)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().put_relation_type(label).await")]
    /// ```
    pub fn put_relation_type(&self, label: String) -> impl Promise<Result<RelationType>> {
        promisify! {
            resolve!(self.transaction_stream.put_relation_type(label))
        }
    }

    /// Creates a new `AttributeType` if none exists with the given label, or retrieves the existing one.
    /// or retrieve. :return:
    ///
    /// # Arguments
    ///
    /// * `label` -- The label of the `AttributeType` to create or retrieve
    /// * `value_type` -- The value type of the `AttributeType` to create
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "await transaction.concepts().put_attribute_type(label, value_type)")]
    #[cfg_attr(not(feature = "sync"), doc = "await transaction.concepts().put_attribute_type(label, value_type).await")]
    /// ```
    pub fn put_attribute_type(&self, label: String, value_type: ValueType) -> impl Promise<Result<AttributeType>> {
        promisify! {
            resolve!(self.transaction_stream.put_attribute_type(label, value_type))
        }
    }

    /// Retrieves an `Entity` by its iid.
    ///
    /// # Arguments
    ///
    /// * `iid` -- The iid of the `Entity` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_entity(iid)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_entity(iid).await")]
    /// ```
    pub fn get_entity(&self, iid: IID) -> impl Promise<Result<Option<Entity>>> {
        promisify! {
            resolve!(self.transaction_stream.get_entity(iid))
        }
    }

    /// Retrieves a `Relation` by its iid.
    ///
    /// # Arguments
    ///
    /// * `iid` -- The iid of the `Relation` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_relation(iid)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_relation(iid).await")]
    /// ```
    pub fn get_relation(&self, iid: IID) -> impl Promise<Result<Option<Relation>>> {
        promisify! {
            resolve!(self.transaction_stream.get_relation(iid))
        }
    }

    /// Retrieves an `Attribute` by its iid.
    ///
    /// # Arguments
    ///
    /// * `iid` -- The iid of the `Attribute` to retrieve
    ///
    /// # Examples
    ///
    /// ```rust
    #[cfg_attr(feature = "sync", doc = "transaction.concepts().get_attribute(iid)")]
    #[cfg_attr(not(feature = "sync"), doc = "transaction.concepts().get_attribute(iid).await")]
    /// ```
    pub fn get_attribute(&self, iid: IID) -> impl Promise<Result<Option<Attribute>>> {
        promisify! {
            resolve!(self.transaction_stream.get_attribute(iid))
        }
    }

    /// Retrieves a list of all schema exceptions for the current transaction.
    ///
    /// # Examples
    ///
    /// ```rust
    /// transaction.concepts().get_schema_exceptions()
    /// ```
    pub fn get_schema_exceptions(&self) -> Result<impl Stream<Item = Result<SchemaException>>> {
        self.transaction_stream.get_schema_exceptions()
    }
}
