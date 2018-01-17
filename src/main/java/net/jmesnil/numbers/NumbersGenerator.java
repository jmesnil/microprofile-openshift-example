/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package net.jmesnil.numbers;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * The Numbers Generator.
 *
 * This class generates a list of random integers.
 * The list of integers is controlled by 2 configuration properties:
 * * {@code num_size} - the number of generated random integers (3 by default)
 * * {@code num_max} - the maximum value of integer (Integer.MAX_VALUE by default)
 *
 * The minimum value is set to 0.
 *
 * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2018 Red Hat inc.
 */
@Dependent
public class NumbersGenerator {

    @Inject
    @ConfigProperty(name = "num_size", defaultValue = "3")
    private int numSize;

    @Inject
    @ConfigProperty(name = "num_max", defaultValue = "" + Integer.MAX_VALUE)
    private int numMax;

    // minimum value of generated integers
    private static final int MIN_VALUE = 0;

    private final Random random = new Random();

    /**
     * Return an array of random integers.
     *
     * @return an array of random integers
     */
    public int[] nextInts() {
        return random.ints(numSize, MIN_VALUE, numMax)
                .toArray();
    }

    /**
     * Configuration health check.
     *
     * This health check verifies if the configuration of the NumbersGenerator is correct.
     * If that's not the case, calls to the {@link #nextInts()} method may fail (e.g. if the maximum size of integer is negative).
     */
    @ApplicationScoped
    @Health
    static class ConfigHealthCheck implements HealthCheck {

        @Inject
        NumbersGenerator generator;

        private boolean checkConfig() {
            if (generator.numSize < 0 ||
                    generator.numMax <= MIN_VALUE) {
                return false;
            }
            return true;
        }

        @Override
        public HealthCheckResponse call() {
            return HealthCheckResponse.named("numbers.config")
                    .state(checkConfig())
                    .withData("num_size", generator.numSize)
                    .withData("num_max",generator.numMax)
                    .build();
        }
    }
}
