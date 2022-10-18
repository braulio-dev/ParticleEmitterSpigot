package emitters.init;

import com.google.gson.JsonElement;

public record EmitterInitialization(String creationExpression, String perUpdateExpression) {
    public static EmitterInitialization parse(JsonElement emitterInitialization) {
        if (emitterInitialization == null) return new EmitterInitialization(null, null);

        JsonElement creation_expression = emitterInitialization.getAsJsonObject().get("creation_expression");
        JsonElement per_update_expression = emitterInitialization.getAsJsonObject().get("per_update_expression");

        String creationExpression = creation_expression == null ? null : creation_expression.getAsString();
        String perUpdateExpression = per_update_expression == null ? null : per_update_expression.getAsString();

        return new EmitterInitialization(creationExpression, perUpdateExpression);
    }
}