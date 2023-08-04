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

mod thing;
mod type_;
mod value;

pub use self::{
    thing::{Attribute, Entity, Relation, Thing},
    type_::{Annotation, AttributeType, EntityType, RelationType, RoleType, RootThingType, ScopedLabel, ThingType},
    value::{Value, ValueType},
};

#[derive(Clone, Debug, PartialEq)]
pub enum Concept {
    RootThingType(RootThingType),

    EntityType(EntityType),
    RelationType(RelationType),
    RoleType(RoleType),
    AttributeType(AttributeType),

    Entity(Entity),
    Relation(Relation),
    Attribute(Attribute),

    Value(Value),
}

impl Concept {
    pub fn type_label_cloned(&self) -> String {
        // FIXME: Add a Label type to simplify this function
        match self {
            Self::RootThingType(_) => RootThingType::LABEL.to_owned(),
            Self::EntityType(type_) => type_.label.clone(),
            Self::RelationType(RelationType { label, .. }) => label.clone(),
            Self::RoleType(RoleType { label, .. }) => format!("{label}"),
            Self::AttributeType(AttributeType { label, .. }) => label.clone(),
            Self::Entity(Entity { type_: EntityType { label, .. }, .. }) => label.clone(),
            Self::Relation(Relation { type_: RelationType { label, .. }, .. }) => label.clone(),
            Self::Attribute(Attribute { type_: AttributeType { label, .. }, .. }) => label.clone(),
            Self::Value(_) => todo!(),
        }
    }
}

#[repr(C)]
#[derive(Copy, Clone, Debug, Eq, PartialEq)]
pub enum Transitivity {
    Explicit,
    Transitive,
}

#[derive(Clone, Debug)]
pub struct SchemaException {
    pub code: String,
    pub message: String,
}
