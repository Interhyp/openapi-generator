package org.openapitools.codegen.languages;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.config.GlobalSettings;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GoAutumrestClientCodegen extends GoClientCodegen implements CodegenConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(GoAutumrestClientCodegen.class);
    private final String apiFileFolder;

    private final Set<String> allReferencedModels = new HashSet<>();
    private List<ModelMap> allModels = new ArrayList<>();

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

        for (CodegenOperation operation : operations) {
            for (CodegenParameter param : operation.allParams) {
                // import "reflect" package if the parameter is collectionFormat=multi
                if (param.isCollectionFormatMulti) {
                    //TODO temporally remove this imports again
                    imports.remove(createMapping("import", "reflect"));
                    break;
                }
            }
        }

        String apiNames = GlobalSettings.getProperty("apis");
        if (apiNames != null && !apiNames.isEmpty()) {
            final Map<String, Collection<String>> model2Imports = new HashMap<>();
            for (ModelMap modelMap : allModels) {
                CodegenModel model = modelMap.getModel();
                model2Imports.put(model.getClassname(), model.getImports());
            }

            for (CodegenOperation operation : operations) {
                for (String modelImport : operation.imports) {
                    addModels(modelImport, model2Imports);
                }
            }
            this.allModels = new ArrayList<>(allModels);
        }
        return objs;
    }

    private void addModels(String model, Map<String, Collection<String>> model2Imports) {
        allReferencedModels.add(model);

        Collection<String> importedModels = model2Imports.get(model);
        if (importedModels != null) {
            for (String importedModel : importedModels) {
                if (!allReferencedModels.contains(importedModel)) {
                    addModels(importedModel, model2Imports);
                }
            }
        }
    }

    @Override
    public void postProcess() {
        super.postProcess();

        String apiNames = GlobalSettings.getProperty("apis");
        if (apiNames != null && !apiNames.isEmpty()) {
            final Set<String> nonReferencedModelFilenames = new HashSet<>();
            for (ModelMap modelMap : allModels) {
                String className = modelMap.getModel().getClassVarName();
                if (!allReferencedModels.contains(className)) {
                    for (String templateName : modelTemplateFiles().keySet()) {
                        String modelName = modelMap.getModel().getName();
                        String filename = modelFilename(templateName, modelName);
                        nonReferencedModelFilenames.add(filename);
                    }
                }
            }

            for (String filename : nonReferencedModelFilenames) {
                try {
                    Path path = Path.of(filename);
                    LOGGER.info("drop unreferenced model-file {} again", path);
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
