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

using System.Collections;
using System.Collections.Generic;

using Vaticle.Typedb.Driver.Common;
using Vaticle.Typedb.Driver.Common.Validation;

using InternalError = Vaticle.Typedb.Driver.Common.Error.Internal;

namespace Vaticle.Typedb.Driver.Common
{
    public class NativeEnumerable<T> : IEnumerable<T>
    {
        private readonly IEnumerator<T> _enumerator;
        private bool _enumeratorUsed;

        public NativeEnumerable(IEnumerator<T> enumerator)
        {
            _enumerator = enumerator;
            _enumeratorUsed = false;
        }

        public IEnumerator<T> GetEnumerator()
        {
            // TODO: Maybe need to allow it (+ swig)!
            Validator.ThrowIfTrue(() => _enumeratorUsed, InternalError.ENUMERATOR_EXCESSIVE_ACCESS);

            _enumeratorUsed = true;
            return _enumerator;
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return this.GetEnumerator();
        }
    }
}
