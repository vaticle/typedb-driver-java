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
from typedb.driver import *
from tests.behaviour.config.parameters import parse_may_error


@step("typeql define{may_error}")
def step_impl(context: Context, may_error: str):
    may_error = parse_may_error(may_error)
    may_error.check(lambda: context.tx().query(query=context.text).resolve())

# @step("typeql insert")
# def step_impl(context: Context):
#     list(context.tx().query.insert(query=context.text))
#
#
# @step("typeql insert; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(next).with_args(context.tx().query.insert(query=context.text)), raises(TypeDBDriverException))
#
#
# @step("typeql insert; throws exception containing \"{pattern}\"")
# def step_impl(context: Context, pattern: str):
#     assert_that(calling(next).with_args(context.tx().query.insert(query=context.text)),
#                 raises(TypeDBDriverException, re.escape(pattern)))
#
#
# @step("typeql delete")
# def step_impl(context: Context):
#     context.tx().query.delete(query=context.text)
#
#
# @step("typeql delete; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(lambda query: context.tx().query.delete(query).resolve()).with_args(query=context.text), raises(TypeDBDriverException))
#
#
# @step("typeql delete; throws exception containing \"{pattern}\"")
# def step_impl(context: Context, pattern: str):
#     assert_that(calling(lambda query: context.tx().query.delete(query).resolve()).with_args(query=context.text),
#                 raises(TypeDBDriverException, re.escape(pattern)))
#
#
# @step("typeql update")
# def step_impl(context: Context):
#     context.tx().query.update(query=context.text)
#
#
# @step("typeql update; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(next).with_args(context.tx().query.update(query=context.text)), raises(TypeDBDriverException))
#
#
# @step("typeql update; throws exception containing \"{pattern}\"")
# def step_impl(context: Context, pattern: str):
#     assert_that(calling(next).with_args(context.tx().query.update(query=context.text)),
#                 raises(TypeDBDriverException, re.escape(pattern)))
#
#
# @step("get answers of typeql insert")
# def step_impl(context: Context):
#     context.clear_answers()
#     context.answers = [answer for answer in context.tx().query.insert(query=context.text)]
#
#
# @step("get answers of typeql get")
# def step_impl(context: Context):
#     context.clear_answers()
#     context.answers = [answer for answer in context.tx().query.get(query=context.text)]
#
#
# @step("typeql get; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(next).with_args(context.tx().query.get(query=context.text)), raises(TypeDBDriverException))
#
#
# @step("typeql get; throws exception containing \"{pattern}\"")
# def step_impl(context: Context, pattern: str):
#     assert_that(calling(next).with_args(context.tx().query.get(query=context.text)),
#                 raises(TypeDBDriverException, re.escape(pattern)))
#
#
# @step("get answer of typeql get aggregate")
# def step_impl(context: Context):
#     context.clear_answers()
#     context.value_answer = context.tx().query.get_aggregate(query=context.text).resolve()
#
#
# @step("typeql get aggregate; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(lambda query: context.tx().query.get_aggregate(query).resolve()).with_args(query=context.text), raises(TypeDBDriverException))
#
#
# @step("get answers of typeql get group")
# def step_impl(context: Context):
#     context.clear_answers()
#     context.answer_groups = [group for group in context.tx().query.get_group(query=context.text)]
#
#
# @step("typeql get group; throws exception")
# def step_impl(context: Context):
#     assert_that(calling(next).with_args(context.tx().query.get_group(query=context.text)),
#                 raises(TypeDBDriverException))
#
#
# @step("get answers of typeql get group aggregate")
# def step_impl(context: Context):
#     context.clear_answers()
#     context.value_answer_groups = [group for group in context.tx().query.get_group_aggregate(query=context.text)]
#
#
# @step("answer size is: {expected_size:Int}")
# def step_impl(context: Context, expected_size: int):
#     assert_that(context.answers, has_length(expected_size),
#                 "Expected [%d] answers, but got [%d]" % (expected_size, len(context.answers)))
#
# @step("get answers of typeql fetch")
# def get_answers_typeql_fetch(context: Context):
#     context.fetch_answers = list(context.tx().query.fetch(query=context.text))
#
# @step("typeql fetch; throws exception")
# def typeql_fetch_throws(context: Context):
#     assert_that(calling(next).with_args(context.tx().query.fetch(query=context.text)),
#                 raises(TypeDBDriverException))
#
# @step("fetch answers are")
# def fetch_answers_are(context: Context):
#     expected = json.loads(context.text)
#     actual = context.fetch_answers
#     assert_that(json_matches(expected, actual), is_(True),
#                 "expected: {}\nactual: {}".format(expected, actual))
#
# @step("rules contain: {rule_label}")
# def step_impl(context: Context, rule_label: str):
#     return rule_label in [rule.label for rule in context.tx().logic.get_rules()]
#
#
# @step("rules do not contain: {rule_label}")
# def step_impl(context: Context, rule_label: str):
#     return not (rule_label in [rule.label for rule in context.tx().logic.get_rules()])
#
#
# class ConceptMatchResult:
#
#     def __init__(self, expected: str, actual: Optional[str], error: str = None):
#         self.matches = actual == expected
#         self.actual = actual
#         self.expected = expected
#         self.error = error
#
#     @staticmethod
#     def of(expected: Any, actual: Any):
#         return ConceptMatchResult(expected=str(expected), actual=str(actual))
#
#     @staticmethod
#     def of_error(expected: Any, error: str):
#         return ConceptMatchResult(expected=str(expected), actual=None, error=error)
#
#     def __str__(self):
#         if self.error:
#             return "[error: %s]" % self.error
#         else:
#             return "[expected: %s, actual: %s, matches: %s]" % (self.expected, self.actual, self.matches)
#
#
# class ConceptMatcher(ABC):
#
#     @abstractmethod
#     def match(self, context: Context, concept: Concept) -> ConceptMatchResult:
#         pass
#
#
# class TypeLabelMatcher(ConceptMatcher):
#
#     def __init__(self, label: str):
#         self.label = parse_label(label)
#
#     def match(self, context: Context, concept: Concept):
#         if concept.is_type():
#             return ConceptMatchResult.of(self.label, concept.as_type().get_label())
#         else:
#             return ConceptMatchResult.of_error(self.label, "%s was matched by Label, but it is not a Type." % concept)
#
#
# class AttributeMatcher(ConceptMatcher, ABC):
#
#     def __init__(self, type_and_value: str):
#         self.type_and_value = type_and_value
#         s = type_and_value.split(":", 1)
#         assert_that(s, has_length(2),
#                     "[%s] is not a valid attribute identifier. It should have format \"type_label:value\"." % type_and_value)
#         self.type_label, self.value_string = s
#
#     def check(self, attribute: Attribute):
#         if attribute.is_boolean():
#             return ConceptMatchResult.of(parse_bool(self.value_string), attribute.as_boolean())
#         elif attribute.is_long():
#             return ConceptMatchResult.of(parse_int(self.value_string), attribute.as_long())
#         elif attribute.is_double():
#             return ConceptMatchResult.of(parse_float(self.value_string), attribute.as_double())
#         elif attribute.is_string():
#             return ConceptMatchResult.of(self.value_string, attribute.as_string())
#         elif attribute.is_datetime():
#             return ConceptMatchResult.of(parse_datetime(self.value_string), attribute.as_datetime())
#         else:
#             raise ValueError("Unrecognised value type " + str(type(attribute)))
#
#
# class AttributeValueMatcher(AttributeMatcher):
#
#     def match(self, context: Context, concept: Concept):
#         if not concept.is_attribute():
#             return ConceptMatchResult.of_error(self.type_and_value,
#                                                "%s was matched by Attribute Value, but it is not an Attribute." % concept)
#
#         attribute = concept.as_attribute()
#
#         if self.type_label != attribute.get_type().get_label().name:
#             return ConceptMatchResult.of_error(self.type_and_value,
#                                                "%s was matched by Attribute Value expecting type label [%s], but its actual type is %s." % (
#                                                    attribute, self.type_label, attribute.get_type()))
#
#         return self.check(attribute)
#
#
# class ThingKeyMatcher(AttributeMatcher):
#
#     def match(self, context: Context, concept: Concept):
#         if not concept.is_thing():
#             return ConceptMatchResult.of_error(self.type_and_value,
#                                                "%s was matched by Key, but it is not a Thing." % concept)
#
#         keys = [key for key in concept.as_thing().get_has(context.tx(), annotations={Annotation.key()})]
#
#         for key in keys:
#             if key.get_type().get_label().name == self.type_label:
#                 return self.check(key)
#
#         return ConceptMatchResult.of_error(self.type_and_value,
#                                            "%s was matched by Key expecting key type [%s], but it doesn't own any key of that type." % (
#                                                concept, self.type_label))
#
#
# class ValueMatcher(ConceptMatcher):
#
#     def __init__(self, value_type_and_value: str):
#         self.value_type_and_value = value_type_and_value
#         s = value_type_and_value.split(":", 1)
#         assert_that(s, has_length(2),
#                     "[%s] is not a valid identifier. It should have format \"value_type:value\"." % value_type_and_value)
#         self.value_type_name, self.value_string = s
#
#     def match(self, context: Context, concept: Concept):
#         if not concept.is_value():
#             return ConceptMatchResult.of_error(self.value_type_and_value,
#                                                "%s was matched by Value, but it is not Value." % concept)
#
#         value = concept.as_value()
#
#         value_type = parse_value_type(self.value_type_name)
#         if value_type != value.get_value_type():
#             return ConceptMatchResult.of_error(self.value_type_and_value,
#                                                "%s was matched by Value expecting value type [%s], but its actual value type is %s." % (
#                                                    value, value_type, value.get_value_type()))
#
#         return self.check(value)
#
#     def check(self, value: Value):
#         if value.is_boolean():
#             return ConceptMatchResult.of(parse_bool(self.value_string), value.get())
#         elif value.is_long():
#             return ConceptMatchResult.of(parse_int(self.value_string), value.get())
#         elif value.is_double():
#             return ConceptMatchResult.of(parse_float(self.value_string), value.get())
#         elif value.is_string():
#             return ConceptMatchResult.of(self.value_string, value.get())
#         elif value.is_datetime():
#             return ConceptMatchResult.of(parse_datetime(self.value_string), value.get())
#         else:
#             raise ValueError("Unrecognised value type " + str(type(value)))
#
#
# def parse_concept_identifier(value: str):
#     identifier_type, identifier_body = value.split(":", 1)
#     if identifier_type == "label":
#         return TypeLabelMatcher(label=identifier_body)
#     elif identifier_type == "key":
#         return ThingKeyMatcher(type_and_value=identifier_body)
#     elif identifier_type == "attr":
#         return AttributeValueMatcher(type_and_value=identifier_body)
#     elif identifier_type == "value":
#         return ValueMatcher(value_type_and_value=identifier_body)
#     else:
#         raise ValueError("Failed to parse concept identifier: " + value)
#
#
# class AnswerMatchResult:
#
#     def __init__(self, concept_match_results: list[ConceptMatchResult]):
#         self.concept_match_results = concept_match_results
#
#     def matches(self):
#         for result in self.concept_match_results:
#             if not result.matches:
#                 return False
#         return True
#
#     def __str__(self):
#         return "[matches: %s, concept_match_results: %s]" % (
#             self.matches(), [str(x) for x in self.concept_match_results])
#
#
# def match_answer_concepts(context: Context, answer_identifier: list[tuple[str, str]],
#                           answer: ConceptRow) -> AnswerMatchResult:
#     results = []
#     for var, concept_identifier in answer_identifier:
#         matcher = parse_concept_identifier(concept_identifier)
#         result = matcher.match(context, answer.get(var))
#         results.append(result)
#     return AnswerMatchResult(results)
#
#
# @step("uniquely identify answer concepts")
# def step_impl(context: Context):
#     answer_identifiers = parse_table(context.table)
#     assert_that(context.answers, has_length(len(answer_identifiers)),
#                 "The number of answers [%d] should match the number of answer identifiers [%d]." % (
#                     len(context.answers), len(answer_identifiers)))
#
#     result_set = [(ai, [], []) for ai in answer_identifiers]
#     for answer_identifier, matched_answers, match_attempts in result_set:
#         for answer in context.answers:
#             result = match_answer_concepts(context, answer_identifier, answer)
#             match_attempts.append(result)
#             if result.matches():
#                 matched_answers.append(answer)
#         assert_that(matched_answers, has_length(1),
#                     "Each answer identifier should match precisely 1 answer, but [%d] answers matched the identifier [%s].\nThe match results were: %s" % (
#                         len(matched_answers), answer_identifier, [str(x) for x in match_attempts]))
#
#     for answer in context.answers:
#         matches = 0
#         for answer_identifier, matched_answers, match_attempts in result_set:
#             if answer in matched_answers:
#                 matches += 1
#                 break
#         if matches != 1:
#             match_attempts = []
#             for answer_identifier in answer_identifiers:
#                 match_attempts.append(match_answer_concepts(context, answer_identifier, answer))
#             assert_that(matches, is_(1),
#                         "Each answer should match precisely 1 answer identifier, but [%d] answer identifiers matched the answer [%s].\nThe match results were: %s" % (
#                             matches, answer, [str(x) for x in match_attempts]))
#
#
# @step("order of answer concepts is")
# def step_impl(context: Context):
#     answer_identifiers = parse_table(context.table)
#     assert_that(context.answers, has_length(len(answer_identifiers)),
#                 "The number of answers [%d] should match the number of answer identifiers [%d]." % (
#                     len(context.answers), len(answer_identifiers)))
#     for i in range(len(context.answers)):
#         answer = context.answers[i]
#         answer_identifier = answer_identifiers[i]
#         result = match_answer_concepts(context, answer_identifier, answer)
#         assert_that(result.matches(), is_(True),
#                     "The answer at index [%d] does not match the identifier [%s].\nThe match results were: %s" % (
#                         i, answer_identifier, result))
#
#
# def assert_value(value: Value, expected_answer: Union[int, float], reason: Optional[str] = None):
#     if value.is_long():
#         assert_that(value.as_long(), is_(expected_answer), reason)
#     elif value.is_double():
#         assert_that(value.as_double(), is_(close_to(expected_answer, delta=0.001)), reason)
#     else:
#         assert False
#
#
# @step("aggregate value is: {expected_answer:Float}")
# def step_impl(context: Context, expected_answer: float):
#     assert_value(context.value_answer, expected_answer)
#
#
# @step("aggregate answer is empty")
# def step_impl(context: Context):
#     assert_that(context.value_answer is None)
#
#
# class AnswerIdentifierGroup:
#     GROUP_COLUMN_NAME = "owner"
#
#     def __init__(self, raw_answer_identifiers: list[list[tuple[str, str]]]):
#         self.owner_identifier = next(
#             entry[1] for entry in raw_answer_identifiers[0] if entry[0] == self.GROUP_COLUMN_NAME)
#         self.answer_identifiers = [[(var, concept_identifier) for (var, concept_identifier) in raw_answer_identifier if
#                                     var != self.GROUP_COLUMN_NAME]
#                                    for raw_answer_identifier in raw_answer_identifiers]
#
#
# @step("answer groups are")
# def step_impl(context: Context):
#     raw_answer_identifiers = parse_table(context.table)
#     grouped_answer_identifiers = defaultdict(list)
#     for raw_answer_identifier in raw_answer_identifiers:
#         owner = next(entry[1] for entry in raw_answer_identifier if entry[0] == AnswerIdentifierGroup.GROUP_COLUMN_NAME)
#         grouped_answer_identifiers[owner].append(raw_answer_identifier)
#     answer_identifier_groups = [AnswerIdentifierGroup(raw_identifiers) for raw_identifiers in
#                                 grouped_answer_identifiers.values()]
#
#     assert_that(context.answer_groups, has_length(len(answer_identifier_groups)),
#                 "Expected [%d] answer groups, but found [%d]." % (
#                     len(answer_identifier_groups), len(context.answer_groups)))
#
#     for answer_identifier_group in answer_identifier_groups:
#         identifier = parse_concept_identifier(answer_identifier_group.owner_identifier)
#         answer_group = next(
#             (group for group in context.answer_groups if identifier.match(context, group.owner()).matches), None)
#         assert_that(answer_group is not None,
#                     reason="The group identifier [%s] does not match any of the answer group owners." % answer_identifier_group.owner_identifier)
#
#         result_set = [(ai, [], []) for ai in answer_identifier_group.answer_identifiers]
#         for answer_identifier, matched_answers, match_attempts in result_set:
#             for answer in answer_group.concept_rows():
#                 result = match_answer_concepts(context, answer_identifier, answer)
#                 match_attempts.append(result)
#                 if result.matches():
#                     matched_answers.append(answer)
#             assert_that(matched_answers, has_length(1),
#                         "Each answer identifier should match precisely 1 answer, but [%d] answers matched the identifier [%s].\nThe match results were: %s" % (
#                             len(matched_answers), answer_identifier, [str(x) for x in match_attempts]))
#
#         for answer in answer_group.concept_rows():
#             matches = 0
#             for answer_identifier, matched_answers, match_attempts in result_set:
#                 if answer in matched_answers:
#                     matches += 1
#                     break
#             if matches != 1:
#                 match_attempts = []
#                 for answer_identifier in answer_identifier_group.answer_identifiers:
#                     match_attempts.append(match_answer_concepts(context, answer_identifier, answer))
#                 assert_that(matches, is_(1),
#                             "Each answer should match precisely 1 answer identifier, but [%d] answer identifiers matched the answer [%s].\nThe match results were: %s" % (
#                                 matches, answer, [str(x) for x in match_attempts]))
#
#
# @step("group aggregate values are")
# def step_impl(context: Context):
#     raw_answer_identifiers = parse_table(context.table)
#     expectations = {}
#     for raw_answer_identifier in raw_answer_identifiers:
#         owner = next(entry[1] for entry in raw_answer_identifier if entry[0] == AnswerIdentifierGroup.GROUP_COLUMN_NAME)
#         expected_answer = parse_float(next(entry[1] for entry in raw_answer_identifier if entry[0] == "value"))
#         expectations[owner] = expected_answer
#
#     assert_that(context.value_answer_groups, has_length(len(expectations)),
#                 reason="Expected [%d] answer groups, but found [%d]." % (
#                     len(expectations), len(context.value_answer_groups)))
#
#     for (owner_identifier, expected_answer) in expectations.items():
#         identifier = parse_concept_identifier(owner_identifier)
#         value_group = next(
#             (group for group in context.value_answer_groups if identifier.match(context, group.owner()).matches),
#             None)
#         assert_that(value_group is not None,
#                     reason="The group identifier [%s] does not match any of the answer group owners." % owner_identifier)
#
#         actual_answer = value_group.value().get()
#         assert_value(value_group.value(), expected_answer,
#                              reason="Expected answer [%f] for group [%s], but got [%f]" % (
#                                  expected_answer, owner_identifier, actual_answer))
#
#
# @step("group aggregate answer value is empty")
# def step_impl(context: Context):
#     assert_that(len(context.value_answer_groups) == 1, reason="This step only work with 1 group aggregate answer")
#     assert_that(context.value_answer_groups[0].value() is None)
#
#
# def variable_from_template_placeholder(placeholder: str):
#     if placeholder.endswith(".iid"):
#         return placeholder.replace(".iid", "").replace("answer.", "")
#     else:
#         raise ValueError("Cannot replace template not based on IID.")
#
#
# def apply_query_template(template: str, answer: ConceptRow):
#     query = ""
#     matches = re.finditer(r"<(.+?)>", template)
#     i = 0
#     for match in matches:
#         required_variable = variable_from_template_placeholder(match.group(1))
#         query += template[i:match.span()[0]]
#         if required_variable in answer.column_names():
#             concept = answer.get(required_variable)
#             if not concept.is_thing():
#                 raise TypeError("Cannot apply IID templating to Types")
#             query += concept.as_thing().get_iid()
#         else:
#             raise ValueError("No IID available for template placeholder: [%s]" % match.group())
#         i = match.span()[1]
#     query += template[i:]
#     return query
#
#
# # TODO: This step seems needlessly complex for what it's actually used for
# @step("each answer satisfies")
# def step_impl(context: Context):
#     for answer in context.answers:
#         query = apply_query_template(template=context.text, answer=answer)
#         assert_that(list(context.tx().query.get(query)), has_length(1))
#
#
# @step("get answers of templated typeql get")
# def step_impl(context: Context):
#     if len(context.answers) > 1:
#         raise ValueError("Can only retrieve answers of templated typeql get given 1 previous answer")
#     answer = context.answers[0]
#     query = apply_query_template(template=context.text, answer=answer)
#     context.clear_answers()
#     context.answers = [answer for answer in context.tx().query.get(query=query)]
#
#
# @step("templated typeql get; throws exception")
# def step_impl(context: Context):
#     for answer in context.answers:
#         query = apply_query_template(template=context.text, answer=answer)
#         assert_that(calling(list).with_args(context.tx().query.get(query)), raises(TypeDBDriverException))
