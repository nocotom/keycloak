/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
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
package org.keycloak.authorization.permission.evaluator;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.keycloak.authorization.Decision;
import org.keycloak.authorization.policy.evaluation.*;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
class IterablePermissionEvaluator implements PermissionEvaluator {

    private final Iterator<EvaluationRequest> evaluationRequests;
    private final PolicyEvaluator policyEvaluator;

    IterablePermissionEvaluator(Iterator<EvaluationRequest> evaluationRequests, PolicyEvaluator policyEvaluator) {
        this.evaluationRequests = evaluationRequests;
        this.policyEvaluator = policyEvaluator;
    }

    @Override
    public void evaluate(Decision decision) {
        try {
            while (this.evaluationRequests.hasNext()) {
                EvaluationRequest evaluationRequest = this.evaluationRequests.next();
                this.policyEvaluator.evaluate(evaluationRequest.getResourcePermission(), evaluationRequest.getEvaluationContext(), decision);
            }
            decision.onComplete();
        } catch (Throwable cause) {
            decision.onError(cause);
        }
    }

    @Override
    public List<Result> evaluate() {
        AtomicReference<List<Result>> result = new AtomicReference<>();

        evaluate(new DecisionResultCollector() {
            @Override
            public void onError(Throwable cause) {
                throw new RuntimeException("Failed to evaluate permissions", cause);
            }

            @Override
            protected void onComplete(List<Result> results) {
                result.set(results);
            }
        });

        return result.get();
    }
}
