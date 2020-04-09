package grakn.client.concept.type;

import grakn.client.GraknClient;
import grakn.client.concept.ConceptId;
import grakn.client.concept.Label;
import grakn.client.concept.Role;
import grakn.client.concept.SchemaConcept;
import grakn.client.concept.remote.RemoteMetaTypeImpl;
import grakn.client.concept.thing.Thing;
import grakn.client.concept.type.Type;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface MetaType<
        SomeType extends Type<SomeType, SomeThing>,
        SomeThing extends Thing<SomeThing, SomeType>>
        extends Type<SomeType, SomeThing> {
    //------------------------------------- Other ---------------------------------
    @Deprecated
    @CheckReturnValue
    @Override
    default MetaType<SomeType, SomeThing> asMetaType() {
        return this;
    }

    @Override
    default MetaType.Remote<SomeType, SomeThing> asRemote(GraknClient.Transaction tx) {
        return MetaType.Remote.of(tx, id());
    }

    @Deprecated
    @CheckReturnValue
    @Override
    default boolean isMetaType() {
        return true;
    }

    interface Local<
            SomeType extends Type<SomeType, SomeThing>,
            SomeThing extends Thing<SomeThing, SomeType>>
            extends Type.Local<SomeType, SomeThing>, MetaType<SomeType, SomeThing> {
    }

    /**
     * Type Class of a MetaType
     */
    interface Remote<
            SomeRemoteType extends Type<SomeRemoteType, SomeRemoteThing>,
            SomeRemoteThing extends Thing<SomeRemoteThing, SomeRemoteType>>
        extends MetaType<SomeRemoteType, SomeRemoteThing>,
            Type.Remote<SomeRemoteType, SomeRemoteThing> {

        static <SomeRemoteType extends Type<SomeRemoteType, SomeRemoteThing>,
                SomeRemoteThing extends Thing<SomeRemoteThing, SomeRemoteType>>
        MetaType.Remote<SomeRemoteType, SomeRemoteThing> of(GraknClient.Transaction tx, ConceptId id) {
            return new RemoteMetaTypeImpl<>(tx, id);
        }

        @Override
        default MetaType.Remote<SomeRemoteType, SomeRemoteThing> asMetaType() {
            return this;
        }
    }
}
