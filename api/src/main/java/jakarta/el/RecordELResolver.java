/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.el;

import static jakarta.el.ELUtil.getExceptionMessageString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Defines property resolution behavior on instances of {@link Record}.
 * <p>
 * The resolver handles base objects of type {@link Record}. It accepts any non-{@code null} object as a property and
 * coerces it to a String using {@link Object#toString()}. The property string is used to find an accessor method for a
 * field with the same name.
 * <p>
 * This resolver is always read-only since {@link Record}s are always read-only.
 * <p>
 * {@code ELResolver}s are combined together using {@link CompositeELResolver}s to define rich semantics for evaluating
 * an expression. See the javadocs for {@link ELResolver} for details.
 */
public class RecordELResolver extends ELResolver {

    /**
     * If the base object is an instance of {@link Record}, returns the value of the given field of this {@link Record}.
     * <p>
     * If the base object is an instance of {@link Record}, the {@code propertyResolved} property of the provided
     * {@link ELContext} must be set to {@code true} by this resolver before returning. If this property is not {@code
     * true} after this method is called, the caller should ignore the return value.
     *
     * @param context  The context of this evaluation.
     * @param base     The {@link Record} on which to get the property.
     * @param property The property to get. Will be coerced to a String.
     *
     * @return If the {@code propertyResolved} property of the provided {@link ELContext} was set to {@code true} then
     *             the value of the given property. Otherwise, undefined.
     *
     * @throws NullPointerException      if the provided {@link ELContext} is {@code null}.
     * @throws PropertyNotFoundException if the {@code base} is an instance of {@link Record} and the specified property
     *                                       does not exist.
     * @throws ELException               if an exception was throws while performing the property resolution. The thrown
     *                                       exception must be included as the cause of this exception, if available.
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        Objects.requireNonNull(context);

        if (base instanceof Record && property != null) {
            context.setPropertyResolved(base, property);

            String propertyName = property.toString();

            Method method;
            try {
                method = base.getClass().getMethod(propertyName);
            } catch (NoSuchMethodException nsme) {
                throw new PropertyNotFoundException(
                        getExceptionMessageString(context, "propertyNotReadable", new Object[] { base.getClass().getName(), property.toString() }));
            }

            try {
                return method.invoke(base);
            } catch (ELException ex) {
                throw ex;
            } catch (InvocationTargetException ite) {
                throw new ELException(ite.getCause());
            } catch (Exception ex) {
                throw new ELException(ex);
            }
        }
        return null;
    }


    /**
     * If the base object is an instance of {@link Record}, always returns {@code null} since {@link Record}s are always
     * read-only.
     * <p>
     * If the base object is an instance of {@link Record}, the {@code propertyResolved} property of the provided
     * {@link ELContext} must be set to {@code true} by this resolver before returning. If this property is not {@code
     * true} after this method is called, the caller should ignore the return value.
     *
     * @param context  The context of this evaluation.
     * @param base     The {@link Record} to analyze.
     * @param property The name of the property to analyze. Will be coerced to a String.
     *
     * @return Always {@code null}
     *
     * @throws NullPointerException      if the provided {@link ELContext} is {@code null}.
     * @throws PropertyNotFoundException if the {@code base} is an instance of {@link Record} and the specified property
     *                                       does not exist.
     */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        Objects.requireNonNull(context);
        if (base instanceof Record && property != null) {
            context.setPropertyResolved(base, property);

            String propertyName = property.toString();

            try {
                base.getClass().getMethod(propertyName);
            } catch (NoSuchMethodException nsme) {
                throw new PropertyNotFoundException(
                        getExceptionMessageString(context, "propertyNotReadable", new Object[] { base.getClass().getName(), property.toString() }));
            }
        }
        return null;
    }


    /**
     * If the base object is an instance of {@link Record}, always throws an exception since {@link Record}s are
     * read-only.
     * <p>
     * If the base object is an instance of {@link Record}, the {@code propertyResolved} property of the provided
     * {@link ELContext} must be set to {@code true} by this resolver before returning. If this property is not {@code
     * true} after this method is called, the caller should ignore the return value.
     *
     * @param context  The context of this evaluation.
     * @param base     The {@link Record} on which to set the property.
     * @param property The name of the property to set. Will be coerced to a String.
     *
     * @throws NullPointerException         if the provided {@link ELContext} is {@code null}.
     * @throws PropertyNotFoundException    if the {@code base} is an instance of {@link Record} and the specified
     *                                          property does not exist.
     * @throws PropertyNotWritableException if the {@code base} is an instance of {@link Record} and the specified
     *                                          property exists.
     */
    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        Objects.requireNonNull(context);
        if (base instanceof Record && property != null) {
            context.setPropertyResolved(base, property);

            String propertyName = property.toString();

            try {
                base.getClass().getMethod(propertyName);
            } catch (NoSuchMethodException nsme) {
                throw new PropertyNotFoundException(
                        getExceptionMessageString(context, "propertyNotFound", new Object[] { base.getClass().getName(), property.toString() }));
            }

            throw new PropertyNotWritableException(
                    getExceptionMessageString(context, "propertyNotWritable", new Object[] { base.getClass().getName(), property.toString() }));
        }
    }


    /**
     * If the base object is an instance of {@link Record}, always returns {@code true}.
     * <p>
     * If the base object is an instance of {@link Record}, the {@code propertyResolved} property of the provided
     * {@link ELContext} must be set to {@code true} by this resolver before returning. If this property is not {@code
     * true} after this method is called, the caller should ignore the return value.
     *
     * @param context  The context of this evaluation.
     * @param base     The {@link Record} to analyze.
     * @param property The name of the property to analyze. Will be coerced to a String.
     *
     * @throws NullPointerException      if the provided {@link ELContext} is {@code null}.
     * @throws PropertyNotFoundException if the {@code base} is an instance of {@link Record} and the specified property
     *                                       does not exist.
     */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        Objects.requireNonNull(context);
        if (base instanceof Record && property != null) {
            context.setPropertyResolved(base, property);

            String propertyName = property.toString();

            try {
                base.getClass().getMethod(propertyName);
            } catch (NoSuchMethodException nsme) {
                throw new PropertyNotFoundException(
                        getExceptionMessageString(context, "propertyNotReadable", new Object[] { base.getClass().getName(), property.toString() }));
            }

            return true;
        }
        return false;
    }


    /**
     * If the base object is an instance of {@link Record}, returns the most general type this resolver accepts for the
     * {@code property} argument. Otherwise, returns {@code null}.
     * <p>
     * If the base object is an instance of {@link Record} this method will always return {@link Object} since any
     * object is accepted for the property argument and coerced to a String.
     *
     * @param context The context of this evaluation.
     * @param base    The {@link Record} to analyze.
     *
     * @return {@link Object} is base is an instance of {@link Record}, otherwise {@code null}.
     */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base instanceof Record) {
            // Fields can be of any type
            return Object.class;
        }
        return null;
    }
}
