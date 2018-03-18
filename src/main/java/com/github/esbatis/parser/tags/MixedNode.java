/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.esbatis.parser.tags;

import com.github.esbatis.parser.DynamicContext;

import java.util.List;

/**
 * @author Clinton Begin
 */
public class MixedNode implements XmlNode {
  private final List<XmlNode> contents;

  public MixedNode(List<XmlNode> contents) {
    this.contents = contents;
  }

  @Override
  public boolean apply(DynamicContext context) {
    for (XmlNode sqlNode : contents) {
      sqlNode.apply(context);
    }
    return true;
  }
}