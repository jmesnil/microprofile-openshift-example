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
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * A Random health check.
 *
 * This health check can be configured with a failure rate to determine how often calls to the health check will fail.
 * By default, this health check is always UP.
 *
 * The configuration property {@code num_failure_rate} can be configured to determine this rate.
 * A value of 0.0 means that the health check is always UP (0% of failure).
 * A value of 1.0 means that the health check is always DOWN (100% of failure).
 * Any value between 0.1 and 1.0 determines the failure rate (e.g. a value of 0.85 means 85% of failure).
 *
 * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2018 Red Hat inc.
 */
@ApplicationScoped
@Health
class RandomHealthCheck implements HealthCheck {

    @Inject
    @ConfigProperty(name="num_failure_rate", defaultValue = "0")
    float failureRate;

    private final Random random = new Random();

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("numbers.randomFailure")
                .withData("num_failure_rate", "" + failureRate)
                .state(random.nextFloat() > failureRate)
                .build();
    }
}
