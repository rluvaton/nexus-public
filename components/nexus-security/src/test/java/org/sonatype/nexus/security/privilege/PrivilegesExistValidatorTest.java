/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.security.privilege;

import java.util.Collections;

import javax.validation.ConstraintValidatorContext;

import org.sonatype.nexus.security.SecuritySystem;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PrivilegesExistValidatorTest
{
  private PrivilegesExistValidator underTest;

  @Before
  public void setup() {
    SecuritySystem securitySystem = mock(SecuritySystem.class);
    when(securitySystem.listPrivileges()).thenReturn(Collections.emptySet());
    underTest = new PrivilegesExistValidator(securitySystem);
  }

  @Test
  public void isValid_ignoresJavaElExpression() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    when(context.buildConstraintViolationWithTemplate(any())).thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
    assertThat(underTest.isValid(Collections
            .singleton("dx27e${\"gggggggggggggggggggggggggggggggggggggggggggz\".toString().replace(\"g\", \"q\")}yv5rm"),
        context), is(false));
    //note the missing $
    verify(context).buildConstraintViolationWithTemplate(
        "Invalid privilege id: dx27e{\"gggggggggggggggggggggggggggggggggggggggggggz\".toString().replace(\"g\", \"q\")}yv5rm. Only letters, digits, underscores(_), hyphens(-), and dots(.) are allowed and may not start with underscore or dot.");
  }
}
