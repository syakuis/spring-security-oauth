package io.github.syakuis.account.support;

import io.github.syakuis.account.configuration.JpaConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(JpaConfiguration.class)
public @interface EnableAccountDomain {
}
