
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Constraint(validatedBy = RegistrationMomentValidator.class)

public @interface ValidRegistrationMoment {

	// Custom properties ------------------------------------------------------

	String min() default "";
	String max() default "";
	boolean past() default false;

	// Standard validation properties -----------------------------------------

	String message() default "{placeholder}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
