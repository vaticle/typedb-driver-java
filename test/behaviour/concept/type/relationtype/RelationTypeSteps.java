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

package grakn.client.test.behaviour.concept.type.relationtype;

import grakn.client.concept.type.RoleType;
import grakn.client.concept.type.Type;
import grakn.client.test.behaviour.config.Parameters;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Set;

import static grakn.client.test.behaviour.connection.ConnectionSteps.tx;
import static grakn.client.test.behaviour.util.Util.assertThrows;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RelationTypeSteps {

    @When("relation\\( ?{type_label} ?) set relates role: {type_label}")
    public void relation_type_set_relates_role(String relationLabel, String roleLabel) {
        tx().concepts().getRelationType(relationLabel).asRemote(tx()).setRelates(roleLabel);
    }

    @When("relation\\( ?{type_label} ?) set relates role: {type_label}; throws exception")
    public void thing_set_relates_role_throws_exception(String relationLabel, String roleLabel) {
        assertThrows(() -> tx().concepts().getRelationType(relationLabel).asRemote(tx()).setRelates(roleLabel));
    }

    @When("relation\\( ?{type_label} ?) unset related role: {type_label}")
    public void relation_type_unset_related_role(String relationLabel, String roleLabel) {
        tx().concepts().getRelationType(relationLabel).asRemote(tx()).unsetRelates(roleLabel);
    }

    @When("relation\\( ?{type_label} ?) unset related role: {type_label}; throws exception")
    public void relation_type_unset_related_role_throws_exception(final String relationLabel, final String roleLabel) {
        assertThrows(() -> relation_type_unset_related_role(relationLabel, roleLabel));
    }

    @When("relation\\( ?{type_label} ?) set relates role: {type_label} as {type_label}")
    public void relation_type_set_relates_role_type_as(String relationLabel, String roleLabel, String superRole) {
        tx().concepts().getRelationType(relationLabel).asRemote(tx()).setRelates(roleLabel, superRole);
    }

    @When("relation\\( ?{type_label} ?) set relates role: {type_label} as {type_label}; throws exception")
    public void thing_set_relates_role_type_as_throws_exception(String relationLabel, String roleLabel, String superRole) {
        assertThrows(() -> tx().concepts().getRelationType(relationLabel).asRemote(tx()).setRelates(roleLabel, superRole));
    }

    @When("relation\\( ?{type_label} ?) remove related role: {type_label}")
    public void relation_type_remove_related_role(String relationLabel, String roleLabel) {
        tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).delete();
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) is null: {bool}")
    public void relation_type_get_role_type_is_null(String relationLabel, String roleLabel, boolean isNull) {
        assertEquals(isNull, isNull(tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel)));
    }

    @When("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) set label: {type_label}")
    public void relation_type_get_role_type_set_label(String relationLabel, String roleLabel, String newLabel) {
        tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).setLabel(newLabel);
    }

    @When("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get label: {type_label}")
    public void relation_type_get_role_type_get_label(String relationLabel, String roleLabel, String getLabel) {
        assertEquals(getLabel, tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).getLabel());
    }

    @When("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) is abstract: {bool}")
    public void relation_type_get_role_type_is_abstract(String relationLabel, String roleLabel, boolean isAbstract) {
        assertEquals(isAbstract, tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).isAbstract());
    }

    private Set<Parameters.ScopedLabel> relation_type_get_related_roles_actuals(String relationLabel) {
        return tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates()
                .map(role -> new Parameters.ScopedLabel(role.getScope(), role.getLabel())).collect(toSet());
    }

    @Then("relation\\( ?{type_label} ?) get related roles contain:")
    public void relation_type_get_related_roles_contain(String relationLabel, List<Parameters.ScopedLabel> roleLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_related_roles_actuals(relationLabel);
        assertTrue(actuals.containsAll(roleLabels));
    }

    @Then("relation\\( ?{type_label} ?) get related roles do not contain:")
    public void relation_type_get_related_roles_do_not_contain(String relationLabel, List<Parameters.ScopedLabel> roleLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_related_roles_actuals(relationLabel);
        for (Parameters.ScopedLabel roleLabel : roleLabels) {
            assertFalse(actuals.contains(roleLabel));
        }
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get supertype: {scoped_label}")
    public void relation_type_get_role_type_get_supertype(String relationLabel, String roleLabel, Parameters.ScopedLabel superLabel) {
        RoleType superType = tx().concepts().getRelationType(superLabel.scope()).asRemote(tx()).getRelates(superLabel.role());
        assertEquals(superType, tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).getSupertype());
    }

    private Set<Parameters.ScopedLabel> relation_type_get_role_type_supertypes_actuals(String relationLabel, String roleLabel) {
        return tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).getSupertypes()
                .map(role -> new Parameters.ScopedLabel(role.getScope(), role.getLabel())).collect(toSet());
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get supertypes contain:")
    public void relation_type_get_role_type_get_supertypes_contain(String relationLabel, String roleLabel, List<Parameters.ScopedLabel> superLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_role_type_supertypes_actuals(relationLabel, roleLabel);
        assertTrue(actuals.containsAll(superLabels));
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get supertypes do not contain:")
    public void relation_type_get_role_type_get_supertypes_do_not_contain(String relationLabel, String roleLabel, List<Parameters.ScopedLabel> superLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_role_type_supertypes_actuals(relationLabel, roleLabel);
        for (Parameters.ScopedLabel superLabel : superLabels) {
            assertFalse(actuals.contains(superLabel));
        }
    }

    private Set<String> relation_type_get_role_type_players_actuals(String relationLabel, String roleLabel) {
        return tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).getPlayers().map(Type::getLabel).collect(toSet());
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get players contain:")
    public void relation_type_get_role_type_get_players_contain(String relationLabel, String roleLabel, List<String> playerLabels) {
        Set<String> actuals = relation_type_get_role_type_players_actuals(relationLabel, roleLabel);
        assertTrue(actuals.containsAll(playerLabels));
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get players do not contain:")
    public void relation_type_get_role_type_get_plays_do_not_contain(String relationLabel, String roleLabel, List<String> playerLabels) {
        Set<String> actuals = relation_type_get_role_type_players_actuals(relationLabel, roleLabel);
        for (String superLabel : playerLabels) {
            assertFalse(actuals.contains(superLabel));
        }
    }

    private Set<Parameters.ScopedLabel> relation_type_get_role_type_subtypes_actuals(String relationLabel, String roleLabel) {
        return tx().concepts().getRelationType(relationLabel).asRemote(tx()).getRelates(roleLabel).asRemote(tx()).getSubtypes()
                .map(role -> new Parameters.ScopedLabel(role.getScope(), role.getLabel())).collect(toSet());
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get subtypes contain:")
    public void relation_type_get_role_type_get_subtypes_contain(String relationLabel, String roleLabel, List<Parameters.ScopedLabel> subLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_role_type_subtypes_actuals(relationLabel, roleLabel);
        assertTrue(actuals.containsAll(subLabels));
    }

    @Then("relation\\( ?{type_label} ?) get role\\( ?{type_label} ?) get subtypes do not contain:")
    public void relation_type_get_role_type_get_subtypes_do_not_contain(String relationLabel, String roleLabel, List<Parameters.ScopedLabel> subLabels) {
        Set<Parameters.ScopedLabel> actuals = relation_type_get_role_type_subtypes_actuals(relationLabel, roleLabel);
        System.out.println(actuals);
        for (Parameters.ScopedLabel subLabel : subLabels) {
            assertFalse(actuals.contains(subLabel));
        }
    }
}
