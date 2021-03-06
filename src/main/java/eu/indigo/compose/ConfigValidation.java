package eu.indigo.compose.parser;

import java.io.Serializable;
import java.util.Set;
import java.util.LinkedHashSet;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.kohsuke.stapler.DataBoundConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import org.apache.commons.lang3.StringUtils;

/**
 * Configuration validation for yaml provided file
 * @see: https://docs.docker.com/compose/compose-file/
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ConfigValidation implements Serializable {

    private static final long serialVersionUID = 0L;

    public ConfigValidation() {}

    public Set<String> validate(String yml, String schema) throws JsonProcessingException {

        ObjectMapper objMapper = new ObjectMapper(new YAMLFactory());

        JsonSchemaFactory factory = JsonSchemaFactory.builder(
                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
                .objectMapper(objMapper).build();

        Set<ValidationMessage> validationMessages = factory.getSchema(schema)
                .validate(objMapper.readTree(yml));
        
        Set<String> invalidMessages;
        if (validationMessages != null) {
            invalidMessages = new LinkedHashSet<String>();
            for (ValidationMessage validationMessage : validationMessages) {
                invalidMessages.add(validationMessage.getMessage());
            }
        } else {
            invalidMessages = null;
        }
        
        return invalidMessages;
    }

}
