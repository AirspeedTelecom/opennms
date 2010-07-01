package org.opennms.core.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Webapp class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Webapp {

	String context() default "/";
	
	String path();

}
