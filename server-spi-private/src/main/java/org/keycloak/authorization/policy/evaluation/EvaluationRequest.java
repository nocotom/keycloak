package org.keycloak.authorization.policy.evaluation;

import org.keycloak.authorization.permission.ResourcePermission;

public class EvaluationRequest {

    private final EvaluationContext evaluationContext;

    private final ResourcePermission resourcePermission;

    public EvaluationRequest(EvaluationContext evaluationContext, ResourcePermission resourcePermission) {
        this.evaluationContext = evaluationContext;
        this.resourcePermission = resourcePermission;
    }

    public EvaluationContext getEvaluationContext() {
        return evaluationContext;
    }

    public ResourcePermission getResourcePermission() {
        return resourcePermission;
    }
}
