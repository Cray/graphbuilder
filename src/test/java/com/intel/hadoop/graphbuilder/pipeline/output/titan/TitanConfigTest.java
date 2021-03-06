/**
 * Copyright (C) 2013 Intel Corporation.
 *     All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more about this software visit:
 *     http://www.01.org/GraphBuilder
 */
package com.intel.hadoop.graphbuilder.pipeline.output.titan;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

import org.junit.Test;

import com.intel.hadoop.graphbuilder.util.RuntimeConfig;

public class TitanConfigTest {

    @Test
    public void testSingletonConfig() throws Exception {
        assertSame(TitanConfig.config, RuntimeConfig.getInstance(TitanConfig.class));

    }

    @Test
    public void testConstructor() throws Exception {

        // just a placeholder
        TitanConfig tc = new TitanConfig();

        assertNotNull(tc);
    }
}
