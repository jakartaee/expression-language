/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
 * Copyright (c) 1997, 2023 Oracle and/or its affiliates and others.
 * All rights reserved.
 * Copyright 2004 The Apache Software Foundation
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
 */

package jakarta.el;

import static jakarta.el.ELUtil.getExceptionMessageString;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines property resolution behavior on objects using the JavaBeans component architecture.
 *
 * <p>
 * If the {@code java.beans.*} package is available (part of the {@code java.desktop} module) the JavaBeans
 * implementation provided by the JRE is used. If the {@code java.beans.*} package is not available, a built-in
 * stand-alone implementation is used that just provides getter/setter support (as everything else requires classes from
 * {@code java.beans.*}).
 * 
 * <p>
 * This resolver handles base objects of any type, as long as the base is not <code>null</code>. It accepts any object
 * as a property or method, and coerces it to a string.
 *
 * <p>
 * For property resolution, the property string is used to find a JavaBeans compliant property on the base object. The
 * value is accessed using JavaBeans getters and setters.
 * </p>
 *
 * <p>
 * For method resolution, the method string is the name of the method in the bean. The parameter types can be optionally
 * specified to identify the method. If the parameter types are not specified, the parameter objects are used in the
 * method resolution.
 * </p>
 *
 * <p>
 * The JavaBeans specification predates the introduction of default method implementations defined on an interface. In
 * addition to the JavaBeans specification requirements for looking up property getters, property setters and methods,
 * this resolver also considers default methods and includes them in the results.
 * </p>
 *
 * <p>
 * The JavaBeans specification predates the introduction of Modules. In addition to the JavaBeans specification
 * requirements for looking up property getters, property setters and methods, this resolver also considers module
 * visibility. 
 * </p>
 *
 * <p>
 * This resolver can be constructed in read-only mode, which means that {@link #isReadOnly} will always return
 * <code>true</code> and {@link #setValue} will always throw <code>PropertyNotWritableException</code>.
 * </p>
 *
 * <p>
 * <code>ELResolver</code>s are combined together using {@link CompositeELResolver}s, to define rich semantics for
 * evaluating an expression. See the javadocs for {@link ELResolver} for details.
 * </p>
 *
 * <p>
 * Because this resolver handles base objects of any type, it should be placed near the end of a composite resolver.
 * Otherwise, it will claim to have resolved a property before any resolvers that come after it get a chance to test if
 * they can do so as well.
 * </p>
 *
 * @see CompositeELResolver
 * @see ELResolver
 * 
 * @since Jakarta Server Pages 2.1
 */
public class BeanELResolver extends ELResolver {

    static private class BPSoftReference extends SoftReference<BeanProperties> {
        final Class<?> key;

        BPSoftReference(Class<?> key, BeanProperties beanProperties, ReferenceQueue<BeanProperties> refQ) {
            super(beanProperties, refQ);
            this.key = key;
        }
    }

    static private class SoftConcurrentHashMap extends ConcurrentHashMap<Class<?>, BeanProperties> {

        private static final long serialVersionUID = -178867497897782229L;
        private static final int CACHE_INIT_SIZE = 1024;
        private ConcurrentHashMap<Class<?>, BPSoftReference> map = new ConcurrentHashMap<>(CACHE_INIT_SIZE);
        private ReferenceQueue<BeanProperties> refQ = new ReferenceQueue<>();

        // Remove map entries that have been placed on the queue by GC.
        private void cleanup() {
            BPSoftReference BPRef = null;
            while ((BPRef = (BPSoftReference) refQ.poll()) != null) {
                map.remove(BPRef.key);
            }
        }

        @Override
        public BeanProperties put(Class<?> key, BeanProperties value) {
            cleanup();
            BPSoftReference prev = map.put(key, new BPSoftReference(key, value, refQ));
            return prev == null ? null : prev.get();
        }

        @Override
        public BeanProperties putIfAbsent(Class<?> key, BeanProperties value) {
            cleanup();
            BPSoftReference prev = map.putIfAbsent(key, new BPSoftReference(key, value, refQ));
            return prev == null ? null : prev.get();
        }

        @Override
        public BeanProperties get(Object key) {
            cleanup();
            BPSoftReference BPRef = map.get(key);
            if (BPRef == null) {
                return null;
            }
            if (BPRef.get() == null) {
                // value has been garbage collected, remove entry in map
                map.remove(key);
                return null;
            }
            return BPRef.get();
        }
    }

    private boolean isReadOnly;

    private final SoftConcurrentHashMap properties = new SoftConcurrentHashMap();

    /*
     * Defines a property for a bean.
     */
    abstract static class BeanProperty {

        final private Class<?> baseClass;
        final private Class<?> type;
        private Method readMethod;
        private Method writeMethod;

        public BeanProperty(Class<?> baseClass, Class<?> type) {
            this.baseClass = baseClass;
            this.type = type;
        }

        public Class<?> getPropertyType() {
            return this.type;
        }

        public boolean isReadOnly(Object base) {
            return getWriteMethod(base) == null;
        }

        public Method getReadMethod(Object base) {
            if (readMethod == null) {
                readMethod = ELUtil.getMethod(baseClass, base, getReadMethod());
            }
            return readMethod;
        }

        public Method getWriteMethod(Object base) {
            if (writeMethod == null) {
                writeMethod = ELUtil.getMethod(baseClass, base, getWriteMethod());
            }
            return writeMethod;
        }
        
        abstract Method getWriteMethod();
        
        abstract Method getReadMethod();
    }

    /*
     * Defines the properties for a bean.
     */
    abstract static class BeanProperties {

        protected final Map<String, BeanProperty> propertyMap = new HashMap<>();
        protected final Class<?> baseClass;

        BeanProperties(Class<?> baseClass) {
            this.baseClass = baseClass;
        }

        public BeanProperty getBeanProperty(String property) {
            return propertyMap.get(property);
        }
    }

    /**
     * Creates a new read/write <code>BeanELResolver</code>.
     */
    public BeanELResolver() {
        this.isReadOnly = false;
    }

    /**
     * Creates a new <code>BeanELResolver</code> whose read-only status is determined by the given parameter.
     *
     * @param isReadOnly <code>true</code> if this resolver cannot modify beans; <code>false</code> otherwise.
     */
    public BeanELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * If the base object is not <code>null</code>, returns the most general acceptable type that can be set on this bean
     * property.
     *
     * <p>
     * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object
     * must be set to <code>true</code> by this resolver, before returning. If this property is not <code>true</code> after
     * this method is called, the caller should ignore the return value.
     * </p>
     *
     * <p>
     * The provided property will first be coerced to a <code>String</code>. If there is a <code>BeanInfoProperty</code>
     * for this property, there were no errors retrieving it and neither the property nor the resolver are read-only,
     * the <code>propertyType</code> of the <code>propertyDescriptor</code> is returned. If the property is resolved but
     * either the property or the resolver is read-only then {@code null} will be returned. Otherwise, a
     * <code>PropertyNotFoundException</code> is thrown.
     * </p>
     *
     * @param context The context of this evaluation.
     * @param base The bean to analyze.
     * @param property The name of the property to analyze. Will be coerced to a <code>String</code>.
     * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then
     * the most general acceptable type which must be {@code null} if the either the property or the resolver is
     * read-only; otherwise undefined
     * @throws NullPointerException if context is <code>null</code>
     * @throws PropertyNotFoundException if <code>base</code> is not <code>null</code> and the specified property does not
     * exist or is not readable.
     * @throws ELException if an exception was thrown while performing the property or variable resolution. The thrown
     * exception must be included as the cause property of this exception, if available.
     */
    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null || property == null) {
            return null;
        }

        BeanProperty beanProperty = getBeanProperty(context, base, property);
        context.setPropertyResolved(true);
        
        if (isReadOnly || beanProperty.isReadOnly(base)) {
            return null;
        }
        
        return beanProperty.getPropertyType();
    }

    /**
     * If the base object is not <code>null</code>, returns the current value of the given property on this bean.
     *
     * <p>
     * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object
     * must be set to <code>true</code> by this resolver, before returning. If this property is not <code>true</code> after
     * this method is called, the caller should ignore the return value.
     * </p>
     *
     * <p>
     * The provided property name will first be coerced to a <code>String</code>. If the property is a readable property of
     * the base object, as per the JavaBeans specification, then return the result of the getter call. If the getter throws
     * an exception, it is propagated to the caller. If the property is not found or is not readable, a
     * <code>PropertyNotFoundException</code> is thrown.
     * </p>
     *
     * @param context The context of this evaluation.
     * @param base The bean on which to get the property.
     * @param property The name of the property to get. Will be coerced to a <code>String</code>.
     * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then
     * the value of the given property. Otherwise, undefined.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws PropertyNotFoundException if <code>base</code> is not <code>null</code> and the specified property does not
     * exist or is not readable.
     * @throws ELException if an exception was thrown while performing the property or variable resolution. The thrown
     * exception must be included as the cause property of this exception, if available.
     */
    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null || property == null) {
            return null;
        }

        Method method = getBeanProperty(context, base, property).getReadMethod(base);
        if (method == null) {
            throw new PropertyNotFoundException(
                    getExceptionMessageString(context, "propertyNotReadable", new Object[] { base.getClass().getName(), property.toString() }));
        }

        Object value;
        try {
            value = method.invoke(base, new Object[0]);
            context.setPropertyResolved(base, property);
        } catch (ELException ex) {
            throw ex;
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        } catch (Exception ex) {
            throw new ELException(ex);
        }

        return value;
    }

    /**
     * If the base object is not <code>null</code>, attempts to set the value of the given property on this bean.
     *
     * <p>
     * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object
     * must be set to <code>true</code> by this resolver, before returning. If this property is not <code>true</code> after
     * this method is called, the caller can safely assume no value was set.
     * </p>
     *
     * <p>
     * If this resolver was constructed in read-only mode, this method will always throw
     * <code>PropertyNotWritableException</code>.
     * </p>
     *
     * <p>
     * The provided property name will first be coerced to a <code>String</code>. If property is a writable property of
     * <code>base</code> (as per the JavaBeans Specification), the setter method is called (passing <code>value</code>). If
     * the property exists but does not have a setter, then a <code>PropertyNotFoundException</code> is thrown. If the
     * property does not exist, a <code>PropertyNotFoundException</code> is thrown.
     * </p>
     *
     * @param context The context of this evaluation.
     * @param base The bean on which to set the property.
     * @param property The name of the property to set. Will be coerced to a <code>String</code>.
     * @param val The value to be associated with the specified key.
     * @throws NullPointerException if context is <code>null</code>.
     * @throws PropertyNotFoundException if <code>base</code> is not <code>null</code> and the specified property does not
     * exist.
     * @throws PropertyNotWritableException if this resolver was constructed in read-only mode, or if there is no setter for
     * the property.
     * @throws ELException if an exception was thrown while performing the property or variable resolution. The thrown
     * exception must be included as the cause property of this exception, if available.
     */
    @Override
    public void setValue(ELContext context, Object base, Object property, Object val) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null || property == null) {
            return;
        }

        if (isReadOnly) {
            throw new PropertyNotWritableException(getExceptionMessageString(context, "resolverNotwritable", new Object[] { base.getClass().getName() }));
        }

        Method method = getBeanProperty(context, base, property).getWriteMethod(base);
        if (method == null) {
            throw new PropertyNotWritableException(
                    getExceptionMessageString(context, "propertyNotWritable", new Object[] { base.getClass().getName(), property.toString() }));
        }

        try {
            method.invoke(base, new Object[] { val });
            context.setPropertyResolved(base, property);
        } catch (ELException ex) {
            throw ex;
        } catch (InvocationTargetException ite) {
            throw new ELException(ite.getCause());
        } catch (Exception ex) {
            if (null == val) {
                val = "null";
            }
            String message = getExceptionMessageString(context, "setPropertyFailed", new Object[] { property.toString(), base.getClass().getName(), val });
            throw new ELException(message, ex);
        }
    }

    /**
     * If the base object is not <code>null</code>, invoke the method, with the given parameters on this bean. The return
     * value from the method is returned.
     *
     * <p>
     * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object
     * must be set to <code>true</code> by this resolver, before returning. If this property is not <code>true</code> after
     * this method is called, the caller should ignore the return value.
     * </p>
     *
     * <p>
     * The provided method object will first be coerced to a <code>String</code>. The methods in the bean is then examined
     * and an attempt will be made to select one for invocation. If no suitable can be found, a
     * <code>MethodNotFoundException</code> is thrown.
     *
     * If the given paramTypes is not <code>null</code>, select the method with the given name and parameter types.
     *
     * Else select the method with the given name that has the same number of parameters. If there are more than one such
     * method, the method selection process is undefined.
     *
     * Else select the method with the given name that takes a variable number of arguments.
     *
     * Note the resolution for overloaded methods will likely be clarified in a future version of the spec.
     *
     * The provide parameters are coerced to the corresponding parameter types of the method, and the method is then
     * invoked.
     *
     * @param context The context of this evaluation.
     * @param base The bean on which to invoke the method
     * @param methodName The simple name of the method to invoke. Will be coerced to a <code>String</code>. If method is
     * "&lt;init&gt;"or "&lt;clinit&gt;" a MethodNotFoundException is thrown.
     * @param paramTypes An array of Class objects identifying the method's formal parameter types, in declared order. Use
     * an empty array if the method has no parameters. Can be <code>null</code>, in which case the method's formal parameter
     * types are assumed to be unknown.
     * @param params The parameters to pass to the method, or <code>null</code> if no parameters.
     * @return The result of the method invocation (<code>null</code> if the method has a <code>void</code> return type).
     * @throws MethodNotFoundException if no suitable method can be found.
     * @throws ELException if an exception was thrown while performing (base, method) resolution. The thrown exception must
     * be included as the cause property of this exception, if available. If the exception thrown is an
     * <code>InvocationTargetException</code>, extract its <code>cause</code> and pass it to the <code>ELException</code>
     * constructor.
     * @since Jakarta Expression Language 2.2
     */
    @Override
    public Object invoke(ELContext context, Object base, Object methodName, Class<?>[] paramTypes, Object[] params) {
        if (base == null || methodName == null) {
            return null;
        }

        Method method = ELUtil.findMethod(base.getClass(), base, methodName.toString(), paramTypes, params, false);

        if (params != null) {
            for (Object param : params) {
                // If the parameters is a LambdaExpression, set the ELContext
                // for its evaluation
                if (param instanceof LambdaExpression) {
                    ((LambdaExpression) param).setELContext(context);
                }
            }
        }

        Object ret = ELUtil.invokeMethod(context, method, base, params);
        context.setPropertyResolved(base, methodName);
        return ret;
    }

    /**
     * If the base object is not <code>null</code>, returns whether a call to {@link #setValue} will always fail.
     *
     * <p>
     * If the base is not <code>null</code>, the <code>propertyResolved</code> property of the <code>ELContext</code> object
     * must be set to <code>true</code> by this resolver, before returning. If this property is not <code>true</code> after
     * this method is called, the caller can safely assume no value was set.
     * </p>
     *
     * <p>
     * If this resolver was constructed in read-only mode, this method will always return <code>true</code>.
     * </p>
     *
     * <p>
     * The provided property name will first be coerced to a <code>String</code>. If property is a writable property of
     * <code>base</code>, <code>false</code> is returned. If the property is found but is not writable, <code>true</code> is
     * returned. If the property is not found, a <code>PropertyNotFoundException</code> is thrown.
     * </p>
     *
     * @param context The context of this evaluation.
     * @param base The bean to analyze.
     * @param property The name of the property to analyzed. Will be coerced to a <code>String</code>.
     * @return If the <code>propertyResolved</code> property of <code>ELContext</code> was set to <code>true</code>, then
     * <code>true</code> if calling the <code>setValue</code> method will always fail or <code>false</code> if it is
     * possible that such a call may succeed; otherwise undefined.
     * @throws NullPointerException if context is <code>null</code>
     * @throws PropertyNotFoundException if <code>base</code> is not <code>null</code> and the specified property does not
     * exist.
     * @throws ELException if an exception was thrown while performing the property or variable resolution. The thrown
     * exception must be included as the cause property of this exception, if available.
     */
    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (context == null) {
            throw new NullPointerException();
        }

        if (base == null || property == null) {
            return false;
        }

        context.setPropertyResolved(true);
        if (isReadOnly) {
            return true;
        }

        return getBeanProperty(context, base, property).isReadOnly(base);
    }

    /**
     * If the base object is not <code>null</code>, returns the most general type that this resolver accepts for the
     * <code>property</code> argument. Otherwise, returns <code>null</code>.
     *
     * <p>
     * Assuming the base is not <code>null</code>, this method will always return <code>Object.class</code>. This is because
     * any object is accepted as a key and is coerced into a string.
     * </p>
     *
     * @param context The context of this evaluation.
     * @param base The bean to analyze.
     * @return <code>null</code> if base is <code>null</code>; otherwise <code>Object.class</code>.
     */
    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base == null) {
            return null;
        }

        return Object.class;
    }

    private BeanProperty getBeanProperty(ELContext context, Object base, Object prop) {
        String property = prop.toString();
        Class<?> baseClass = base.getClass();

        BeanProperties beanProperties = properties.get(baseClass);
        if (beanProperties == null) {
            beanProperties = BeanSupport.getInstance().getBeanProperties(baseClass);
            properties.put(baseClass, beanProperties);
        }

        BeanProperty beanProperty = beanProperties.getBeanProperty(property);
        if (beanProperty == null) {
            throw new PropertyNotFoundException(getExceptionMessageString(context, "propertyNotFound", new Object[] { baseClass.getName(), property }));
        }

        return beanProperty;
    }
}
