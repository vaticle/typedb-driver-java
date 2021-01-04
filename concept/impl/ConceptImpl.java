package grakn.client.concept.impl;

import grakn.client.common.exception.GraknClientException;
import grakn.client.concept.Concept;
import grakn.client.concept.thing.Attribute;
import grakn.client.concept.thing.Entity;
import grakn.client.concept.thing.Relation;
import grakn.client.concept.thing.Thing;
import grakn.client.concept.thing.impl.AttributeImpl;
import grakn.client.concept.thing.impl.EntityImpl;
import grakn.client.concept.thing.impl.RelationImpl;
import grakn.client.concept.thing.impl.ThingImpl;
import grakn.client.concept.type.AttributeType;
import grakn.client.concept.type.EntityType;
import grakn.client.concept.type.RelationType;
import grakn.client.concept.type.RoleType;
import grakn.client.concept.type.ThingType;
import grakn.client.concept.type.Type;
import grakn.client.concept.type.impl.AttributeTypeImpl;
import grakn.client.concept.type.impl.EntityTypeImpl;
import grakn.client.concept.type.impl.RelationTypeImpl;
import grakn.client.concept.type.impl.RoleTypeImpl;
import grakn.client.concept.type.impl.ThingTypeImpl;
import grakn.client.concept.type.impl.TypeImpl;

import static grakn.client.common.exception.ErrorMessage.Concept.INVALID_CONCEPT_CASTING;
import static grakn.common.util.Objects.className;

public abstract class ConceptImpl implements Concept {

    @Override
    public final boolean isRemote() {
        return false;
    }

    @Override
    public TypeImpl asType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Type.class)));
    }

    @Override
    public ThingTypeImpl asThingType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(ThingType.class)));
    }

    @Override
    public EntityTypeImpl asEntityType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(EntityType.class)));
    }

    @Override
    public AttributeTypeImpl asAttributeType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(AttributeType.class)));
    }

    @Override
    public RelationTypeImpl asRelationType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(RelationType.class)));
    }

    @Override
    public RoleTypeImpl asRoleType() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(RoleType.class)));
    }

    @Override
    public ThingImpl asThing() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Thing.class)));
    }

    @Override
    public EntityImpl asEntity() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Entity.class)));
    }

    @Override
    public AttributeImpl<?> asAttribute() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Attribute.class)));
    }

    @Override
    public RelationImpl asRelation() {
        throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Relation.class)));
    }

    public abstract static class Remote implements Concept.Remote {

        @Override
        public final boolean isRemote() {
            return true;
        }

        @Override
        public TypeImpl.Remote asType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Type.class)));
        }

        @Override
        public ThingTypeImpl.Remote asThingType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(ThingType.class)));
        }

        @Override
        public EntityTypeImpl.Remote asEntityType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(EntityType.class)));
        }

        @Override
        public RelationTypeImpl.Remote asRelationType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(RelationType.class)));
        }

        @Override
        public AttributeTypeImpl.Remote asAttributeType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(AttributeType.class)));
        }

        @Override
        public RoleTypeImpl.Remote asRoleType() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(RoleType.class)));
        }

        @Override
        public ThingImpl.Remote asThing() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Thing.class)));
        }

        @Override
        public EntityImpl.Remote asEntity() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Entity.class)));
        }

        @Override
        public RelationImpl.Remote asRelation() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Relation.class)));
        }

        @Override
        public AttributeImpl.Remote<?> asAttribute() {
            throw new GraknClientException(INVALID_CONCEPT_CASTING.message(className(this.getClass()), className(Attribute.class)));
        }
    }
}
