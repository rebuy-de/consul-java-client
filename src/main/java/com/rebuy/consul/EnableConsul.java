package com.rebuy.consul;

import com.rebuy.consul.settings.ConsulSettings;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({
    ConsulSettings.class,
    ConsulConfiguration.class
})
public @interface EnableConsul
{
}
