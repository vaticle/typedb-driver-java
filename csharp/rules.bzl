#
# Copyright (C) 2022 Vaticle
#
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
#

load("@rules_dotnet//dotnet:defs.bzl", "csharp_library", "csharp_test")
load("@vaticle_dependencies//builder/swig:csharp.bzl", "swig_csharp")
load("@bazel_skylib//rules:copy_file.bzl", "copy_file")

def swig_native_csharp_library(name, target_frameworks, targeting_packs, tags=[], **kwargs):
    swig_csharp(
        name = "__" + name,
        target_frameworks = target_frameworks,
        targeting_packs = targeting_packs,
        shared_lib_name = name,
        tags = tags,
        **kwargs,
    )

    native.alias(
        name = name,
        actual = "__" + name
    )


def csharp_behaviour_test(name, steps, features, deps, target_frameworks, targeting_packs, **kwargs):
    csharp_test(
        name = name,
        srcs = steps + ["//csharp/test/behaviour/util:TestRunner.cs"],
        data = features,
        deps = deps + [
            "@paket.csharp_deps//gherkin",
            "@paket.csharp_deps//xunit.runner.utility",
            "@paket.csharp_deps//xunit.gherkin.quick",
        ],
        target_frameworks = target_frameworks,
        targeting_packs = targeting_packs,
        runtime_identifier = "any",
        visibility = ["//visibility:public"],
        **kwargs,
    )
