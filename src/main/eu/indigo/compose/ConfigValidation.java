package eu.indigo.compose.parser;

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

import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.JsonSchema;

/**
 * Configuration validation for yaml provided file
 * @see: https://docs.docker.com/compose/compose-file/
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class ConfigValidation implements Serializable {

    private static final long serialVersionUID = 0L;

    public ConfigValidation() {}

    Set validate(String yml, String schema) {

        ObjectMapper objMapper = new ObjectMapper(new YAMLFactory());

        JsonSchemaFactory factory = JsonSchemaFactory.builder(
                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
                .objectMapper(objMapper).build();

        Set invalidMessages = factory.getSchema(schema)
                .validate(objMapper.readTree(yml))
                .message;
        return invalidMessages;
    }

}
