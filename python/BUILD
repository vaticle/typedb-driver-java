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

load("@typedb_bazel_distribution//docs:python/rules.bzl", "sphinx_docs")
load("@typedb_dependencies//builder/swig:python.bzl", "py_native_lib_rename")
load("@typedb_dependencies//tool/checkstyle:rules.bzl", "checkstyle_test")
load("//python:rules.bzl", "native_driver_versioned")
load("//python:python_versions.bzl", "python_versions")
load("//python:docs_structure.bzl", "dir_mapping")
load("//tool/docs:python/rules.bzl",  "sphinx_to_adoc")
load("//tool/docs:examples/rules.bzl",  "test_to_example", "update_markdown_example")

native_driver_versioned(python_versions = python_versions)

alias(
    name = "driver_python",
    actual = ":driver_python39",
    visibility = ["//visibility:public"],
)

genrule(
    name = "native-driver-wrapper-link",
    outs = ["typedb/native_driver_wrapper.py"],
    srcs = [":native-driver-wrapper39"],
    cmd = "cp $< $@",
    visibility = ["//visibility:public"]
)

py_native_lib_rename(
    name = "native-driver-binary-link",
    out = "typedb/native_driver_python",
    src = ":native_driver_python39",
    visibility = ["//visibility:public"]
)

sphinx_docs(
    name = "docs_html",
    target = ":assemble-pip39",
    out = "driver_docs_sphinx",
    sphinx_conf = "conf.py",
    sphinx_rst = "index.rst",
    package_subdir = "typedb",
)

sphinx_to_adoc(
    name = "docs_adoc",
    data = [":docs_html"],
    docs_dirs = dir_mapping,
)

test_to_example(
    name = "example_core",
    input = "//python/tests/integration/core:test_example.py",
    output = "python/core_example.py",
    removed_lines = ["assert_that", "todo"],
    changed_words = {
        "test_example(self)": "typedb_example()",
        "TestExample(TestCase)": "TypeDBExample",
    },
    header_comment_sign = "#",
)

test_to_example(
    name = "example_cloud",
    input = "//python/tests/integration/cloud:test_example.py",
    output = "python/cloud_example.py",
    removed_lines = ["assert_that", "todo"],
    changed_words = {
        "test_example(self)": "typedb_example()",
        "TestExample(TestCase)": "TypeDBExample",
    },
    header_comment_sign = "#",
)

update_markdown_example(
    name = "readme_example_core",
    input = ":core_example.py",
    output = ":README.md",
    start_marker = "CORE_EXAMPLE_START_MARKER",
    end_marker = "CORE_EXAMPLE_END_MARKER",
    language_tag = "py",
    removed_header = "#",
)

update_markdown_example(
    name = "readme_example_cloud",
    input = ":cloud_example.py",
    output = ":README.md",
    start_marker = "CLOUD_EXAMPLE_START_MARKER",
    end_marker = "CLOUD_EXAMPLE_END_MARKER",
    language_tag = "py",
    removed_header = "#",
)

checkstyle_test(
    name = "checkstyle",
    include = glob([
        "*.bzl",
        ".bazelrc",
        ".factory/*",
        ".gitignore",
        "BUILD",
        "WORKSPACE",
        "requirements*.txt",
        "typedb/**/*",
    ]),
    exclude = glob([
        "conf.py",
        "index.rst",
        "docs/**/*.adoc",
        "core_example.py",
        "cloud_example.py",
    ]),
    license_type = "apache-header",
    size = "small",
)

# CI targets that are not declared in any BUILD_ file, but are called externally
filegroup(
    name = "ci",
    data = [
        "@typedb_dependencies//tool/checkstyle:test-coverage",
        "@typedb_dependencies//distribution/artifact:create-netrc",
        "@typedb_dependencies//tool/release/notes:create",
    ],
)
