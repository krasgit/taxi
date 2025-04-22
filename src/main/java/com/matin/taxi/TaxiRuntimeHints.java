package com.matin.taxi;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import com.matin.taxi.model.BaseEntity;
import com.matin.taxi.model.Person;
import com.matin.taxi.vet.Vet;

public class TaxiRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.resources().registerPattern("db/*"); // https://github.com/spring-projects/spring-boot/issues/32654
		hints.resources().registerPattern("messages/*");
		hints.resources().registerPattern("META-INF/resources/webjars/*");
		hints.resources().registerPattern("mysql-default-conf");
		hints.serialization().registerType(BaseEntity.class);
		hints.serialization().registerType(Person.class);
		hints.serialization().registerType(Vet.class);
	}

}
