/*
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

package grakn.client.concept.remote;

import grakn.client.GraknClient;
import grakn.client.concept.ConceptId;
import grakn.client.concept.DataType;
import grakn.protocol.session.ConceptProto;

import java.util.stream.Stream;

import static grakn.client.concept.DataType.staticCastValue;

/**
 * Client implementation of Attribute
 *
 * @param <D> The data type of this attribute
 */
class RemoteAttributeImpl<D> extends RemoteThingImpl<RemoteAttribute<D>, RemoteAttributeType<D>>
        implements RemoteAttribute<D> {

    RemoteAttributeImpl(GraknClient.Transaction tx, ConceptId id) {
        super(tx, id);
    }

    @Override
    public final D value() {
        ConceptProto.Method.Req method = ConceptProto.Method.Req.newBuilder()
                .setAttributeValueReq(ConceptProto.Attribute.Value.Req.getDefaultInstance()).build();

        ConceptProto.ValueObject value = runMethod(method).getAttributeValueRes().getValue();
        return staticCastValue(value);
    }

    @Override
    public final Stream<RemoteThing<?, ?>> owners() {
        ConceptProto.Method.Req method = ConceptProto.Method.Req.newBuilder()
                .setAttributeOwnersReq(ConceptProto.Attribute.Owners.Req.getDefaultInstance()).build();

        int iteratorId = runMethod(method).getAttributeOwnersIter().getId();
        return conceptStream(tx(), iteratorId, res -> res.getAttributeOwnersIterRes().getThing()).map(RemoteConcept::asThing);
    }

    @Override
    public final DataType<D> dataType() {
        return type().dataType();
    }

    @SuppressWarnings("unchecked")
    @Override
    RemoteAttributeType<D> asCurrentType(RemoteConcept<?> concept) {
        return (RemoteAttributeType<D>) concept.asAttributeType();
    }

    @SuppressWarnings("unchecked")
    @Override
    final RemoteAttribute<D> asCurrentBaseType(RemoteConcept<?> other) {
        return (RemoteAttribute<D>) other.asAttribute();
    }
}
