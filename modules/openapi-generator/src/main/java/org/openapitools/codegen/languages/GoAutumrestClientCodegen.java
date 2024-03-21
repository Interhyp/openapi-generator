package org.openapitools.codegen.languages;

import com.google.common.collect.Iterables;
import org.openapitools.codegen.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.parameters.Parameter;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public class GoAutumrestClientCodegen extends GoClientCodegen implements CodegenConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(GoAutumrestClientCodegen.class);
    private final String apiFileFolder;

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "go-autumrest";
    }

    public String getHelp() {
        return "Generates a go-autumrest client.";
    }

    public GoAutumrestClientCodegen() {
        super();

        embeddedTemplateDir = templateDir = "go-autumrest";

        apiTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        modelFileFolder = "";
        apiFileFolder = "";
    }

    @Override
    public String apiFileFolder() {
        String folder = outputFolder + File.separator;
        if (StringUtils.isNotBlank(apiFileFolder)) {
            folder += apiFileFolder + File.separator;
        }
        return folder;
    }

    @Override
    public void processOpts() {
        super.processOpts();
        supportingFiles.clear();

//        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));

        supportingFiles.add(new SupportingFile("base_client.mustache", "", "base_client.go"));
        supportingFiles.add(new SupportingFile("client_error.mustache", "", "client_error.go"));
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        objs = super.postProcessModels(objs);

        List<Map<String, String>> imports = objs.getImports();

        for (ModelMap m : objs.getModels()) {
            CodegenModel model = m.getModel();
            if (model.isEnum) {
                continue;
            }
//TODO temporally remove this imports again
            // additional import for different cases
            // oneOf
            if (model.oneOf != null && !model.oneOf.isEmpty()) {
                imports.remove(createMapping("import", "fmt"));
            }

            // anyOf
            if (model.anyOf != null && !model.anyOf.isEmpty()) {
                imports.remove(createMapping("import", "fmt"));
            }

            // additionalProperties: true and parent
            if (model.isAdditionalPropertiesTrue && model.parent != null && Boolean.FALSE.equals(model.isMap)) {
                imports.remove(createMapping("import", "reflect"));
//                imports.add(createMapping("import", "strings"));
            }
        }
        return objs;
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        objs = super.postProcessOperationsWithModels(objs, allModels);

        // remove model imports to avoid error
        List<Map<String, String>> imports = objs.getImports();
        if (imports == null)
            return objs;

        OperationMap objectMap = objs.getOperations();
        List<CodegenOperation> operations = objectMap.getOperation();

        //TODO temporally remove this imports again
        boolean addedReflectImport = false;
        for (CodegenOperation operation : operations) {

            for (CodegenParameter param : operation.allParams) {

                // import "reflect" package if the parameter is collectionFormat=multi
                if (!addedReflectImport && param.isCollectionFormatMulti) {
                    imports.remove(createMapping("import", "reflect"));
                    addedReflectImport = true;
                }
            }
        }

        return objs;
    }
}
