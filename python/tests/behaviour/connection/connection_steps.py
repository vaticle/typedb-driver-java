# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

from behave import *
from tests.behaviour.context import Context
from typedb.common.exception import TypeDBDriverException
from tests.behaviour.config.parameters import parse_int, parse_bool, parse_may_error


@step(u'typedb has configuration')
def step_impl(context: Context):
    # TODO: implement configuring the TypeDB runner when a python typedb-runner is available
    pass


@step(u'typedb starts')
def step_impl(context: Context):
    # TODO: start TypeDB via a python typedb-runner once one is available
    pass


@step(u'connection opens with default authentication')
def step_impl(context: Context):
    context.setup_context_driver_fn()


@step(u'connection opens with a wrong host{may_error}')
def step_impl(context: Context, may_error: str):
    may_error = parse_may_error(may_error)
    may_error.check(lambda: context.setup_context_driver_fn(host="surely-not-localhost"))


@step(u'connection opens with a wrong port{may_error}')
def step_impl(context: Context, may_error: str):
    may_error = parse_may_error(may_error)
    may_error.check(lambda: context.setup_context_driver_fn(port=0))


@step(u'connection opens with authentication: {username:Words}, {password:Words}')
def step_impl(context: Context, username: str, password: str):
    context.setup_context_driver_fn(username, password)


@step(u'connection opens with authentication: {username:Words}, {password:Words}; throws exception')
def step_impl(context: Context, username: str, password: str):
    try:
        context.setup_context_driver_fn(username, password)
        assert False
    except TypeDBDriverException:
        pass


@step(u'connection closes')
def step_impl(context: Context):
    context.driver.close()


@step(u'typedb stops')
def step_impl(context: Context):
    # TODO: stop TypeDB via a python typedb-runner once one is available
    pass


@step("connection is open: {is_open}")
def step_impl(context: Context, is_open: str):
    is_open = parse_bool(is_open)
    real_is_open = hasattr(context, 'driver') and context.driver and context.driver.is_open()
    assert is_open == real_is_open


@step("connection has {count} databases")
def step_impl(context: Context, count: str):
    count = parse_int(count)
    assert len(context.driver.databases.all()) == count
